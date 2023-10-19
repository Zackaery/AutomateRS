version = "early-access"

project.extra["PluginName"] = "AutomateRS"
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

dependencies {
    implementation(group = "net.java.dev.jna", name = "jna-platform", version = "5.9.0")
    implementation(group = "org.apache.httpcomponents", name = "httpclient", version = "4.5.13")
    implementation(group = "org.apache.httpcomponents", name = "httpmime", version = "4.4")
}
