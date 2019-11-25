package com.github.mpkorstanje.tests;

import io.cucumber.java.en.Given;

public class CucumberStepDefinitions {

    @Given("a single step")
    public void aSingleStep(){
        System.out.println("CucumberTest was executed");
    }

}
