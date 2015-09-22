/* ========================================================================
 * RemoteJVM : Simply distribute Java applications over several nodes
 * ========================================================================
 *
 * (C) Copyright 2015-2016, Arnaud Roques
 *
 * Project Info:  https://github.com/plantuml/cloudjvm
 * 
 * This file is part of RemoteJVM.
 *
 * RemoteJVM is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RemoteJVM distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * Original Author:  Arnaud Roques
 *
 */
package com.remotejvm;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

class MyJVM {

	private final File root;
	private final int port;
	private final JarAndClasses jarAndClasses;

	public MyJVM(File root, int port, JarAndClasses jarAndClasses) {
		this.root = root;
		this.port = port;
		this.jarAndClasses = jarAndClasses;

	}

	public void start() {

		final Thread thread = new Thread() {
			@Override
			public void run() {
				final Project project = new Project();

				final Java javaTask = new Java();
				// javaTask.setTimeout(Long.MAX_VALUE);
				// javaTask.setTaskName("runjava");
				javaTask.setProject(project);
				javaTask.setFork(true);
				javaTask.setFailonerror(true);
				javaTask.setDir(root);
				javaTask.setClassname(NodeWorker.class.getName());
				javaTask.setArgs("" + port);
				Path path = jarAndClasses.buildPath(project, root.getName());
				javaTask.setClasspath(path);
				javaTask.init();
				System.out.println("Starting worker " + port);
				final int ret = javaTask.executeJava();
				System.out.println("Worker end: " + port + " ret=" + ret);
			}
		};
		thread.start();
	}
}
