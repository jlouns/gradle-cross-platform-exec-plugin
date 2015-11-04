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
		command = normalizeCommandPaths(command);
		if (windows) {
			return windowsExtensions.findResult(command) { extension ->
				Path commandFile = Paths.get(command + '.' + extension);

				return CrossPlatformExec.resolveCommandFromFile(commandFile);
			};
		} else {
			return unixExtensions.findResult(command) { extension ->
				Path commandFile
				if (extension) {
					commandFile = Paths.get(command + '.' + extension);
				} else {
					commandFile = Paths.get(command);
				}

				return CrossPlatformExec.resolveCommandFromFile(commandFile);
			};
		}
	}

	private static String resolveCommandFromFile(Path commandFile) {
		if (!Files.isExecutable(commandFile)) {
			return null;
		}

		Path cwd = Paths.get('.').toAbsolutePath().normalize();

		String resolvedCommand = cwd.relativize(commandFile.toAbsolutePath().normalize());

		if (!resolvedCommand.startsWith('.')) {
			resolvedCommand = '.' + File.separator + resolvedCommand;
		}

		return resolvedCommand;
	}

	private static String normalizeCommandPaths(String command) {
		return command.replaceAll('\\\\', '/').replaceAll('/', File.separator);
	}
}
