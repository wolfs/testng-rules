package com.tngtech.testng.rules;


import static org.testng.Assert.*;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.annotations.TestNGRule;

@Listeners(RulesListener.class)
public class RuleTest {
    private int counter = 0;
    private int counter2 = 0;

    @TestNGRule
    public final IHookable incrementRule = new IHookable() {

        @Override
        public void run(IHookCallBack callBack, ITestResult testResult) {
            counter += 2;
            callBack.runTestMethod(testResult);
        }
    };

    @TestNGRule
    public IHookable incrementRule2 = new IHookable() {

        @Override
        public void run(IHookCallBack callBack, ITestResult testResult) {
            counter *= 3;
            callBack.runTestMethod(testResult);
        }
    };

    @TestNGRule
    public ITestListener addOne = new TestWatchman() {
        @Override
        public void beforeMethod(ITestResult result) {
        }

        @Override
        public void onStart(ITestContext context) {
            counter2 +=1;
        }

        @Override
        public void afterMethod(ITestResult result) {
        }

    };

    @Test
    public void checkItWorked() {
        System.out.println("Test");
        assertEquals(counter, 6);
        assertEquals(counter2,1);
    }

    @Test
    public void checkItWorkedTwice() {
        System.out.println("Test");
        assertEquals(counter, 24);
        assertEquals(counter2,1);
    }

}
