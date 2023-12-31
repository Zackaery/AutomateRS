dependencies {
    implementation(group = "com.formdev", name = "flatlaf", version = "3.2.1")
}
version = "early-access"

project.extra["PluginName"] = "AutomateRS - Manager"
project.extra["PluginDescription"] = "RuneScape - Automated"

tasks {
    jar {
        manifest {
            attributes(mapOf(
                "Plugin-Version" to project.version,
                "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                "Plugin-Provider" to project.extra["PluginProvider"],
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}
