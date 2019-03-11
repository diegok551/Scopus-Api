
package gr.kremmydas.scopusApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.parser.ParseException;


public class HttpConnection {
    
    private String url;
    private final String USER_AGENT = "Mozilla/5.0";
    private String hindex;
    
    public HttpConnection (String url){
        this.url = url;
    }
    
    public void getHIndex() throws MalformedURLException, IOException, ParseException{
        URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        //Establecimiento metodo de conexion
        con.setRequestMethod("GET");
        
        //Establecimiento de headers
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("X-ELS-APIKey","793b68c43d37babe90e3ee9125414f9d");
        con.setRequestProperty("Accept","application/json");
        
        int responseCode = con.getResponseCode();
	System.out.println("Sending request to : " + url);
	System.out.println("Response Code busqueda autor : " + responseCode);
                
	BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
                
            //Cierre de conexion
            in.close();
            
            
            //Tratamiento de la entrada
            SResultAuthor respuesta = new SResultAuthor(response.toString());
            this.hindex = respuesta.getHIndex();
            
    }
    
    public String getIndex(){
        return hindex;
    }
    
}
