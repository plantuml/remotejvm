
# Distribute a Java application over several JVMs

This library provides a simple way for distributing Java applications over several nodes.

Each node must run a generic application-agnostic daemon (written itself in Java). This daemon is launched by:
```
java -cp ant.jar:remotejvm.jar com.remotejvm.Node
```

The JVMs do not share their memory but they can communicate and exchange data.

The Java application itself is automagically distributed to nodes.

You can [consult the Javadoc online](http://plantuml.github.io/remotejvm/index.html). There are only three public classes to do all the job.

## Usage

Here is a simple example

```java
public class Demo { 
    static public interface Worker extends Serializable { 
        // Just compute the sum of two integers 
        public Integer add(Integer a, Integer b); 
    } 
    static public class WorkerImpl implements Worker { 
        public Integer add(Integer a, Integer b) { 
            // This will be printed in the JVM that runs the method 
            System.out.println("Computing sum " + a + " and " + b); 
            return a + b; 
        } 
    } 
    public static void main(String[] args) throws RemoteException { 
        // This computation is done locally 
        Worker local = new WorkerImpl(); 
        System.out.println("local=" + local.add(3, 4)); 

        // The remote JVM runs on the local host 
        RemoteJVM remoteJVM = RemoteJVMUtils.create("127.0.0.1"); 

        // This computation is done on the remote JVM. 
        // So all arguments and result must be Serializable 
        Worker remote = (Worker) remoteJVM.transfert(local); 
        System.out.println("remote=" + remote.add(5, 6)); 
    } 
}
```

In the current example, the call to the remote ``Worker`` is done synchronously.
Using ``Future`` interface, we have ideas for providing asynchronous calls.

## Test yourself!

Just download both ``ant.jar`` and ``remotejvm.jar`` files. Depending on your system, download ``run_node.bat/.sh`` and ``run_demo.bat/.sh``

1. Put all files in the same directory.
2. Double-click or launch ``run_node`` : this will launch the daemon.
3. Double-click or launch ``run_demo`` in another window : this will launch the Java code listed below.

You will see that computations is done in both JVM, but that results are printed in the second window.

A folder named ``11001`` will be created : this is where classes are deserialized for the daemon (because in real world, ``run_node`` and ``run_demo`` should be launch on different machines.

By the way, you can edit ``run_demo`` file and change the IP address ``127.0.0.1`` to a real IP of yours, and make a test on two differents machines.

### Notes about ANT

ANT is only used to launch JVM : this is the best portable way to launch JVM from Java.


