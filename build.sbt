import org.scalajs.linker.interface.Report
import sys.process.*

val Scala3 = "3.6.3"
val Version = "0.0.1"

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / excludeLintKeys += taskyFrontend / Compile / stMinimize

/* Handle OS specific usage of commands */
def convertCmd(cmd: String): String = (
    sys.props("os.name").toLowerCase() match {
        case osName if osName contains "windows" => "cmd /C " ++ cmd
        case _ => cmd
    }
) 

/* Global project settings */
inThisBuild(List(
    version := Version,
    organization := "com.github.rileyHorrix",
    startYear := Some(2025),
    licenses := List("MIT" -> url("https://opensource.org/licenses/MIT")),
    developers := List(
        Developer("Riley-horrix", "Riley Horrix", "", url("https://github.com/j-mie6/parsley-debug-app")),
    ),
    versionScheme := Some("early-semver"),
    scalaVersion := Scala3,
))

lazy val isRelease = sys.env.get("RELEASE").contains("true") /* Compile in release mode (not dev) */

/* Setup for Laminar */
lazy val taskyFrontend = project
    .in(file("frontend"))
    .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterExternalNpmPlugin)
    .settings(
        /* Scala JS/ScalablyTyped settings */
        scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.ESModule)),
        scalaJSUseMainModuleInitializer := true,
        stUseScalaJsDom := true,
        stIgnore += "type-fest",
        Compile / stMinimize := Selection.AllExcept("types"),

        /* Frontend settings */        
        name := "tasky-frontend",
        libraryDependencies ++= Seq( /* Extra dependencies required for frontend */
            "org.scala-lang" %% "scala3-compiler" % "3.3.3",
            "com.raquo" %%% "laminar" % "17.2.0",
            "com.lihaoyi" %%% "upickle" % "4.1.0"
        ),

        /* Run npm to link with ScalablyTyped */
        externalNpm := {
            convertCmd("npm").!
            println()
            baseDirectory.value.getParentFile()
        },

        /* Compile the frontend */
        Compile / packageSrc / mappings ++= {
            val base = (Compile / sourceManaged).value
            val files = (Compile / managedSources).value
            files.map { f => (f, f.relativeTo(base).get.getPath) }
        }
    )


/* Report frontend build setup */
lazy val reportFrontend = taskKey[(Report, File)]("")
ThisBuild / reportFrontend := {
    if (isRelease) {
        println("Building in release mode")
    } else {
        println("Building in quick compile mode. To build in release mode, set RELEASE environment variable to \"true\"")
    }

    (taskyFrontend / Compile / fullLinkJS).value.data -> (taskyFrontend / Compile / fullLinkJS / scalaJSLinkerOutputDirectory).value
}

/* Build Dill frontend */
lazy val buildFrontend = taskKey[Map[String, File]]("Build the Scala Laminar frontend.")

buildFrontend := {
    val (report, fm) = reportFrontend.value
    val outDir = (ThisBuild / baseDirectory).value / "static"
    
    IO.listFiles(fm)
        .map { file =>
            val (name, ext) = file.baseAndExt
            val out = outDir / (name + "." + ext)

            IO.copyFile(file, out)

            file.name -> out
        }
        .toMap
}

/* Build backend */
lazy val buildBackend = taskKey[Unit]("Build the backend application.")

buildBackend := {
    convertCmd("npm run tauri build").!
}

/* Run backend in dev mode */
lazy val runBackend = taskKey[Unit]("Run backend in development mode.")

runBackend := {    
    convertCmd("npm run tauri dev").!
}

/* Build frontend and backend into executables */
val build = taskKey[Unit]("Build the project.")

build := {
    buildFrontend.value
    buildBackend.value
}

/* Run project - install dependencies, build frontend then run backend */
run := {
    setup.value
    buildFrontend.value
    runBackend.value
}

/* Setup required dependencies */
lazy val setup = taskKey[Unit]("Install required dependencies.")

setup := {
    convertCmd("npm install").!
}

/* Clean all generated files */
val cleanHard = taskKey[Unit]("Remove generated files and dependencies.")

cleanHard := {
    print("Removing npm dependencies... ")
    "rm -rf node_modules/".!
    println("done")

    print("Removing Scala files... ")
    "rm -rf .metals/".!
    "rm -rf .bloop/".!
    "rm -rf .bsp/".!
    "rm -rf .scala-build/".!
    println("done")
    
    print("Removing targets... ")
    "rm -rf static/".!
    println("done")

    print("Removing backend targets... ")
    "rm -rf backend/target/".!
    println("done")
    
    print("Removing frontend targets... ")
    "rm -rf frontend/target/".!
    println("done")

    /* Apply default clean */
    clean.value
}