package org.example;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

/**
 * Class to init test-suite
 */
public class TESTMAIN {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();

        List<String> suites = Lists.newArrayList();
        suites.add("src/test/resources/testng.xml");
        // for local testing


        testNG.setTestSuites(suites);
        testNG.run();
    }
}