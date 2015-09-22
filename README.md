
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

### Notes about ANT

ANT is only used to launch JVM : this is the best portable way to launch JVM from Java.


