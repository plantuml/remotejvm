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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

class JarAndClasses implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<PathData> pathData = new ArrayList<PathData>();

	public JarAndClasses() throws IOException {
		int cpt = 0;
		for (String s : Path.systemClasspath.list()) {
			final File file = new File(s);
			if (file.isDirectory()) {
				pathData.add(new PathDir(file, cpt++));
			} else if (file.getName().endsWith(".jar")) {
				pathData.add(new PathJarFile(file));
			}
		}
	}

	public void deployTo(File root) throws IOException {
		FileUtil.deltree(root);
		for (PathData path : pathData) {
			path.deployTo(root);
		}
	}

	public Path buildPath(Project project, String dir) {
		final Path result = new Path(project);
		for (PathData path : pathData) {
			path.appendTo(result, dir);
		}
		return result;
	}

}
