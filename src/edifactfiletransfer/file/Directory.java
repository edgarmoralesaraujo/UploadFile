/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 *
 * @author Edgar Morales
 */
public class Directory {
    
    private WatchService wService;
    private WatchKey key;
    
    public Directory(String source) throws IOException{
        wService = FileSystems.getDefault().newWatchService();
        
        Paths.get(source).register(wService, StandardWatchEventKinds.ENTRY_CREATE); //Al escribir los ficheros con PHP, primero crea el fichero y luego lo modifica, por lo que prestaremos atencion unicamente al evento ENTRY_MODIFY       
    }
    
    public List<WatchEvent<?>> waiting() throws InterruptedException {
        List<WatchEvent<?>> events = null;
       //Esperamos mientras tomamos algún evento
        key = wService.take();
        if (key.isValid()) {
            events = key.pollEvents();
        }
        //once an key has been processed,
        boolean valid = key.reset();
        return events; //Nunca es null por que wService.take() espera que la creacion de al menos un documento
    }
}
