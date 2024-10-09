## **Message Queues Specification**

### 1. **Overview**

The **Message Queues** framework provides an abstraction for **message-based communication** between tasks. This framework enables **variable-sized messages** to be sent and received as whole units. The framework consists of the following core abstractions:

- **QueueBroker**: Manages connections between tasks by establishing message queues between them.
- **MessageQueue**: Represents a queue that facilitates sending and receiving of messages (as a whole) between tasks.
- **Task**: An abstract class representing a unit of execution (thread) that communicates through message queues.

---

### 2. **Connecting**

Tasks communicate with each other through **MessageQueues**. To establish a connection, each task uses a **QueueBroker**. Tasks can either **accept** incoming connections or **connect** to an existing broker on a specific port.

#### a) **Accepting a Connection**
A task, for exemple let's say a server, can listen for incoming connections from other tasks (clients) on a specific port. This is done using the `accept(int port)` method of **QueueBroker**, which creates a **MessageQueue** for each connection.



#### b) **Establishing a Connection**
A task can initiate a connection to a **QueueBroker** on another task by calling the `connect(String name, int port)` method. This establishes a **MessageQueue** with the specified task.



### 3. **Sending Messages**

In the **MessageQueue**, messages are sent as units, unlike raw byte streams. Each **MessageQueue** provides a `send(byte[] bytes, int offset, int length)` method that allows the sender to send a message in parts, if necessary, while ensuring the message is delivered as a whole to the receiver.

#### - **Chunking Large Messages**
If the message size exceeds the buffer capacity (e.g., 64 bytes), the **MessageQueue** automatically handles **message fragmentation**. The message is broken into chunks and sent in multiple parts.

---

### 4. **Receiving Messages**

The **receive()** method in **MessageQueue** enables tasks to receive a complete message. It handles the reconstruction of fragmented messages, ensuring the receiving task gets the entire payload, even if it was sent in parts.

#### - **Message Reconstruction**
If a message was fragmented during transmission, the **receive()** method collects and reconstructs it before returning the final payload.

---

### 5. **Closing a Message Queue**

Each **MessageQueue** provides a `close()` method to properly close the connection between tasks. The `closed()` method allows checking the status of the message queue. The **MessageQueue** ensures that when a connection is closed, any ongoing communication is completed, and both sides are notified of the disconnection.
