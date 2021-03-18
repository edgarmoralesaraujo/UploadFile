/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer;

import edifactfiletransfer.file.Manage;
import edifactfiletransfer.params.Logger;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author Edgar Morales
 */
public class EdifactFileTransfer extends Logger{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Manage manage = new Manage();
            manage.process();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Error al abrir la llave pública: "+ex.getMessage());
        }
                
    }    
    
}