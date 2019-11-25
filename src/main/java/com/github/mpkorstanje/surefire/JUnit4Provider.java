package com.github.mpkorstanje.surefire;

import org.junit.runner.JUnitCore;

import java.util.Set;

public class JUnit4Provider implements Provider {
    @Override
    public Set<String> discoverTests(SurefireTestDiscoveryRequest surefireRequest) {
        System.out.println("JUnit 4 does not support discovery returning tests discovered by surefire");
        return surefireRequest.testsDiscoveredBySurefire;
    }

    @Override
    public void execute(String test) {
        try {
            Class<?> aTest = Class.forName(test);
            System.out.println("JUnit 4 Executing  " + aTest);
            JUnitCore.runClasses(aTest);
            System.out.println("JUnit 4 Executed  " + aTest);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
