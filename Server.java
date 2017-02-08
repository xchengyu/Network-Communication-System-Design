import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
	public static HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();
	public static Object lock = new Object();
	public static int tcp_port = 8888;
	public static HashMap<String, Integer> udp_port = new HashMap<String, Integer>();
	private static int clientCount = 4;
	private static ArrayList<ServerTCPThread> pool = new ArrayList<ServerTCPThread>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket socket_tcp = new ServerSocket(tcp_port);
			System.out.println("Server is working, Server ip is: " + 
			socket_tcp.getInetAddress().getLoopbackAddress().getHostAddress()
			+ ", static TCP port is: " + socket_tcp.getLocalPort());
			while (clientCount > 0) {
				Socket childSocket = socket_tcp.accept();
				ServerTCPThread child = new ServerTCPThread(childSocket);
				pool.add(child);
				child.start();
				clientCount--;
			}
			while (true) {
				int dead = 0;
				int size = pool.size();
				for (ServerTCPThread child : pool) {
					if (!child.isAlive()) {
						dead += 1;
					}
				}
				if (dead == size) {
					break;
				}
			}
			socket_tcp.close();
			for (String key : map.keySet()) {
				System.out.println("The neighbor information of Client " + key + " is:");
				HashMap<String, Integer> neighbors = map.get(key);
				for (String neighbor : neighbors.keySet()) {
					System.out.println(neighbor + " " + neighbors.get(neighbor));
				}
			}
/************************************TCP closed*****************************************************/
			String topologyInfo = topology(map);
			InetAddress address=InetAddress.getByName("localhost");
			byte[] data=topologyInfo.getBytes();
			DatagramSocket socket = null;
			for (String key : udp_port.keySet()) {
				int port = udp_port.get(key);
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				socket = new DatagramSocket();
				socket.send(packet);
				System.out.println("Server has sent topology info to Client " + key + ", remote static UDP port is: "
						+ port + ", Server ip is: " + socket.getLocalAddress().getLoopbackAddress().getHostAddress()
						+ ", Server dynamic UDP port is: " + socket.getLocalPort());
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String topology(HashMap<String, HashMap<String, Integer>> map) {
		String message = "";
		HashMap<String, Integer> edges = new HashMap<String, Integer>();
		for (String key : map.keySet()) {
			HashMap<String, Integer> neighbors = map.get(key);
			for (String neighbor : neighbors.keySet()) {
				 int distance = neighbors.get(neighbor);
				 String edge = key.compareTo(neighbor) < 0 ? key + neighbor : neighbor + key;
				 edges.put(edge, distance);
			}
		}
		for (String edge : edges.keySet()) {
			int distance = edges.get(edge);
			message += "#" + edge + " " + distance;
		}
		message = message.substring(1);
		return message;
	}
}
