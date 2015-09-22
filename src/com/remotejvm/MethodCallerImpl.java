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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class MethodCallerImpl implements MethodCaller {

	private final ConcurrentMap<UUID, Serializable> data = new ConcurrentHashMap<UUID, Serializable>();

	public MethodResult call(UUID me, String method, Object[] args) throws RemoteException {
		final Serializable object = data.get(me);
		if (object == null) {
			throw new RemoteException("No " + me + " Object");
		}
		try {
			for (Method m : object.getClass().getMethods()) {
				if (RemoteInvocationHandler.getMethodSignature(m).equals(method)) {
					final Serializable result = (Serializable) m.invoke(object, args);
					return MethodResult.ok(result);
				}
			}
			throw new NoSuchMethodException();
		} catch (Exception e) {
			e.printStackTrace();
			return MethodResult.error(e);
		}

	}

	public UUID addObject(Serializable me) {
		final UUID uuid = UUID.randomUUID();
		data.put(uuid, me);
		return uuid;
	}

}
