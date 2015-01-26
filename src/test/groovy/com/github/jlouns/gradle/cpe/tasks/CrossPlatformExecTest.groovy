package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

/**
 * Created on 1/26/15
 *
 * @author jlouns
 */
class CrossPlatformExecTest extends GroovyTestCase {

	private Project project;

	@Override
	void setUp() {
		project = ProjectBuilder.builder().build()

		project.apply plugin: 'com.github.jlouns.cpe'
	}

	Task createTask() {
		Task task = project.task([ 'type': CrossPlatformExec ], 'testCpe') {
			commandLine 'echo', 'foo'
		}

		try {
			task.exec()
		} catch (Exception ex) { }

		task
	}

	void testFormsWindowsExecCall() {
		System.setProperty("os.name", "windows")

		Task task = createTask()

		assertArrayEquals(['cmd', '/c', 'echo', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsLinuxExecCall() {
		System.setProperty("os.name", "linux")

		Task task = createTask()

		assertArrayEquals(['echo', 'foo'].toArray(), task.commandLine.toArray());
	}

}
