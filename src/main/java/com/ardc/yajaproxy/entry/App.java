package com.ardc.yajaproxy.entry;

import com.ardc.yajaproxy.communication.Server;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

/**
 * Entry class for the software, responsible for calling all the other needed classes.
 * @author ALVES, R.C.
 */
public class App {
    
    /**
     * The options for this software.
     */
    private static Options opt = null;
    
    /**
     * Reference to the CommandLine Interface that will be parsed.
     */
    private static CommandLine cmd = null;
    
    /**
     * The local port in which the proxy should listen.
     */
    private static int localPort;
    
    /**
     * The remote port for which the proxy should send data to.
     */
    private static int remotePort;
    
    /**
     * Flag for verbose execution of the software.
     */
    private static boolean verbose = false;
    
    /**
     * Path to an output file.
     */
    private static String outputFile = null;
    
    /**
     * The remote host.
     */
    private static String host = "localhost";
    
    /**
     * Main method of the class, responsible for parsing the Arguments and calling the other needed classes.
     * @param args The arguments that should be taken into account when running the program.
     * @throws ParseException 
     */
    public static void main(String[] args) throws ParseException {
        //Starting up the software
        startup();
        
        //Parsing the arguments
        CommandLineParser parser = new GnuParser();
        cmd = parser.parse(opt, args);
        
        //Setting up the options for execution
        parseArgs();
        
        //Creating a new Server
        Server proxyServer = new Server(localPort, remotePort, host, verbose);
        proxyServer.setRun(true);
        //Creating a thread for the Server
        Thread serverThread = new Thread(proxyServer);
        //Running the server
        serverThread.start();
    }
    
    /**
     * Method responsible for starting up the Options and the system itself.
     */
    private static void startup(){
        System.out.println("[Info]: Starting up YAJAProxy...");
        //Options set that will be used for the software
        opt = new Options();
        
        //Creating Options and Addind them to the set
        opt.addOption("l", true, "Local Port");
        opt.addOption("r", true, "Remote Port");
        opt.addOption("v", false, "Verbose mode");
        opt.addOption("h", true, "Remote Host");
        opt.addOption("f", true, "Print to file");
    }
    
    /**
     * Method responsible for parsing the command line, reading the flags and setting those on the software.
     */
    private static void parseArgs(){
        //Verbose mode
        if(cmd.hasOption("v")){
            verbose = true;
            System.out.format("[%s]: %s\n", "Flag", "Verbose Mode is activated.");
        }
        //Local Port
        if(cmd.hasOption("l")){
            localPort = Integer.parseInt(cmd.getOptionValue("l"));
            if(verbose){
                System.out.format("[%s]: %s %d\n", "Flag", "Local Port:", localPort);
            }
        }else{
            System.out.println("The Local Port argument is missing. Use -l <num> to set up a local port.");
            System.exit(1);
        }
        
        //Remote Port
        if(cmd.hasOption("r")){
            remotePort = Integer.parseInt(cmd.getOptionValue("r"));
            if(verbose){
                System.out.format("[%s]: %s %d\n", "Flag", "Remote Port:", remotePort);
            }
        }else{
            System.out.println("The Remote Port argument is missing. Use -r <num> to set up a remote port.");
            System.exit(2);
        }
        
        //Print to file
        if(cmd.hasOption("f")){
            outputFile = cmd.getOptionValue("f");
            if(verbose){
                System.out.format("[%s]: %s %s\n", "Flag", "Output File:", outputFile);
            }
        }else{
            if(verbose){
                System.out.format("[%s]: %s\n", "Flag", "File flag wasn't detected.");
            }
        }
        
        //Remote host
        if(cmd.hasOption("h")){
            host = cmd.getOptionValue("h");
            if(verbose){
                System.out.format("[%s]: %s %s\n", "Flag", "Remote Host:", host);
            }
        }else{
            System.out.println("The Remote Host argument is missing. Use -h <IPAddress> to set up a remote host.");
            System.exit(3);
        }
    }
    
    
}
