/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer;

import edifactfiletransfer.params.Logger;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Edgar Morales
 */
public class Validations extends Logger{
    public static void existPublicKey(String pubkey){
        if(!Files.exists(Paths.get(pubkey))){
            System.out.println("NO EXISTE");
            System.exit(0);
        }
    }
    
    public static void existSourceDir(String dir) {
        Path directoryPath = FileSystems.getDefault().getPath(dir);
        if (!Files.exists(directoryPath)) {
            log.warn(String.format("El directorio %s no existe.", directoryPath.toString()));
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException ex) {
                log.error("Error al crear el directorio fuente: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}