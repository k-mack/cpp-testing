package plugins.catchtest

import org.gradle.model.Managed
import org.gradle.model.ModelMap
import org.gradle.model.RuleSource

@Managed
interface CatchTest {
    ModelMap<CatchTestDriver> getTestDrivers()
    ModelMap<CatchTestSuite> getTestSuites()
}