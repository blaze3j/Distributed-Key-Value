package communication;

import java.net.*;

public class TCPReceive {
    private String mAddress = null;
    private int mPort = 0;
    private Socket mSocket = null;
    
    public TCPReceive(String address, int port) {
        mAddress = address;
        mPort = port;
        
        try {
            mSocket = new Socket(mAddress, mPort);
//            BufferedReader in = new BufferedReader(new
//                    InputStreamReader(skt.getInputStream()));
//            System.out.print("Received string: '");
//
//            while (!in.ready()) {}
//            System.out.println(in.readLine()); // Read one line and output it
//
//            System.out.print("'\n");
//            in.close();
        }
        catch(Exception e) {
            System.out.print("Error: Cannot create receiving socket.\n");
        }
    }
    
    public void close() {
        try {
            mSocket.close();
        }
        catch(Exception e) {
            System.out.print("Error: Cannot close\n");
        }
    }
}
