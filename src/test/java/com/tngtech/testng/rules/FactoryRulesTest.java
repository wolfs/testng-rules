package com.tngtech.testng.rules;

import static org.testng.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.tngtech.testng.rules.annotations.TestNGRule;
import com.tngtech.testng.rules.spring.EasySpringBean;

@Listeners(RulesListener.class)
public class FactoryRulesTest extends BaseTest {


    @ContextConfiguration({"classpath:spring-context.xml"})
    public static class TestClass {
        private final int value;

        @TestNGRule
        public SpringRule springRule = new SpringRule(this.getClass());

        @Autowired
        private EasySpringBean easySpringBean;

        TestClass(int value) {
            this.value = value;
        }

        @Test
        public void testValueInit() {
            assertEquals(value, 3);
            assertNotNull(easySpringBean);
        }
    }




    @Factory
    public Object[] testsForValues() {
        return new Object[] {new TestClass(3), new TestClass(3)};
    }
}
