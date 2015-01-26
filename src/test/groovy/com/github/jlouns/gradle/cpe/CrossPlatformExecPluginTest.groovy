package com.github.jlouns.gradle.cpe

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 * Created on 1/26/15
 *
 * @author jlouns
 */
class CrossPlatformExecPluginTest extends GroovyTestCase {
	void testAddsCrossPlatformExecTask() {
		Project project = ProjectBuilder.builder().build()

		project.apply plugin: 'com.github.jlouns.cpe'

		assertTrue(project.extensions.extraProperties.has('CrossPlatformExec'))
	}
}
