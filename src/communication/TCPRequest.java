package communication;

import java.net.*;

public class TCPRequest {
    private int mPort = 0;
    private ServerSocket mServerSocket = null;
    private Socket mSocket = null;
    
    public TCPRequest(int port) {
        super();

        mPort = port;
        
        try {
            mServerSocket = new ServerSocket(mPort);
            mSocket = mServerSocket.accept();
            System.out.print("Connected!\n");
        }
        catch(Exception e) {
            System.out.print("Error: cannot create socket.\n");
        }
    }
    
    public void close() {
        try {
        mSocket.close();
        mServerSocket.close();
        }
        catch(Exception e) {
            System.out.print("Error: cannot close socket.\n");
        }
    }
    
    public void send() {
        // PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
        // System.out.print("Sending string: '" + data + "'\n");
        // out.print(data);
        // out.close();
    }
}