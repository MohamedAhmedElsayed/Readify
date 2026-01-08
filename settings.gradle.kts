pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Readify"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":features:articles:presentation")
include(":designsystem")
include(":features:articles:domain")
include(":features:articles:data")
include(":core:data")
