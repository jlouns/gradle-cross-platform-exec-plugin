package com.github.jlouns.gradle.cpe.tasks

import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class CrossPlatformExec extends AbstractExecTask {
	private static final def windowsExtensions = ['bat', 'cmd', 'exe'];
	private static final def unixExtensions = [null, 'sh'];

	private boolean windows;

	public CrossPlatformExec() {
		super(CrossPlatformExec.class);
		windows = OperatingSystem.current().windows;
	}

	@Override
	@TaskAction
	protected void exec() {
		List<String> commandLine = this.getCommandLine();

		if (!commandLine.isEmpty()) {
			commandLine[0] = this.findCommand(commandLine[0]);
		}

		if (windows) {
			commandLine.add(0, '/c');
			commandLine.add(0, 'cmd');
		}

		this.setCommandLine(commandLine);

		super.exec();
	}

	private String findCommand(String command) {
		if (windows) {
			return windowsExtensions.findResult(command) { extension ->
				String commandFile = command + '.' + extension;

				if (Files.isExecutable(Paths.get(commandFile))) {
					return commandFile;
				}

				return null;
			};
		} else {
			return unixExtensions.findResult(command) { extension ->
				Path commandFile
				if (extension) {
					commandFile = Paths.get(command + '.' + extension);
				} else {
					commandFile = Paths.get(command);
				}

				if (Files.isExecutable(commandFile)) {
					Path cwd = Paths.get('.').toAbsolutePath().normalize();
					String resolvedCommand = cwd.relativize(commandFile.toAbsolutePath().normalize());

					if (!resolvedCommand.startsWith('.')) {
						resolvedCommand = './' + resolvedCommand;
					}

					return resolvedCommand;
				}

				return null;
			};
		}
	}

}
