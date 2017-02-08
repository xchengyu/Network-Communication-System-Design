# Network-Communication-System-Design
Network Communication System Design

1. Simulated TCP/UDP communication among network. The programming language is Java.

2. Created four clients and one server, each client sets up a TCP connection with server and creates a TCP socket to send
the information about the distance between itself and its neighbors to the server.

3. Used multi-thread to handle the TCP connection between server and client.

4. Built a graph of this tiny network based on the information provided by clients and calculated the minimum spanning
tree of this network by using Dijkstra's algorithm.

5. Sent the graph of this tiny network to every client through a UDP socket.
