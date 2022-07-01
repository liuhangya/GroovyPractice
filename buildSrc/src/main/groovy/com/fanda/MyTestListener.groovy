package com.fanda

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

class MyTestListener implements TestListener{

    @Override
    void beforeSuite(TestDescriptor testDescriptor) {

    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        if (!suite.parent && result.getTestCount() > 0) {
            def time = result.getEndTime() - result.getStartTime()
            // 测试套件耗时：1333
            println("测试套件耗时：$time")
        }
    }

    @Override
    void beforeTest(TestDescriptor testDescriptor) {

    }

    @Override
    void afterTest(TestDescriptor suite, TestResult result) {
        if (result.getTestCount() > 0) {
            def time = result.getEndTime() - result.getStartTime()
            // 测试类耗时：5
            println("测试类耗时：$time")
        }
    }
}