package com.tngtech.testng.rules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.test.context.TestContextManager;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SkipException;

public class SpringRule implements IHookable, ITestListener {
    private static class SpringTestFailure extends SkipException {
        public SpringTestFailure(String skipMessage, Throwable cause) {
            super(skipMessage, cause);
        }

        @Override
        public boolean isSkip() {
            return false;
        }

    }

    private final Class<?> testClass;

    private TestContextManager testContextManager;

    private Throwable testException;

    public SpringRule(Object testClass) {
        this.testClass=testClass.getClass();
    }

    public SpringRule(Class<?> testClass) {
        this.testClass=testClass;
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            Method method = result.getMethod().getConstructorOrMethod()
                    .getMethod();
            this.testContextManager.beforeTestMethod(result.getInstance(),
                    method);
        } catch (Exception e) {
            throw new SpringTestFailure(
                    "Failed to setup Spring for TestMethod", e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        springAfterTest(result);
    }

    private void springAfterTest(ITestResult result) {
        try {
            this.testContextManager.afterTestMethod(result.getInstance(),
                    result.getMethod().getConstructorOrMethod().getMethod(),
                    this.testException);
        } catch (Exception e) {
            throw new SpringTestFailure(
                    "Failed to tear down Spring for TestMethod", e);
        } finally {
            this.testException = null;
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        springAfterTest(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        springAfterTest(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        springAfterTest(result);
    }

    @Override
    public void onStart(ITestContext context) {
        testContextManager = new TestContextManager(testClass);
        try {
            this.testContextManager.beforeTestClass();
            ITestNGMethod[] allTestMethods = context.getAllTestMethods();
            Set<Object> testInstances = new HashSet<Object>();
            for (ITestNGMethod iTestNGMethod : allTestMethods) {
                testInstances
                        .addAll(Arrays.asList(iTestNGMethod.getInstances()));
            }
            for (Object instance : testInstances) {
                this.testContextManager.prepareTestInstance(instance);
            }
        } catch (Exception e) {
            throw new SkipException("SpringRule BeforeTestClass failed");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            this.testContextManager.afterTestClass();
        } catch (Exception e) {
            throw new SpringTestFailure("Failed to run afterClass", e);
        }
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);

        Throwable testResultException = testResult.getThrowable();
        if (testResultException instanceof InvocationTargetException) {
            testResultException = ((InvocationTargetException) testResultException)
                    .getCause();
        }
        this.testException = testResultException;
    }

}
