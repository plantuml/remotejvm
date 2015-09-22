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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tools.ant.types.Path;

class PathDir implements Serializable, PathData {

	private static final long serialVersionUID = 1L;
	private final Map<String, byte[]> data = new TreeMap<String, byte[]>();

	private final String root;
	private final int cpt;

	public PathDir(File dir, int cpt) throws IOException {
		if (dir.isDirectory() == false) {
			throw new IllegalArgumentException();
		}
		this.cpt = cpt;
		this.root = dir.getPath();
		recurse(dir);
	}

	public void deployTo(File root) throws IOException {
		final File dest = new File(root, "" + cpt);
		dest.mkdirs();
		for (Map.Entry<String, byte[]> ent : data.entrySet()) {
			final File f = new File(dest, ent.getKey());
			f.delete();
			f.getParentFile().mkdirs();
			FileUtil.copyToFile(ent.getValue(), f);
		}
	}

	private void recurse(File dir) throws IOException {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				recurse(f);
			} else if (f.getName().endsWith(".class")) {
				addInternal(f.getPath().substring(root.length() + 1), f);
			}
		}

	}

	private void addInternal(String path, File file) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileUtil.copyToStream(file, baos);
		baos.close();
		data.put(path, baos.toByteArray());
	}

	public void appendTo(Path path, String dir) {
		final String tmp = dir + File.separator + cpt;
		path.add(new Path(path.getProject(), tmp));
	}

}
