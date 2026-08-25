#!/bin/python3
# run the student code using maven, retrieve the rst table and gives the grade
import subprocess
import re
import sys

from inginious import feedback
from inginious import rst
from inginious import input

code_file = 'src/main/java/{0}/{1}.java'
input.parse_template(code_file)

FAIL_IF_STDERR = True

MIRROR_FLAGS = [
    '-s', 'settings-maven-cache.xml',
    '-Dmaven.wagon.http.connectionTimeout=5000',
    '-Dmaven.wagon.http.readTimeout=5000',
    '-Daether.connector.connectTimeout=5000',
    '-Daether.connector.requestTimeout=5000',
]


def run_mvn(flags = []):
    command = [ 'mvn', '-q', '-am', 'test' ] + flags

    process = subprocess.run(
        command,
        shell=False,  # Allow "command" to be a list of strings (not just a string)
        stdout=subprocess.PIPE,
        stderr=None if FAIL_IF_STDERR else subprocess.DEVNULL,
    )

    # Maven only uses the stdout for its results
    return process.stdout.decode().split('\n')


# 1st attempt: use Maven mirror but with a short timeout, so a dead
# mirror fails fast instead of hanging.
out = run_mvn(MIRROR_FLAGS)


is_error_with_mirror = False
for l in out:
    if l.startswith('[ERROR]') and 'org.apache.maven.plugins:maven-resources-plugin' in l:
        is_error_with_mirror = True
        break


if is_error_with_mirror:
    # Mirror likely down (or something else failed fast) -> retry once with
    # the fallback settings.xml (no mirror, hits Central directly), this
    # time without an artificial timeout so a normal-length build can finish.
    out = run_mvn()


try:
    start = -1
    end = -1
    for i, line in enumerate(out):
        if line == "--- GRADE ---":
            start = i
        if line == "--- END GRADE ---":
            end = i
    assert start < end
    assert start != -1
    assert end != -1
    out = out[start:end+1]

    def get_grade(line):
        match = re.search(r"^TOTAL *([0-9\.]+)/([0-9\.]+)$", line)
        a = float(match.group(1))
        b = float(match.group(2))
        return a/b if b != 0 else 0.0

    g = get_grade(out[-3])

    g *= 100
    g = int(round(g))

    feedback.set_global_result("failed" if g != 100 else "success")
    feedback.set_grade(g)
    feedback.set_global_feedback("\n".join(out[1:-3]), append=True)
except:
    feedback.set_global_result("failed")
    feedback.set_grade(0.0)
    mvn_feedback = rst.get_codeblock("bash", "\n".join(out))
    feedback.set_global_feedback("An error occured while grading. Here is the output of the command `mvn -q -am test`:", append=True)
    feedback.set_global_feedback(mvn_feedback, append=True)
