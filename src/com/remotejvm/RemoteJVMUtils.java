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
import java.rmi.NotBoundException;

/**
 * Factory to create instances of RemoteJVM.
 * 
 * <p>Before accessing to a RemoteJVM, you must start the Node class on the distant machine.
 * 
 * <p>Here is a typical example:
 * <p>
 * 

<div style='background:#FFFFFF; font-family: Courier New, Courier; font-size: 10pt; COLOR: #000000; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;'><strong><font color='#7f0055'>public</font></strong>&nbsp;<strong><font color='#7f0055'>class</font></strong>&nbsp;Demo&nbsp;{
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>static</font></strong>&nbsp;<strong><font color='#7f0055'>public</font></strong>&nbsp;<strong><font color='#7f0055'>interface</font></strong>&nbsp;Worker&nbsp;<strong><font color='#7f0055'>extends</font></strong>&nbsp;Serializable&nbsp;{
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;Just&nbsp;compute&nbsp;the&nbsp;sum&nbsp;of&nbsp;two&nbsp;integers
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>public</font></strong>&nbsp;Integer&nbsp;add(Integer&nbsp;a,&nbsp;Integer&nbsp;b);
<br/>&nbsp;&nbsp;&nbsp;&nbsp;}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>static</font></strong>&nbsp;<strong><font color='#7f0055'>public</font></strong>&nbsp;<strong><font color='#7f0055'>class</font></strong>&nbsp;WorkerImpl&nbsp;<strong><font color='#7f0055'>implements</font></strong>&nbsp;Worker&nbsp;{
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>public</font></strong>&nbsp;Integer&nbsp;add(Integer&nbsp;a,&nbsp;Integer&nbsp;b)&nbsp;{
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;This&nbsp;will&nbsp;be&nbsp;printed&nbsp;in&nbsp;the&nbsp;JVM&nbsp;that&nbsp;runs&nbsp;the&nbsp;method
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<font color='#2a00ff'>"Computing&nbsp;sum&nbsp;"</font>&nbsp;+&nbsp;a&nbsp;+&nbsp;<font color='#2a00ff'>"&nbsp;and&nbsp;"</font>&nbsp;+&nbsp;b);
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>return</font></strong>&nbsp;a&nbsp;+&nbsp;b;
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><font color='#7f0055'>public</font></strong>&nbsp;<strong><font color='#7f0055'>static</font></strong>&nbsp;<strong><font color='#7f0055'>void</font></strong>&nbsp;main(String[]&nbsp;args)&nbsp;<strong><font color='#7f0055'>throws</font></strong>&nbsp;RemoteException&nbsp;{
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;This&nbsp;computation&nbsp;is&nbsp;done&nbsp;locally
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Worker&nbsp;local&nbsp;=&nbsp;<strong><font color='#7f0055'>new</font></strong>&nbsp;WorkerImpl();
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<font color='#2a00ff'>"local="</font>&nbsp;+&nbsp;local.add(3,&nbsp;4));
<br/>
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;The&nbsp;remote&nbsp;JVM&nbsp;runs&nbsp;on&nbsp;the&nbsp;local&nbsp;host
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RemoteJVM&nbsp;remoteJVM&nbsp;=&nbsp;RemoteJVMUtils.create(<font color='#2a00ff'>"127.0.0.1"</font>);
<br/>
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;This&nbsp;computation&nbsp;is&nbsp;done&nbsp;on&nbsp;the&nbsp;remote&nbsp;JVM.
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#3f7f5f'>//&nbsp;So&nbsp;all&nbsp;arguments&nbsp;and&nbsp;result&nbsp;must&nbsp;be&nbsp;Serializable
</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Worker&nbsp;remote&nbsp;=&nbsp;(Worker)&nbsp;remoteJVM.transfert(local);
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<font color='#2a00ff'>"remote="</font>&nbsp;+&nbsp;remote.add(5,&nbsp;6));
<br/>&nbsp;&nbsp;&nbsp;&nbsp;}
<br/>}<div style='text-align:right;BORDER-TOP: #ccc 1px dashed'><a href="http://www.togotutor.com/code-to-html/java-to-html.php" target="_blank">Code Formatted by ToGoTutor</a></div></div>

 * 
 * <p>Before running this example, you have to start the node with the following command:
 * <p>
 * <code>java -cp ant.jar:remotejvm.jar com.remotejvm.Node</code>
 * <p>
 * <p>In the current example, since <code>127.0.0.1</code> is used as IP, you have to start the node
 * on the local machine itself.
 * 
 * 
 * @see RemoteJVM
 * @see Node
 *
 */
final public class RemoteJVMUtils {

	private RemoteJVMUtils() {

	}

	/**
	 * Retrieve the instance of a RemoteJVM.
	 * 
	 * @param ip		A String indicating the IP adress of the distant host
	 * 					(example: 192.168.1.12)
	 * 
	 * @return			An interface giving acces to the remote JVM
	 * 
	 */
	public static RemoteJVM create(String ip) {
		try {
			return new RemoteJVMImpl(ip);
		} catch (NotBoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
