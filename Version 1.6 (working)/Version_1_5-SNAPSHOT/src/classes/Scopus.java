
package classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Scopus {
    
    private String respuesta;
    private final String USER_AGENT = "Mozilla/5.0";
    private String url;

    
    public String sendRequestArticulo (String doi) throws IOException{
        this.url = "https://api.elsevier.com/content/search/scopus?query=DOI%28" + doi + "%29&view=complete";
        this.respuesta  = sendRequest();
        return respuesta;
    }
    
    public String sendRequestAutor (String idAutor) throws IOException{
        this.url = "https://api.elsevier.com/content/author/author_id/%7B" + idAutor + "%7D?httpAccept=application/json&&apiKey=793b68c43d37babe90e3ee9125414f9d&view=METRICS";
        this.respuesta  = sendRequest();
        return respuesta;
    }
    
    public String sendRequestPublicacion (String issn) throws IOException{
        this.url = "https://api.elsevier.com/content/serial/title/?issn=" + issn;
        this.respuesta  = sendRequest();
        return respuesta;
    }
    
    private String sendRequest() throws MalformedURLException, IOException{
        String resp = "";
        
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
            resp = response.toString();
            
        }catch(Exception e){
            e.printStackTrace();
        }
 
        return resp;
    }
}
