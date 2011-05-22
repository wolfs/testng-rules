package com.tngtech.testng.rules;

import java.util.Random;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.annotations.Concurrent;
import com.tngtech.testng.rules.annotations.TestNGRule;

@Listeners(RulesListener.class)
public class ConcurrentTest {
    @TestNGRule
    public ConcurrentRule concurrentRule = new ConcurrentRule();

    @Test
    @Concurrent(15)
    public void myTestMethod() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        System.out.println("Thread " + Thread.currentThread().getName() + " finished");
    }

    @Test(expectedExceptions = {NumberFormatException.class})
    @Concurrent(10)
    public void myTestMethod_failing() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        Integer.parseInt("blabla");
    }
}
