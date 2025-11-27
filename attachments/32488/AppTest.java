package gti.test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import static org.junit.Assert.*;

@RunWith(AppTest.CustomRunner.class)
public class AppTest
{
  public static class CustomRunner
      extends Runner
  {
    private final Description description;
    private final Map<AppTest, Description> tests;

    public CustomRunner(Class<AppTest> testClass)
    {
      description = Description.createSuiteDescription(testClass.getSimpleName());
      final int numTests = 100000;
      tests = new LinkedHashMap<AppTest, Description>(numTests);
      for (int i = 0; i < numTests; i++)
      {
        AppTest test = new AppTest();
        Description testDesc = Description.createTestDescription(AppTest.class,
                                                                 "test" + (i % 100));
        description.addChild(testDesc);
        tests.put(test,
                  testDesc);
      }
    }

    @Override
    public Description getDescription()
    {
      return description;
    }

    @Override
    public void run(RunNotifier notifier)
    {
      for (Entry<AppTest, Description> me : tests.entrySet())
      {
        AppTest test = me.getKey();
        Description testDesc = me.getValue();
        notifier.fireTestStarted(testDesc);
        try
        {
          test.testIt();
          notifier.fireTestFinished(testDesc);
        }
        catch (Throwable ex)
        {
          notifier.fireTestFailure(new Failure(testDesc,
                                               ex));
        }
      }
    }
  }

  static private int counter = 0;

  public void testIt()
  {
    assertTrue(counter++ % 8888 != 0);
  }
}
