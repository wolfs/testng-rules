package com.tngtech.testng.rules;


import static org.testng.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.spring.EasySpringBean;

@ContextConfiguration(locations="classpath:spring-context.xml")
public class SpringTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private EasySpringBean easySpringBean;

    @BeforeSuite
    public static void initLogging() {
        BasicConfigurator.configure();
    }

    @Test
    public void testSpringContext() {
        assertNotNull(easySpringBean);
    }
}
