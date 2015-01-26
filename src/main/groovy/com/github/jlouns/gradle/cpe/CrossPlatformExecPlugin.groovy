package com.github.jlouns.gradle.cpe

import com.github.jlouns.gradle.cpe.tasks.CrossPlatformExec
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created on 1/26/15
 *
 * @author jlouns
 */
class CrossPlatformExecPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		addTask(project, CrossPlatformExec)
	}

	static void addTask(Project project, Class type) {
		project.extensions.extraProperties.set(type.getSimpleName(), type)
	}

}
