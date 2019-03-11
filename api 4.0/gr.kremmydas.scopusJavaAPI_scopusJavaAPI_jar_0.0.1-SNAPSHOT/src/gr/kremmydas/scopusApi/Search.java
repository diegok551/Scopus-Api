package gr.kremmydas.scopusApi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search {
    
    //PONER EL DOI AQUI
       // private final String doi = "10.1109/ISESE.2004.1334895";
        
	private final String USER_AGENT = "Mozilla/5.0";
        private int citas;
        private String titulo;
        private ArrayList<Author> autores;
        private String[] hindex = new String[15];
        
	public static void main(String[] args) throws Exception {

		Search http = new Search();

		System.out.println("Send Http GET request");
		http.sendGet();


	}

	// HTTP GET request
	private void sendGet() throws Exception {
        String a ;
        Scanner tec=new Scanner(System.in);
        System.out.println("Recuerda no dejar espacion en blanco\npor favor introduce el DOI");
        a=tec.nextLine();
                
                String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28"+a+"%29&view=complete";
                
       //         String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28" + doi + "%29&view=complete";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("X-ELS-APIKey","793b68c43d37babe90e3ee9125414f9d");
                con.setRequestProperty("Accept","application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
                
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
                
                //Cierre de conexion
		in.close();               
                
                //Tratamiento de la respuesta
                SearchResult respuesta = new SearchResult(response.toString());
                ArrayList<Entry> entradas = respuesta.getEntries();
                for (Entry entrada: entradas){
                    citas = entrada.getCitedByCount();
                    autores = entrada.getAuthors();
                    titulo = entrada.getTitle();
                    int i = 0;
                   for (Author autor: autores){
                       i++;
                       String url2 = "https://api.elsevier.com/content/author/author_id/%7B" + autor.getScopusID() + "%7D?httpAccept=application/json&&apiKey=793b68c43d37babe90e3ee9125414f9d&view=METRICS";
                       HttpConnection conexion = new HttpConnection(url2);
                       conexion.getHIndex();
                       hindex[i-1] = conexion.getIndex();
                       System.out.println("h-index del autor " + i + " : " + hindex[i-1] + "\n");
 
                   }
                 
                }
                
                //print result formateado
               System.out.println("\nEl numero de citas es : " + citas +" este es el titulo  "+titulo );
    //   String jsonString =  "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\":}]}";
    System.out.println("\n creando docuemnto");
     String jsonString = "{\"infile\": [{\"Titulo\":"+titulo+",\"Citas\":"+citas+"}]}";
       JSONObject output;
        try {
            output = new JSONObject(jsonString);
            JSONArray docs = output.getJSONArray("infile");
            File file=new File("C:\\Documentos\\fromJSON.csv");
            System.out.println("\n documento creado ");
             String csv = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csv);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
           //TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}
