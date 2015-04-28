# gradle-cross-platform-exec-plugin
 [![Build Status](https://travis-ci.org/jlouns/gradle-cross-platform-exec-plugin.svg)](https://travis-ci.org/jlouns/gradle-cross-platform-exec-plugin)
 [![Download](https://api.bintray.com/packages/jlouns/maven/gradle-cross-platform-exec-plugin/images/download.svg)](https://bintray.com/jlouns/maven/gradle-cross-platform-exec-plugin/_latestVersion)

Gradle plugin which provides an exec task that works on Unix or Windows-based systems

## Installing

Releases of this plugin are hosted on [Gradle's Plugin Repository](https://login.gradle.org/plugin/com.github.jlouns.cpe).
Apply the plugin to your project using one of the two methods below.

### Gradle 2.0 and older

```groovy
buildscript {
	repositories {
		maven {
			url "https://plugins.gradle.org/m2/"
		}
	}
	dependencies {
		classpath 'com.github.jlouns:gradle-cross-platform-exec:0.2.0'
	}
}

apply plugin: 'com.github.jlouns.cpe'
```

### Gradle 2.1 and newer

```groovy
plugins {
	id 'com.github.jlouns.cpe' version '0.2.0'
}
```

## Usage

This plugin enables a `CrossPlatformExec` task type in your buildscript which behaves exactly like a typical Gradle
`Exec` task, except that it normalizes calls to work across operating systems. It does this by:

1. Searching for a file matching the name of the command and executing the matching file if it exists
  - Files ending with .bat and .cmd are matched on Windows
  - Files ending with .sh and nothing are matched on \*nix
2. Prepending each command with `cmd /c` on Windows

To define a `CrossPlatformExec` task, simply specify the task `type`:

```groovy
task foo(type: CrossPlatformExec) {
	commandLine 'echo', 'bar'
}
```

All of the same options for a typical [Exec](https://gradle.org/docs/current/dsl/org.gradle.api.tasks.Exec.html)
task are available.

## License

MIT © [Jonathan Lounsbury](https://github.com/jlouns)
