package gr.kremmydas.scopusApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Search {
    
	private final String USER_AGENT = "Mozilla/5.0";
        private int citas;
        private ArrayList<Author> autores;
        
	public static void main(String[] args) throws Exception {

		Search http = new Search();
		http.sendGet();

	}
        
	// HTTP GET request
	private void sendGet() throws Exception {
            String a ;
        Scanner tec=new Scanner(System.in);
        System.out.println("Recuerda no dejar espacion en blanco\npor favor introduce el DOI");
        a=tec.nextLine();
                
                String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28"+a+"%29&view=complete";
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
		System.out.println("Response Code : " + responseCode+"\n");
                
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
                
                //Cierre de conexion
		in.close();

		//print result
		System.out.println(response.toString());
                
                
                //Tratamiento de la respuesta
                SearchResult respuesta = new SearchResult(response.toString());
                ArrayList<Entry> entradas = respuesta.getEntries();
                for (Entry entrada: entradas){
                    citas = entrada.getCitedByCount();
                    autores = entrada.getAuthors();
                    int i = 0;
                   for (Author autor: autores){
                       i++;
                       System.out.println("Identificador del autor " + i + " : " + autor.getScopusID());
                   }
                }
                
                //print result formateado
                System.out.println("El numero de citas es : " + citas);

	}

}
