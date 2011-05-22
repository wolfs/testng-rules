package com.tngtech.testng.rules;


import static org.testng.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.annotations.TestNGRule;
import com.tngtech.testng.rules.spring.EasySpringBean;

@Listeners(RulesListener.class)
@ContextConfiguration(locations="classpath:spring-context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class SpringRuleTest extends BaseTest {

    @TestNGRule
    public SpringRule springRule = new SpringRule(this);

    @Autowired
    private EasySpringBean easySpringBean;

    @Test
    public void testSpringContext() {
        assertNotNull(easySpringBean);
    }
}
