package plugins.catchtest.codecoverage

import org.gradle.model.Managed
import org.gradle.model.ModelMap
import org.gradle.model.RuleSource

@Managed
interface CodeCoverage {
    ModelMap<WholeArchive> getWholeArchives()
}