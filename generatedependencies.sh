#!/bin/bash

echo "<dependencies>"

for jar in libs/*.jar; do
  filename=$(basename -- "$jar")
  base="${filename%.*}"

  # Use the last occurrence of '-' to split into name and version (if it exists)
  artifactId="${base%-*}"
  version="${base##*-}"
  if [ "$artifactId" == "$version" ]; then
      version="unknown"
  fi

  echo "  <dependency>"
  echo "      <groupId>local.dependency</groupId>"  # Default groupId as we can't determine it from the filename
  echo "      <artifactId>$artifactId</artifactId>"
  echo "      <version>$version</version>"
  echo "      <scope>system</scope>"
  echo "      <systemPath>\${project.basedir}/libs/$filename</systemPath>"
  echo "  </dependency>"
done

echo "</dependencies>"