package fileTrans;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.text.StyledDocument;

import client.Client;

public class TransfileRecive {
	private TransfileSocket cs = null;

	private String ip ;// 设置IP

	private int port = 8822;

	public TransfileRecive(String path,boolean flag) {
		try {
			if (createConnection()) {
				getMessage(path,flag);	
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 建立连接
	private boolean createConnection() throws UnknownHostException {
		
		ip=Client.client.friendIP;
		//System.out.println("ip地址："+ip);
		cs = new TransfileSocket(ip, port);
		try {
			cs.CreateConnection();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	// 接收消息
	private void getMessage(String savePath,Boolean flag) {
		if (cs == null)
			return;
		DataInputStream inputStream = null;
		try {
			inputStream = cs.getMessageStream();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			if(flag){
			// 本地保存路径，文件名会自动从服务器端继承而来。
			int bufferSize = 10240;
			byte[] buf = new byte[bufferSize];
			int passedlen = 0;
			long len = 0;

			savePath += "\\"+inputStream.readUTF();
			DataOutputStream fileOut = new DataOutputStream(
					new BufferedOutputStream(new BufferedOutputStream(
							new FileOutputStream(savePath))));
			len = inputStream.readLong();
			if(savePath.endsWith(".jpg")||savePath.endsWith(".bmp")||savePath.endsWith(".gif")){
				while (true) {
					int read = 0;
					if (inputStream != null) {
						read = inputStream.read(buf);
					}
					if (read == -1) {
						break;
					}
					fileOut.write(buf, 0, read);
				}
				fileOut.close();
				ImageIcon imageIcon=new ImageIcon(savePath);
				StyledDocument doc=client.ChatUI.dsiplChatArea.getStyledDocument();
				client.ChatUI.dsiplChatArea.setCaretPosition(doc.getLength());
				client.ChatUI.dsiplChatArea.insertIcon(imageIcon);
			}
			else{
			String msg = "\n文件的长度为:" + len + "\n";
			msg += "开始接收文件!" + "\n";
			client.Client.client.sendChatMsg(msg, client.ChatUI.souceid,
					client.ChatUI.destid);
			client.Client.client.sendChatMsg(msg, client.ChatUI.destid,
					client.ChatUI.souceid);
			while (true) {
				int read = 0;
				if (inputStream != null) {
					read = inputStream.read(buf);
				}
				passedlen += read;
				if (read == -1) {
					break;
				}
				// 打印文件传输百分比
				String string = "文件接收了" + (passedlen * 100 / len) + "%\n";
				client.Client.client.sendChatMsg(string,
						client.ChatUI.souceid, client.ChatUI.destid);
				client.Client.client.sendChatMsg(string,
						client.ChatUI.destid, client.ChatUI.souceid);
				fileOut.write(buf, 0, read);
			}
			String string = "接收完成,文件存为" + savePath + "\n";
			String string2 = "文件发送完成\n";
			client.Client.client.sendChatMsg(string2,
					client.ChatUI.souceid, client.ChatUI.destid);
			client.Client.client.sendChatMsg(string,
					client.ChatUI.destid, client.ChatUI.souceid);
			fileOut.close();
			}
			}
			else if(!flag){
				cs.shutDownConnection();
				String string = "已拒绝接收\n";
				String string2 = "对方拒绝接收\n";
				client.Client.client.sendChatMsg(string2,
						client.ChatUI.souceid, client.ChatUI.destid);
				client.Client.client.sendChatMsg(string,
						client.ChatUI.destid, client.ChatUI.souceid);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			String string = "文件接收失败" + "\n";
			client.Client.client.sendChatMsg(string,
					client.ChatUI.destid, client.ChatUI.souceid);
			client.Client.client.sendChatMsg(string,
					client.ChatUI.souceid, client.ChatUI.destid);
			return;
		}
	}
}
