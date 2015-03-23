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
	private File shScript;
	private File batScript;
	private File cmdScript;

	@Override
	void setUp() {
		project = ProjectBuilder.builder().build()

		project.apply plugin: 'com.github.jlouns.cpe'
		
		shScript = new File("some_script.sh")
		batScript = new File("some_script.bat")
		cmdScript = new File("some_script2.cmd")
		
		shScript.createNewFile()
		batScript.createNewFile()
		cmdScript.createNewFile()
	}

	@Override
	void tearDown() {
		shScript.delete()
		batScript.delete()
		cmdScript.delete()
	}

	Task createTask(String executable='echo') {
		Task task = project.task([ 'type': CrossPlatformExec ], 'testCpe') {
			commandLine executable, 'foo'
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

	void testForAddingShScriptExtensions() {
		System.setProperty("os.name", "linux")
		
		Task task = createTask('some_script')
		
		assertArrayEquals(['some_script.sh', 'foo'].toArray(), task.commandLine.toArray());	
	}

	void testForAddingBatScriptExtensions() {
		System.setProperty("os.name", "windows")
		
		Task task = createTask('some_script')
		
		assertArrayEquals(['cmd', '/c', 'some_script.bat', 'foo'].toArray(), task.commandLine.toArray());	
	}

	void testForAddingCmdScriptExtensions() {
		System.setProperty("os.name", "windows")
		
		Task task = createTask('some_script2')
		
		assertArrayEquals(['cmd', '/c', 'some_script2.cmd', 'foo'].toArray(), task.commandLine.toArray());	
	}

}
