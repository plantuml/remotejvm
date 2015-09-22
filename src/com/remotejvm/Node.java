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

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Node to run on distant server to be able to create RemoteJVM on them.
 *
 * <p>Node listens to port 11000 for incoming connection.
 * 
 */
public class Node {
	
	private Node() {
		
	}

	/**
	 * Main class: instanciate and run the node.
	 * 
	 * @param argv		no arguments are needed
	 * 
	 * @throws RemoteException
	 */
	public static void main(String[] argv) throws RemoteException {

		final Registry registry = LocateRegistry.createRegistry(GateMaster.GATE_MASTER_PORT);
		final GateMaster master = (GateMaster) UnicastRemoteObject.exportObject(new GateMasterImpl(),
				GateMaster.GATE_MASTER_PORT);
		registry.rebind("GateMaster", master);
		System.err.println("Node ok!");

	}
}
