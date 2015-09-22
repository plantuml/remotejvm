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

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

class MyOutputStream extends OutputStream {

	private final GateMaster gateMaster;
	private final String name;

	public MyOutputStream(String name) throws AccessException, RemoteException, NotBoundException {
		this.name = name;
		this.gateMaster = (GateMaster) LocateRegistry.getRegistry("127.0.0.1", GateMaster.GATE_MASTER_PORT).lookup(
				"GateMaster");
	}

	public void write(byte[] b, int off, int len) throws IOException {
		final String s = new String(b, off, len);
		if (s.matches("\\s*")) {
			return;
		}
		gateMaster.remotePrintln(decorate(s));
	}

	private String decorate(String s) {
		return "(" + name + ") " + s;
	}

	@Override
	public void write(int b) throws IOException {
		gateMaster.remotePrintln(decorate("writeByte " + b));
	}

}
