import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        features = {"/Users/ibnuhhazar/Workspace/TangoMobile/feature/Latihan2.feature"},
        plugin = {"json:/Users/ibnuhhazar/Workspace/TangoMobile/target/cucumber-parallel/json/2.json"},
        monochrome = true,
        tags = {"@Registrations1,@Registrations2"},
        glue = {"com.test.stepDef"})
public class Parallel02IT {
}
