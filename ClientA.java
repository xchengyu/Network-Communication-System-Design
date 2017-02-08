import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientA {
	public static int udp_port = 32520;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("D:\\USC\\USC_Textbook\\ee450\\450_project_java_version\\serverA.txt");
		BufferedReader fbr = null;
		String neighborInfo = "";
		try {
			fbr = new BufferedReader(new FileReader(file));
			String tempString = null;
            int line = 1;
            try {
				while ((tempString = fbr.readLine()) != null) {
				    System.out.println("line " + line + ": " + tempString);
				    neighborInfo += "#" +tempString;
				    line++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (fbr != null) {
					fbr.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Socket socket_tcp = new Socket("localhost", Server.tcp_port);
			OutputStream os = socket_tcp.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("I' m Client A, my local ip is: " + 
					socket_tcp.getLocalAddress().getHostAddress()
					+ ", my local TCP port is: " + socket_tcp.getLocalPort()
					+ ", my local UDP port is: " + udp_port
					+ ", my neighbor is: " + neighborInfo);
			pw.flush();
			socket_tcp.shutdownOutput();
			os.close();
			pw.close();
			socket_tcp.close();
/*****************************TCP closed**********************************************/
			DatagramSocket socket_udp=new DatagramSocket(udp_port);
			byte[] data =new byte[1024];
			DatagramPacket packet=new DatagramPacket(data, data.length);
			System.out.println("****Client A UDP socket is listenning·****");
			socket_udp.receive(packet);
			String info=new String(data, 0, packet.getLength());
			System.out.println("Client A has received topology info from Server: "+info
					+ ". Server dynamic port is: " + packet.getPort() + ".");
			socket_udp.close();
/*****************************UDP closed**********************************************/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
