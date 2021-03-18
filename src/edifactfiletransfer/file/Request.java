/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.file;

import edifactfiletransfer.params.Params;
import org.apache.log4j.Logger;

/**
 *
 * @author Edgar Morales
 */
public class Request extends Params implements Runnable{
    private String input;
    
    public Request(String input){
        super();
        this.input = input;
        log = Logger.getLogger(Request.class);
    }
    
    @Override
    public void run(){
        log.info("Enviando "+input+" a "+getApi());
        try {
            this.finalize();
        } catch (Throwable ex) {
            log.error("Error al finalizar el hilo");
        }
    }
}
