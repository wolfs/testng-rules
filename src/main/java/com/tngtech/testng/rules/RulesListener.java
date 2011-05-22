package com.tngtech.testng.rules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.tngtech.testng.rules.annotations.TestNGRule;

public class RulesListener implements IHookable, ITestListener {

    private static interface Function0<T> {
        public void apply(T arg);
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        List<IHookable> hookables = getRules(testResult.getInstance(),
                IHookable.class);
        if (hookables.isEmpty()) {
            callBack.runTestMethod(testResult);
        } else {
            IHookable hookable = hookables.get(0);
            hookables.remove(0);
            for (IHookable iHookable : hookables) {
                hookable = compose(hookable, iHookable);
            }
            hookable.run(callBack, testResult);
        }
    }

    private IHookable compose(final IHookable first, final IHookable second) {
        return new IHookable() {

            @Override
            public void run(final IHookCallBack callBack,
                    final ITestResult testResult) {
                first.run(new IHookCallBack() {

                    @Override
                    public void runTestMethod(ITestResult testResult) {
                        second.run(callBack, testResult);
                    }

                    @Override
                    public Object[] getParameters() {
                        return callBack.getParameters();
                    }
                }, testResult);
            }
        };
    }

    private <T> List<T> getRules(Object object, Class<T> type) {
        List<T> rules = new ArrayList<T>();
        Field[] declaredFields = object.getClass().getFields();
        for (Field field : declaredFields) {
            TestNGRule annotation = field.getAnnotation(TestNGRule.class);
            if (annotation != null) {
                try {
                    Object fieldContent = field.get(object);
                    if (type.isAssignableFrom(field.getType())) {
                        @SuppressWarnings("unchecked")
                        T rule = (T) fieldContent;
                        rules.add(rule);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rules;
    }

    @Override
    public void onTestStart(final ITestResult result) {
        executeRulesForInstance(new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onTestStart(result);
            }
        }, result.getInstance());
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        executeRulesForInstance(new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onTestSuccess(result);
            }
        }, result.getInstance());
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        executeRulesForInstance(new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onTestFailure(result);
            }
        }, result.getInstance());
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        executeRulesForInstance(new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onTestSkipped(result);
            }
        }, result.getInstance());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
        executeRulesForInstance(new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onTestFailedButWithinSuccessPercentage(result);
            }
        }, result.getInstance());
    }

    @Override
    public void onStart(final ITestContext context) {
        executeRulesForContext(context,
                new Function0<ITestListener>() {
                    @Override
                    public void apply(ITestListener listener) {
                        listener.onStart(context);
                    }
                });
    }

    @Override
    public void onFinish(final ITestContext context) {
        executeRulesForContext(context, new Function0<ITestListener>() {
            @Override
            public void apply(ITestListener listener) {
                listener.onFinish(context);
            }
        });
    }

    private void executeRulesForContext(ITestContext context,
            Function0<ITestListener> action) {
        ITestNGMethod[] allTestMethods = context.getAllTestMethods();
        Set<Object> testInstances = new HashSet<Object>();
        for (ITestNGMethod iTestNGMethod : allTestMethods) {
            testInstances.addAll(Arrays.asList(iTestNGMethod.getInstances()));
        }
        for (Object instance : testInstances) {
            executeRulesForInstance(action, instance);
        }
    }

    private void executeRulesForInstance(Function0<ITestListener> action,
            Object allInst) {
        List<ITestListener> hookables = getRules(allInst, ITestListener.class);
        for (ITestListener listener : hookables) {
            action.apply(listener);
        }
    }

}
