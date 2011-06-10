package com.tngtech.testng.rules;


import static org.testng.Assert.*;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.annotations.TestNGRule;

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
