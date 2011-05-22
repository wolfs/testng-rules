package com.tngtech.testng.rules;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public abstract class TestWatchman implements ITestListener {

    public abstract void beforeMethod(ITestResult result);
    public abstract void afterMethod(ITestResult result);

    @Override
    public void onTestStart(ITestResult result) {
        beforeMethod(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        afterMethod(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        afterMethod(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        afterMethod(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        afterMethod(result);
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }

}
