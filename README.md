# Network-Communication-System-Design
Network Communication System Design
 Simulated TCP/UDP communication among network. The programming language is Java.
 Created four clients and one server, each client sets up a TCP connection with server and creates a TCP socket to send
the information about the distance between itself and its neighbors to the server.
 Used multi-thread to handle the TCP connection between server and client.
 Built a graph of this tiny network based on the information provided by clients and calculated the minimum spanning
tree of this network by using Dijkstra's algorithm.
 Sent the graph of this tiny network to every client through a UDP socket.
