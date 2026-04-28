pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "GreetingProcessorProject"
include("annotations")
include("processor")
include("app")