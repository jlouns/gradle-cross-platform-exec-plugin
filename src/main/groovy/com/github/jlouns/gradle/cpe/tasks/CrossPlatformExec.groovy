package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem
import java.io.File

/**
 * Created on 1/26/15
 *
 * @author jlouns
 */
class CrossPlatformExec extends AbstractExecTask {
	private boolean windows;

	public CrossPlatformExec() {
		super(CrossPlatformExec.class);
		windows = OperatingSystem.current().windows;
	}

	@Override
	@TaskAction
	protected void exec() {
		List<String> commandLine = this.getCommandLine();
		commandLine.set(0, this.findScript(commandLine.get(0)));
		
		if(windows) {
			commandLine.add(0, '/c');
			commandLine.add(0, 'cmd');
		}

		this.setCommandLine(commandLine);
		super.exec();
	}

	private String findScript(String script) {
		def scriptFile = new File(script)
		if(scriptFile.isFile()) {
			return script;
		}
		
		if(windows) {
			def batScript = new File(script + ".bat");
			if(batScript.isFile()) {
				return batScript;
			}

			def cmdScript = new File(script + ".cmd");
			if(cmdScript.isFile()) {
				return cmdScript;
			}
		}
		else {
			def shScript = new File(script + ".sh")
			if(shScript.isFile()) {
				return shScript;
			}
		}


		return script;
	}

}
