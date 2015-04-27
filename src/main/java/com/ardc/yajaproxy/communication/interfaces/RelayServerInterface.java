package com.ardc.yajaproxy.communication.interfaces;

/**
 * Interface describing the methods a Relay Server should implement.
 * @author ALVES, R.C.
 */
public interface RelayServerInterface extends Runnable{
    
    /**
     * Method for running the Relay Server.
     */
    @Override
    public void run();
}
