import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ServerTCPThread extends Thread {
	private Socket socket = null;
	private InputStream is = null;
	private InputStreamReader isr = null;
	private BufferedReader br = null;
	public ServerTCPThread(Socket childSocket) {
		// TODO Auto-generated constructor stub
		this.socket = childSocket;
	}
	
	public void run() {
		InetAddress remote = socket.getInetAddress();
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String read = null;
			String info = "";
			while ((read = br.readLine()) != null) {
				info += read;
			}
			int index = info.indexOf('#');
			int clientNameIndex = info.indexOf("Client");
			String clientName = info.substring(clientNameIndex + 7, clientNameIndex + 8);
//System.out.println("test" +clientName);//test
			System.out.println("receive info from Client " + clientName + ", remote ip is: " + 
					remote.getHostAddress() + ", remote TCP port is: " + socket.getPort());
						System.out.println("Info is: " + info);
			String remoteNeighborInfo = "";
			if (index != -1) {
				remoteNeighborInfo = info.substring(index + 1);
			}
			String[] remoteNeighbor = remoteNeighborInfo.split("#");
			synchronized(Server.lock) {
				Server.map.put(clientName, new HashMap<String, Integer>());
				HashMap<String, Integer> neighbors = Server.map.get(clientName);
				for (int i = 0; i < remoteNeighbor.length; i++) {
					String[] res = remoteNeighbor[i].split(" ");
					int indexName = res[0].indexOf("server");
					String key = res[0].substring(indexName + "server".length(), 
							indexName + 1 + "server".length());
					Integer value = Integer.parseInt(res[1]);
					neighbors.put(key, value);
					
//System.out.println("test" +remoteNeighbor[i]);//test
				}
				int indexUDP = info.indexOf("UDP port is: ");
				String[] udp = info.substring(indexUDP + "UDP port is: ".length()).split(",");
				int udpPort = Integer.parseInt(udp[0]);
//System.out.println(udpPort);//test
				Server.udp_port.put(clientName, udpPort);
			}
			socket.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
				if (socket!=null) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
