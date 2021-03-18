/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.file;

import edifactfiletransfer.params.Params;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.io.FileNotFoundException;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 *
 * @author Edgar Morales
 */
public class File extends Params {

    private String stringPath;

    public File(String stringPath) {
        super();
        log = Logger.getLogger(File.class);
        this.stringPath = stringPath;
    }

    public String read() {
        String content = null;
        switch (getInput_type()) {
            case "XML":
                if(this.isValidType("text/xml")){
                    content = this.readXML();
                }
                break;

            case "TXT":
                break;

            default:
                log.warn("Tipo de entrada no definido.");
                break;
        }
        return content;
    }

    public String readAsBase64() {
        String base64 = null;

        return base64;
    }

    private String readXML() {
        String xml = null;
        Reader fileReader = null;

        this.readyToRead(new java.io.File(stringPath));

        if (getCharset() == null || getCharset().equals("")) {
            java.io.File xmlFile = new java.io.File(stringPath);

            // Let's get XML file as String using BufferedReader
            // FileReader uses platform's default character encoding
            // if you need to specify a different encoding, use InputStreamReader
            try {
                fileReader = new FileReader(xmlFile);
                xml = this.reader(fileReader);
            } catch (FileNotFoundException ex) {
                log.error("No existe el archivo " + stringPath);
                ex.printStackTrace();
            }
        } else {
            FileInputStream xmlFile = null;
            try {
                xmlFile = new FileInputStream(stringPath);
            } catch (FileNotFoundException ex) {
                log.error("No existe el archivo " + stringPath);
                ex.printStackTrace();
            }
            try {
                fileReader = new InputStreamReader(xmlFile, getCharset());
                xml = this.reader(fileReader);
                xmlFile.close();
            } catch (UnsupportedEncodingException ex) {
                log.error("Codificacion no soportada: " + getCharset());
                ex.printStackTrace();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return xml;
    }
    
    private String reader(Reader fileReader){
        String xml = null;
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(fileReader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            line = bufReader.readLine();

            while (line != null) {
                sb.append(line).append("\n");
                line = bufReader.readLine();
                xml = sb.toString();
            }
        } catch (IOException ex) {
            log.error("Error al leer el archivo: " + stringPath);
            ex.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                    
                } catch (IOException ex) {
                    log.error("Error al cerrar el archivo " + stringPath + ": " + ex.getMessage());
                }
            }
        }
        
        return xml;
    }

    private String readTXT() {
        String txt = null;

        return txt;
    }

    private void readyToRead(java.io.File file) {
        boolean bLocked = true;
        while (bLocked) {
            log.debug("ESPERANDO DESBLOQUEO DE ======>" + file.getName());
            try {
                RandomAccessFile fis = new RandomAccessFile(file, "rw");
                FileLock lck = fis.getChannel().lock();
                lck.release();
                fis.getChannel().close(); //Libera el archivo
                bLocked = false;
            } catch (Exception ex) {
                bLocked = true;
            }
        }
    }
    
    private boolean isValidType(String valid){
        String mime = "";
        try {
            mime = Files.probeContentType(Paths.get(stringPath));
            System.out.println(mime);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return (valid.equals(mime));
    }
}
