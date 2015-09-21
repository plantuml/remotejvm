
# Distribute a Java application over several JVMs

This library will provide a simple way for distributing Java applications over several nodes.

Each node must run a generic application-agnostic daemon (written itself in Java).
The JVMs do not share their memory but they can communicate and exchange data.

The Java application itself is automatically distributed to nodes.

## Usage

Here is a simple example

```java
// The interface of objects to be distributed 
public interface Worker extends Serializable {
	// Just compute the sum of two integers
    public int add(int a, int b);
}

// A basic implementation
public class WorkerImpl implements Worker {
    public int add(int a, int b) { return a + b; }
}

// This computation is done locally
Worker local = new WorkerImpl();
System.out.println("local=" + local.add(3, 4));

// Create and deploy a new JVM to some host.
// This new JVM will have access to the very same classes/jar than the current JVM.
// This is done by serializing all .class/.jar files in the classpath of this JVM to the remote
// host before launching the new remote JVM
CloudJVM otherJVM = CloudJVM.create("192.168.1.12");

// Create a proxy access to some remote object.
// The object is sent to the remote JVM thanks to serialization of the "local" object.
// So both instance will be different.
// We will use "java dynamic proxy" to build this proxy instance.
// The proxy instance will use RMI and serialization to send request to the remote host.
Worker remote = (Worker) otherJVM.transfert(local);

// This computation is done on the remote JVM.
// So all arguments and result must be Serializable
System.out.println("remote=" + remote.add(3, 4));

// Stop the remote JVM
otherJVM.stop();

```

In the current example, the call to the remote ``Worker`` is done synchronously.
Using ``Future`` interface, we have ideas for providing asynchronous calls.

Questions:

1. Does it sound usefull to you ?

2. Does something like this already exist ?

