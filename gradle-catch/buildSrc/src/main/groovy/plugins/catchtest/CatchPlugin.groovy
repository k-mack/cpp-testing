package plugins.catchtest

import org.gradle.model.Model
import org.gradle.model.RuleSource

import plugins.catchtest.codecoverage.CodeCoverage

class CatchPlugin extends RuleSource {
    @Model
    void catchTests(CatchTest c) {}

    @Model
    void codeCoverage(CodeCoverage c) {}
}