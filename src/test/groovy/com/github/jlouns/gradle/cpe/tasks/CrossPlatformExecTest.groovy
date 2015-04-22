package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created on 1/26/15
 *
 * @author jlouns
 */
class CrossPlatformExecTest extends GroovyTestCase {

	private Project project;
	private Path[] commandFiles;

	@Override
	void setUp() {
		project = ProjectBuilder.builder().build()

		project.apply plugin: 'com.github.jlouns.cpe'

		commandFiles = [
			Paths.get('one'),
			Paths.get('one.cmd'),
			Paths.get('two.bat'),
			Paths.get('two.sh')
		];

		commandFiles.each {
			Files.createFile(it)
		}
	}

	@Override
	void tearDown() {
		commandFiles.each {
			Files.delete(it)
		}
	}

	Task createTask(String executable) {
		Task task = project.task([ 'type': CrossPlatformExec ], 'testCpe') {
			commandLine executable, 'foo'
		}

		try {
			task.exec()
		} catch (Exception ex) {
			println(ex.stackTrace)
			// Ignore it
		}

		task
	}

	static void asOs(String os) {
		System.setProperty('os.name', os)
	}

	void testFormsLinuxExecCallForCommand() {
		asOs('linux')

		Task task = createTask('echo')

		assertArrayEquals(['echo', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsLinuxExecCallForFile() {
		asOs('linux')

		Task task = createTask('one')

		assertArrayEquals(['./one', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsLinuxExecCallForShFile() {
		asOs('linux')

		Task task = createTask('two')

		assertArrayEquals(['./two.sh', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForCommand() {
		asOs('windows')

		Task task = createTask('echo')

		assertArrayEquals(['cmd', '/c', 'echo', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForCmdFile() {
		asOs('windows')

		Task task = createTask('one')

		assertArrayEquals(['cmd', '/c', 'one.cmd', 'foo'].toArray(), task.commandLine.toArray());
	}

	void testFormsWindowsExecCallForBatFile() {
		asOs('windows')

		Task task = createTask('two')

		assertArrayEquals(['cmd', '/c', 'two.bat', 'foo'].toArray(), task.commandLine.toArray());
	}

}
