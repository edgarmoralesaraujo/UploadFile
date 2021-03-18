/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.params;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Edgar Morales
 */
public class Logger {

    public static org.apache.log4j.Logger log;

    public Logger() {
        String log4jConfigFile = System.getProperty("user.dir") + File.separator + "properties" + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
    }
}