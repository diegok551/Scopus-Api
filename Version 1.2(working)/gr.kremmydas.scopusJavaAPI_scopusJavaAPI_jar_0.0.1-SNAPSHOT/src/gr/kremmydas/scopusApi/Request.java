
package gr.kremmydas.scopusApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request {
    
    private String url;
    private String respuesta;
    private final String USER_AGENT = "Mozilla/5.0";
    
    public Request(String url){
        this.url = url;
    }
    
    public String sendRequest() throws MalformedURLException, IOException{
        URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");

	//add request headers
	con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("X-ELS-APIKey","793b68c43d37babe90e3ee9125414f9d");
        con.setRequestProperty("Accept","application/json");

	int responseCode = con.getResponseCode();
	//System.out.println("\nSending 'GET' request to URL : " + url);
	//System.out.println("Response Code : " + responseCode);
                
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
            //System.out.println("Respuesta 1 : " + response);
            
        return respuesta;
    }
}
