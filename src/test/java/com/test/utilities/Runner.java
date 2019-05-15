package com.test.utilities;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"html:target//cucumber-pretty" }, 
		features = { "feature" },
	    tags = { "@Registrations1" },
		glue = { "com.test.stepDef", "com.test.utilities" })
public class Runner {
	
}
