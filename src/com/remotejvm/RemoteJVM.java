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
import java.rmi.RemoteException;

/**
 * A RemoteJVM represents an instance of another JVM on a distant node.
 * 
 * 
 */
public interface RemoteJVM {

	/**
	 * 
	 * Transfert a local object to the remote JVM.
	 * 
	 * The object is transfered through serialization.
	 * <p>This methods returns a proxy instance that allow you to launch from
	 * your local JVM methods on the remote object. So methods through this proxy
	 * are executed on the remote JVM.
	 * 
	 * <p>The result is also retrieved through serialization.
	 * 
	 * @param object	a local object to be sent to the remote JVM
	 * 
	 * @return			a proxy that allows to call methods to the remote object.
	 * 
	 * @throws RemoteException
	 */
	public Serializable transfert(Serializable object) throws RemoteException;

}
