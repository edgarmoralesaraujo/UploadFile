/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.params;

import java.util.Properties;

/**
 *
 * @author Edgar Morales
 */
public class Params extends Logger{
    //public Logger log;
    Properties mr;
    //COMPANY
    private static String rfc;
    //INPUT FILE
    private static String source; //Directorio donde se leen los archivos de entrada
    private static String input_type;
    private static String charset; //Codificacion del archivo de entrada
    //CIFRADO
    private static String public_key; //Directorio donde se guarda la llave publica
    //SYSTEM
    private static int threads; //Numero maximo de hilos
    //API
    private static String api; //API para recepcion de archivo de entrada
    
    public Params(){
        super();
        //String log4jConfigFile = System.getProperty("user.dir") + File.separator + "properties" + File.separator + "log4j.properties";
        //PropertyConfigurator.configure(log4jConfigFile);
        
        mr = (new Props()).getConfiguracion();
        this.rfc = mr.getProperty("rfc");
        this.source = mr.getProperty("source");
        this.input_type = mr.getProperty("input_type");
        this.charset = mr.getProperty("charset");
        this.public_key = mr.getProperty("public_key");
        this.threads = Integer.parseInt(mr.getProperty("threads"));
        
        this.api = mr.getProperty("api");
    }

    public static String getRfc() {
        return rfc;
    }
    
    public static String getSource() {
        return source;
    }

    public static String getInput_type() {
        return input_type;
    }
    
    public static String getCharset() {
        return charset;
    }

    public static String getPublic_key() {
        return public_key;
    }

    public static int getThreads() {
        return threads;
    }

    public static String getApi() {
        return api;
    }
    
}
