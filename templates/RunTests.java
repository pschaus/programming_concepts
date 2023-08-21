import com.github.guillaumederval.javagrading.GradingListener;
import org.junit.runner.JUnitCore;

public class RunTests {{
    public static void main(String args[]) {{
        JUnitCore runner = new JUnitCore();
        runner.addListener(new GradingListener());
			runner.run({0}.{1}.class);
    }}
}}
