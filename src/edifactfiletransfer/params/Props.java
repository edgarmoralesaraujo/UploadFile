/*
 * To change this license header, choose License Headers in Project Props.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.params;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author edgarmorales
 */
public class Props {
    private Properties propiedad = new Properties();
    public Props() {
    try {
        String conf = System.getProperty("user.dir") + File.separator + "properties" + File.separator + "conf.properties";
        InputStream input = new FileInputStream(conf);
        this.propiedad.load(input);
        
        //this.propiedad.load(getClass().getResourceAsStream("/properties/conf.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public Properties getConfiguracion() { return this.propiedad; }
}