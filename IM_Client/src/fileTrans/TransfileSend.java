package fileTrans;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.Client;

public class TransfileSend {
	int port = 8822;
	public void start(String filePath, String fileName) {
		
		Socket s = null;
		String msg = "\n用户" + String.valueOf(client.ChatUI.souceid) + " "
				+ Client.getTime() + "\n";
		msg += "收到来自用户" + client.ChatUI.souceid + "的文件(" + fileName
				+ ")请求！\n";
		Client.client.sendChatMsg(msg, client.ChatUI.souceid,
				client.ChatUI.destid);
		try {
			ServerSocket ss = new ServerSocket(port);
				// 选择进行传输的文件
				File fi = new File(filePath);
				s = ss.accept();
				DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
				DataOutputStream ps = new DataOutputStream(s.getOutputStream());
				// 将文件名及长度传给接收端。
				ps.writeUTF(fi.getName());
				ps.flush();
				ps.writeLong((long) fi.length());
				ps.flush();
				int bufferSize = 10240;
				byte[] buf = new byte[bufferSize];
				while (true) {
					int read = 0;
					if (fis != null) {
						read = fis.read(buf);
					}
					if (read == -1) {
						break;
					}
					ps.write(buf, 0, read);
				}
				ps.flush();
				fis.close();
				s.close();
				ss.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
