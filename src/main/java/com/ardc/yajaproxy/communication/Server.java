package com.ardc.yajaproxy.communication;

import com.ardc.yajaproxy.communication.interfaces.ServerInterface;
import com.ardc.yajaproxy.utils.FilePrinter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server implementation, responsible for handling connections.
 * @author ALVES, R.C.
 */
public class Server implements ServerInterface{

    /**
     * The local port to which the server should listen.
     */
    private final int localPort;
    
    /**
     * The remote port to which the server will send data thru.
     */
    private final int remotePort;
    
    /**
     * The host.
     */
    private final String host;
    
    /**
     * Verbose flag.s
     */
    private final boolean verbose;

    /**
     * Reference to a FilePrinter.
     */
    private FilePrinter filePrinter = FilePrinter.getFprinter();
    
    /**
     * The socket on which the server will listen for data.
     */
    private ServerSocket serverSocket = null;
    
    /**
     * The socket the server will use to send data to the client.
     */
    private Socket clientSocket = null;
    
    /**
     * The socket the server will use to send data to the original destination.
     */
    private Socket remoteSocket = null;
    
    /**
     * Flag for safely stopping the server.
     */
    private boolean run = false;
    
    /**
     * Constructor for a Server with disabled verbose mode.
     * @param localPort The local port.
     * @param remotePort The remote port.
     * @param host The host.
     */
    public Server(int localPort, int remotePort, String host) {
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.host = host;
        this.verbose = false;
        startup();
    }

    /**
     * Constructor for a Server with enabled verbose mode.
     * @param localPort The local port.
     * @param remotePort The remote port.
     * @param host The host.
     * @param verbose If the server should or shouldn't be verbose.
     */
    public Server(int localPort, int remotePort, String host, boolean verbose) {
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.host = host;
        this.verbose = verbose;
        startup();
    }

    /**
     * Method for starting up the server's listening socket.
     */
    private void startup(){
        try{
            serverSocket = new ServerSocket(localPort);
            if(verbose){
                System.out.format("[%s]: %s %d\n", "Server", "Created a server socket on port:", localPort);
            }
        }catch(IOException er){
            System.err.format("[%s]: %s\n", "Server", er.getMessage());
        }
    }
    
    @Override
    public void run() {
        //Printing the Server's information to the Console.
        System.out.format("[%s]: %s", "Server", 
                String.format("Starting up server...\n"
                        + "[Server]: Listening on port: %d.\n"
                        + "[Server]: Remote port: %d.\n"
                        , localPort, remotePort));
        
        //Setting up IO buffers
        final byte[] request = new byte[1024];
        byte[] reply = new byte[4096];
        
        //While the server is runnning...
        while(run){
            try{
                clientSocket = serverSocket.accept();       //Accepting an incoming connection from the client.
                if(verbose){
                    System.out.format("[%s]: %s %d.\n", "Server", "Incoming connection from client at port", localPort);
                }
                
                //Setting up IO Streams
                final InputStream streamFromClient = clientSocket.getInputStream();
                final OutputStream streamToClient = clientSocket.getOutputStream();
                
                //Attempting to connect with the real server.
                try{
                    //Setting up a Socket for communicating with the remote host
                    remoteSocket = new Socket(host, remotePort);
                    if(verbose){
                        System.out.format("[%s]: %s %s:%d.\n", "Server", "New Remote Socket", host, remotePort);
                    }
                }catch(IOException er){
                    //Connection failed, sending a message to the client...
                    System.err.format("[%s]: %s\n", "Server", er.getMessage());
                    PrintWriter clientOut = new PrintWriter(streamToClient);
                    clientOut.format("[%s]: %s %s:%d.\n", "Server", "Failed to connect to", host, remotePort);
                    clientOut.flush();
                    clientSocket.close();
                }
                //Real Server shoulld be connected to, time to proxy the messages...
                
                //Getting the Remote Server's streams
                final InputStream streamFromServer = remoteSocket.getInputStream();
                final OutputStream streamToServer = remoteSocket.getOutputStream();
                
                //Creating a Relay Server to pass the data from the client to the server
                RelayServer relay = new RelayServer();
                //Setting its proprieties
                relay.setRequest(request);
                relay.setStreamFromClient(streamFromClient);
                relay.setStreamToServer(streamToServer);
                relay.setVerbose(verbose);
                //Creating a new thread to run it, for asynchrony
                Thread relayThread = new Thread(relay);
                //Starting the RelayServer
                relayThread.start();
                
                int bytesRead;
                try{
                    while((bytesRead = streamFromServer.read(reply)) != -1){
                        if(verbose){
                            System.out.format("[%s]: %s\n", "Server", new String(request));
                        }
                        streamToClient.write(reply, 0, bytesRead);
                        FilePrinter.printToFile(String.format("%s@%s", "Server", remoteSocket.getInetAddress()), new String(reply));
                        streamToClient.flush();
                    }
                }catch(IOException er){
                    System.err.format("[%s]: %s\n", "Server", er.getMessage());
                }
                
                //The remote server closed its connection to us
                //Closing connection to the client
                streamToClient.close();
                FilePrinter.finishWriting();
            }catch(IOException er){
                System.err.format("[%s]: %s\n", "Server", er.getMessage());
            }finally{
                try{
                    if(remoteSocket != null){
                        remoteSocket.close();
                    }
                    if(clientSocket != null){
                        clientSocket.close();
                    }
                } catch (IOException ex) {
                    System.err.format("[%s]: %s\n", "Server", ex.getMessage());
                }
            }
            
        }
    }

    public FilePrinter getFilePrinter() {
        return filePrinter;
    }

    public void setFilePrinter(FilePrinter filePrinter) {
        this.filePrinter = filePrinter;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
    
    
}
