package classes;

import java.io.*;
import java.net.*;

public class Request {
    
    private String url;
    private String respuesta;
    private final String USER_AGENT = "Mozilla/5.0";
    
    public Request(String url){
        this.url = url;
    }
    
    public String sendRequest() throws MalformedURLException, IOException{
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add request headers
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("X-ELS-APIKey","793b68c43d37babe90e3ee9125414f9d");
            con.setRequestProperty("Accept","application/json");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Cierre de conexion
            in.close();
            respuesta = response.toString();
        }catch(Exception e){
           System.out.println("NO HAY CONEXION A INTERNET PARA COMPLETAR BUSQUEDA DE INFORMACION");
        }
 
        return respuesta;
    }
}
