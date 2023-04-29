package org.example.component;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = { "pretty" },
    features = "src/test/resources/features/resource_processing.feature",
    glue = "org.example.component.steps"
)
public class ResourceProcessingCucumberTest {

}
