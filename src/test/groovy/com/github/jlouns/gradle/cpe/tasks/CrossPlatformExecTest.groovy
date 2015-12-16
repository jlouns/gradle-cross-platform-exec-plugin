package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission

class CrossPlatformExecTest extends GroovyTestCase {

	private static String separator = File.separator;

	private Project project;
	private Path testDir;
	private Path[] commandFiles;

	@Override
	void setUp() {
		project = ProjectBuilder.builder().build()

		project.apply plugin: "com.github.jlouns.cpe"

		testDir = Paths.get("testdir");

		commandFiles = [
			Paths.get("one"),
			Paths.get("one.cmd"),
			Paths.get("two.bat"),
			Paths.get("two.sh"),
			Paths.get("three"),
			Paths.get("three.exe"),
			testDir.resolve("four.sh"),
			testDir.resolve("four.exe")
		];

		Files.createDirectory(testDir);

		commandFiles.each {
			Files.createFile(it)
			try {
				Files.setPosixFilePermissions(it, EnumSet.allOf(PosixFilePermission))
			} catch (Exception ignore) {
				// Ignore it, probably on windows
			}
		}
	}

	@Override
	void tearDown() {
		commandFiles.each {
			Files.delete(it)
		}
		Files.delete(testDir);
	}

	Task createTask(String executable) {
		Task task = project.task([ "type": CrossPlatformExec ], "testCpe") {
			commandLine executable, "foo"
		}

		try {
			task.exec()
		} catch (Exception ignore) {
			ignore.printStackTrace()
		}

		task
	}

	static void asOs(String os) {
		System.setProperty("os.name", os)
	}

	void testFormsLinuxExecCallForCommand() {
		asOs("linux")

		Task task = createTask("echo")

		assertArrayEquals(["echo", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsLinuxExecCallForFile() {
		asOs("linux")

		Task task = createTask("one")

		assertArrayEquals([".${separator}one", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsLinuxExecCallForShFile() {
		asOs("linux")

		Task task = createTask("two")

		assertArrayEquals([".${separator}two.sh", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForCommand() {
		asOs("windows")

		Task task = createTask("echo")

		assertArrayEquals(["cmd", "/c", "echo", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForCmdFile() {
		asOs("windows")

		Task task = createTask("one")

		assertArrayEquals(["cmd", "/c", "one.cmd", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForBatFile() {
		asOs("windows")

		Task task = createTask("two")

		assertArrayEquals(["cmd", "/c", "two.bat", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForExeFile() {
		asOs("windows")

		Task task = createTask("three")

		assertArrayEquals(["cmd", "/c", "three.exe", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFixesPathSeparatorsOnLinux() {
		asOs("linux")

		Task task = createTask(".\\testdir\\four")

		assertArrayEquals([".${separator}testdir${separator}four.sh", "foo"].toArray(), task.commandLine.toArray());
	}

	void testFixesPathSeparatorsOnWindows() {
		asOs("windows")

		Task task = createTask("./testdir/four")

		assertArrayEquals(["cmd", "/c", "testdir${separator}four.exe", "foo"].toArray(),
			task.commandLine.toArray());
	}

}
