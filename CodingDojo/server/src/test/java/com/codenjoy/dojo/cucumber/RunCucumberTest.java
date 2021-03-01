package com.codenjoy.dojo.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber.html", "junit:target/junit-report.xml"},
        features = "src/test/resources/cucumber")
public class RunCucumberTest {

}
