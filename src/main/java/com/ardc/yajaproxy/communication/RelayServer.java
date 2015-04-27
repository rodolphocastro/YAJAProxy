package com.ardc.yajaproxy.communication;

import com.ardc.yajaproxy.communication.interfaces.RelayServerInterface;
import com.ardc.yajaproxy.utils.FilePrinter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class implementing a Relay Server which function is to relay messages from the Client to the Remote server.
 * @author ALVES, R.C.
 */
public class RelayServer implements RelayServerInterface{

    /**
     * InputStream from the Client.
     */
    private InputStream streamFromClient = null;
    
    /**
     * OutputStream to the remote server.
     */
    private OutputStream streamToServer = null;
    
    /**
     * The request itself.
     */
    private byte[] request = new byte[1024];
    
    /**
     * Flag for verbose mode.
     */
    private boolean verbose;
    
    /**
     * The Client's IP Address.
     */
    private String clientAddress;
    
    @Override
    public void run() {
        int bytesRead;
        //Relaying the request
        try{
            while((bytesRead = streamFromClient.read(request)) != -1){
                if(verbose){
                    System.out.format("[%s]: %s\n", "Relay-Server", new String(request));
                }
                streamToServer.write(request, 0, bytesRead);
                FilePrinter.printToFile(String.format("%s@%s", "Client", clientAddress), new String(request));
                streamToServer.flush();
            }
        }catch(IOException err){
            System.err.format("[%s]: %s\n", "Relay-Server", err.getMessage());
        }
        
        //Client closed the connection to us
        //Closing connection to the remote server
        try{
            streamToServer.close();
        }catch(IOException err){
            System.err.format("[%s]: %s\n", "Relay-Server", err.getMessage());
        }
    }
    
    //Getters and Setters generated by the IDE

    public InputStream getStreamFromClient() {
        return streamFromClient;
    }

    public void setStreamFromClient(InputStream streamFromClient) {
        this.streamFromClient = streamFromClient;
    }

    public OutputStream getStreamToServer() {
        return streamToServer;
    }

    public void setStreamToServer(OutputStream streamToServer) {
        this.streamToServer = streamToServer;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public byte[] getRequest() {
        return request;
    }

    public void setRequest(byte[] request) {
        this.request = request;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }
    
    
}
