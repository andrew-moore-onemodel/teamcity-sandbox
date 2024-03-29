import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.10"

project {

    buildType(BuildBackend)
    buildType(BuildContainers)
    buildType(BuildFrontend)
    buildType(BuildStorybook)
}

object BuildFrontend : BuildType({
    name = "Build React"

    params {
        param("env.DISABLE_ESLINT_PLUGIN", "true")
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        nodeJS {
            name = "Build React"
            workingDir = "frontend"
            shellScript = """
                npm install
                npm run build
            """.trimIndent()
        }
        nodeJS {
            name = "React Tests"
            workingDir = "frontend"
            shellScript = """
                npm install
                npm run test:ci
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object BuildBackend : BuildType({
    name = "Build Backend"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetBuild {
            projects = "backend/Webapp.sln"
            sdk = "6"
        }
    }
})

object BuildContainers : BuildType({
    name = "BuildContainers"

    dependencies {
        snapshot(BuildFrontend) {
        }
        snapshot(BuildBackend) {
        }
        snapshot(BuildStorybook) {
        }
    }
})

object BuildStorybook : BuildType({
    name = "Build Storybook"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        nodeJS {
            name = "Build Storybook"
            workingDir = "frontend"
            shellScript = """
                npm install
                npm run build-storybook
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
