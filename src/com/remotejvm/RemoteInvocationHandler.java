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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

class RemoteInvocationHandler implements InvocationHandler {

	private final MethodCaller methodCaller;
	private final UUID uuid;

	public RemoteInvocationHandler(UUID uuid, MethodCaller methodCaller) {
		this.uuid = uuid;
		this.methodCaller = methodCaller;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final String signature = getMethodSignature(method);
		final MethodResult result = methodCaller.call(uuid, signature, args);
		if (result.getException() != null) {
			throw result.getException();
		}
		return result.getResult();
	}

	public static String getMethodSignature(Method m) {
		final StringBuilder sb = new StringBuilder();
		sb.append(m.getReturnType());
		sb.append("-");
		sb.append(m.getName());
		sb.append("-");
		for (Class param : m.getParameterTypes()) {
			sb.append(param.getName());
			sb.append("-");
		}
		return sb.toString();
	}

}
