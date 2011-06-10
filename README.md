TestNG Rule Support
===================

About
-----

This is a prototype of a Rule (like in JUnit Rules) implementation for TestNG.

You need to annotate your test class with @Listeners(RulesListener.class) to activate rule support.
Then any public field of the class of type IHookable or ITestListener will be executed at the appropriate
time. Multiple IHookables will be chained.

Example
-------

Consider the following test:

    @Listeners(RulesListener.class)
    public class RuleTest {
        private int counter = 0;

        @TestNGRule
        public final IHookable incrementRule = new IHookable() {

            @Override
            public void run(IHookCallBack callBack, ITestResult testResult) {
                counter += 2;
                callBack.runTestMethod(testResult);
            }
        };

        @Test
        public void checkItWorked() {
            System.out.println("Test");
            assertEquals(counter, 2);
        }
    }
    
The rule will then be executed (like an IHookable) instead of each method.

In src/test there is also a SpringRule which works like using the AbstractTestNGSpringContextTests as
a base class, but using composition (with Rules) instead of inheritance. An class then is the SpringRuleTest.

I also implement the ConcurrentRule from [ConcurrentRule].

TODO
----

* Implement classes implementing ITestListener for some common usecases like before/afterClass, ...
* Make other ITestNGListeners possible
* Add DependsOn attribute to the Rule annotation to make ordering possible
    
[ConcurrentRule]: http://blog.mycila.com/2009/11/writing-your-own-junit-extensions-using.html