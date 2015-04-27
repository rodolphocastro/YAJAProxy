package com.ardc.yajaproxy.communication.interfaces;

/**
 * Interface describing the methods that should be implemented by a Server.
 * @author ALVES, R.C.
 */
public interface ServerInterface extends Runnable{
    
    /**
     * Method for running the server.
     */
    @Override
    public void run();
    
}
