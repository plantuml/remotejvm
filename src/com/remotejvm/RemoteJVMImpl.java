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
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

class RemoteJVMImpl implements RemoteJVM {

	private final MethodCaller methodCaller;
	private final int port;

	public RemoteJVMImpl(String ip) throws NotBoundException, IOException {
		final GateMaster gateMaster = (GateMaster) LocateRegistry.getRegistry(ip, GateMaster.GATE_MASTER_PORT).lookup(
				"GateMaster");
		this.port = gateMaster.deployClassesAndLaunchAndGetPort(new JarAndClasses());
		this.methodCaller = (MethodCaller) LocateRegistry.getRegistry(ip, port).lookup("MethodCaller");
	}

	public Serializable transfert(Serializable object) throws RemoteException {
		final UUID uuid = methodCaller.addObject(object);

		for (Class interf : object.getClass().getInterfaces()) {
			final boolean isSerializable = Serializable.class.isAssignableFrom(interf);
			if (isSerializable) {
				final InvocationHandler handler = new RemoteInvocationHandler(uuid, methodCaller);
				return (Serializable) Proxy.newProxyInstance(object.getClass().getClassLoader(),
						new Class[] { interf }, handler);
			}
		}

		throw new IllegalArgumentException();

	}

}
