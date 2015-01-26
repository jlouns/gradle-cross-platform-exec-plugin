package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem

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
		if(windows) {
			List<String> commandLine = this.getCommandLine();
			commandLine.add(0, '/c');
			commandLine.add(0, 'cmd');

			this.setCommandLine(commandLine);
		}
		super.exec();
	}

}
