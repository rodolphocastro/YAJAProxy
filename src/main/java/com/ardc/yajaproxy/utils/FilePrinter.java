package com.ardc.yajaproxy.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Implementation of a File Printer, a class that sole porpose is to write to an output file. 
 * @author ALVES, R.C.
 */
public class FilePrinter {
    
    /**
     * Self-Reference, ensuring this is a singleton.
     */
    private static FilePrinter fprinter = new FilePrinter();
    
    /**
     * PrintWriter used to write data to the file.
     */
    private static PrintWriter writer = null;
    
    /**
     * The path to the output file.
     */
    private static String file = null;
    
    /**
     * Flag for the verbose mode.
     */
    private static boolean verbose = false;
    
    /**
     * Private constructor, ensuring this class is a singleton.
     */
    private FilePrinter(){
        if(file!=null){
            setup();
        }
    }

    /**
     * Method for refreshing the FilePrinter.
     * @param newFile The new file that should be written to.
     */
    public static void refresh(String newFile){
        if(verbose){
            System.out.format("[%s]: %s.\n", "FilePrinter", String.format(
            "changed file from %s to %s", file, newFile));
        }
        file = newFile;
        setup();
    }
    
    /**
     * Method for setting up the PrintWriter.
     */
    private static void setup(){
        try{
            writer = new PrintWriter(file, "UTF-8");
            if(verbose){
                System.out.format("[%s]: %s.\n", "FilePrinter", "Refreshing the PrintWriter");
            }
        }catch(FileNotFoundException | UnsupportedEncodingException err){
            System.err.println("[FilePrinter]: File not found.");
        }
    }
    
    /**
     * Method for printing a message on a file.
     * @param tag The tag for identification of the message.
     * @param msg The message that should be printed to the file.
     */
    public static void printToFile(String tag, String msg){
        if(file!=null){
            writer.println(String.format("[%s]: %s.\n", tag, msg));
            writer.println("[/".concat(tag).concat("]\n"));
            if(verbose){
                System.out.format("[%s]: %s.\n", "FilePrinter", "Printing a new line on the file");
            }
        }
    }
    
    /**
     * Method for finishing up the process of writing to a file.
     */
    public static void finishWriting(){
        if(file!=null){
            if(verbose){
                System.out.format("[%s]: %s.\n", "FilePrinter", "Closing the output stream to the file.");
            }
            writer.close();
        }
    }
    
    //Getters and Setters automaticaly created by the IDE
    
    public static FilePrinter getFprinter() {
        return fprinter;
    }

    public static void setVerbose(boolean verbose) {
        FilePrinter.verbose = verbose;
    }

    
    
    
    
}
