/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edifactfiletransfer.REST;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.json.Json;
import javax.json.JsonObject;

import edifactfiletransfer.params.Params;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.stream.JsonParsingException;
import org.apache.http.entity.StringEntity;

/**
 *
 * @author Edgar Morales
 */
public class Api extends Params {

    public Api() {
        log = org.apache.log4j.Logger.getLogger(Api.class);
    }

    public void consume(String method, String dataJSON) {
        CloseableHttpClient client = null;
        try {
            client = HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE))
                    .build();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        log.info("Enviando documento...");
        //HttpGet serviceRequest = new HttpGet(getApi());
        HttpPost httpPost = new HttpPost(getApi().concat(method));
        //serviceRequest.addHeader("Authorization", "Bearer " + accessToken);
        try {
            System.out.println(dataJSON);
            StringEntity entity = new StringEntity(dataJSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        } catch (UnsupportedEncodingException ex) {
            log.error("La información que se desea enviar no se encuentra en un formato válido: "+ex.getMessage());
        }
        
        CloseableHttpResponse serviceResponse = null;
        try {
            serviceResponse = client.execute(httpPost);
        } catch (Exception ex) {
            log.error("Error al enviar el documento: "+ex.getMessage());
            ex.printStackTrace();
        }

        JsonObject serviceObject = null;
        try {
            serviceObject = Json.createReader(serviceResponse.getEntity().getContent()).readObject();
            System.out.printf("Result: %s%n", serviceObject.toString());
        } catch (IOException ex) {
            log.error("IOException: Error al leer la respuesta: "+ex.getMessage());
            ex.printStackTrace();
        } catch (UnsupportedOperationException ex) {
           log.error("UnsupportedOperationException: Error al leer la respuesta: "+ex.getMessage());
           ex.printStackTrace();
        } catch (JsonParsingException ex) {
            log.error("JsonParsingException: Error al leer la respuesta: "+ex.getMessage());
            ex.printStackTrace();
        }

    }
}
