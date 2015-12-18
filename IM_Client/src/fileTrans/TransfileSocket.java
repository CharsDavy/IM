package fileTrans;

import java.net.*;
import java.io.*;

 public class TransfileSocket {
    private String ip;

    private int port;

    private Socket socket = null;

    DataOutputStream out = null;

    DataInputStream getMessageStream = null;

     public TransfileSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

     /**
     * 创建socket连接
     * 
     * @throws Exception
     *             exception
     */
     public void CreateConnection() throws Exception {
         try {
            socket = new Socket(ip, port);
         } catch (Exception e) {
            e.printStackTrace();
            if (socket != null)
                socket.close();
            throw e;
         } finally {
        }
    }
     public DataInputStream getMessageStream() throws Exception {
         try {
            getMessageStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            return getMessageStream;
         } catch (Exception e) {
            e.printStackTrace();
            if (getMessageStream != null)
                getMessageStream.close();
            throw e;
         } finally {
        }
    }

     public void shutDownConnection() {
         try {
            if (out != null)
                out.close();
            if (getMessageStream != null)
                getMessageStream.close();
            if (socket != null)
                socket.close();
         } catch (Exception e) {

        }
    }
}
