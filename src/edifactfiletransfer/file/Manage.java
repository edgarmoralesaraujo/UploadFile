/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.file;

import algorithm.RSA;
import edifactfiletransfer.REST.Api;
import edifactfiletransfer.Validations;
import edifactfiletransfer.params.Params;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Edgar Morales
 */
public class Manage extends Params {

    private Directory dir;
    private RSA rsa;

    public Manage() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        super();
        log = org.apache.log4j.Logger.getLogger(Manage.class);
        
        Validations.existPublicKey(getPublic_key());
        Validations.existSourceDir(getSource());
        try {
            dir = new Directory(getSource());
        } catch (IOException ex) {
            log.error("Error al registrar el directorio fuente: "+ex.getMessage());
        }
        
        this.rsa = new RSA();
        this.rsa.openFromDiskPublicKey(getPublic_key());
    }

    public void process() {
        while (true) {
            List<WatchEvent<?>> events = null;
            try {
                events = dir.waiting();
            } catch (InterruptedException ex) {
                log.error("Error al esperar archivos: "+ex.getMessage());
            }

            if(events != null){
                for (WatchEvent<?> event : events) {
                    Path path = (Path) event.context();
                    //WatchEvent.Kind<?> kindOfEvent = event.kind();
                    //System.out.println(String.format("El evento '%s' fue detectado en '%s'", kindOfEvent.name(), path));
                    log.info("Procesando el archivo " + path);

                    //Obtenemos la información a cifrar a cifrar
                    edifactfiletransfer.file.File file = new edifactfiletransfer.file.File(getSource().concat(path.toString()));
                    String str = file.read();
                    byte[] strBytes = str.getBytes();
                    String strEncode = Base64.getEncoder().encodeToString(strBytes);

                    log.debug("TAMANO DE LA CADENA" + strBytes.length);
                    log.debug(strEncode);
                    
                    try {
                        byte[] rfc = this.rsa.Encrypt(getRfc());
                        byte[] input = this.rsa.EncryptForBlocks(strEncode);
                        byte[] filename = this.rsa.Encrypt(path.toString());

                        String data = "{\"rfc\": \"".concat(Base64.getEncoder().encodeToString(rfc)).concat("\", \"data\":\"").concat(Base64.getEncoder().encodeToString(input)).concat("\", \"filename\":\"".concat(Base64.getEncoder().encodeToString(filename)).concat("\"}"));

                        log.debug("REQUEST: "+data);

                        try{
                            Api api = new Api();
                            api.consume("upload", data);
                        } catch (Exception ex) {
                            log.error("Error al subir el archivo: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                            
                        log.info("ELIMINANDO =====> "+path.toAbsolutePath());
                        path.toFile().delete();
                        //Files.deleteIfExists(path.toAbsolutePath());
                        //java.io.File _file = new java.io.File(path.toAbsolutePath().toString());
                        //_file.delete();

                    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException | InvalidKeySpecException | NoSuchProviderException ex) {
                        log.error("Error al cifrar los datos: " + ex.getMessage());
                    }
                }
            }
            System.gc();
        }
    }
}
