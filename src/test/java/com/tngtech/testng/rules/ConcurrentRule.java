package com.tngtech.testng.rules;

import java.util.concurrent.CountDownLatch;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.tngtech.testng.rules.annotations.Concurrent;

public class ConcurrentRule implements IHookable {

    @Override
    public void run(final IHookCallBack callBack, final ITestResult testResult) {
        ITestNGMethod frameworkMethod = testResult.getMethod();
        Concurrent concurrent = frameworkMethod.getConstructorOrMethod().getMethod().getAnnotation(Concurrent.class);
        if (concurrent == null) {
            callBack.runTestMethod(testResult);
        } else {
            final String name = frameworkMethod.getMethodName();
            final Thread[] threads = new Thread[concurrent.value()];
            final CountDownLatch go = new CountDownLatch(1);
            final CountDownLatch finished = new CountDownLatch(threads.length);
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            go.await();
                            callBack.runTestMethod(testResult);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (Throwable throwable) {
                            if (throwable instanceof RuntimeException) {
                                throw (RuntimeException) throwable;
                            }
                            if (throwable instanceof Error) {
                                throw (Error) throwable;
                            }
                            RuntimeException r = new RuntimeException(throwable.getMessage(), throwable);
                            r.setStackTrace(throwable.getStackTrace());
                            throw r;
                        } finally {
                            finished.countDown();
                        }
                    }
                }, name + "-Thread-" + i);
                threads[i].start();
            }
            go.countDown();
            try {
                finished.await();
            } catch (Exception e) {
                throw new RuntimeException("Failed to run tests concurrently", e);
            }
        }

    }
}
