package gr.kremmydas.scopusApi;

	
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Search {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		Search http = new Search();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();

	}

	// HTTP GET request
	private void sendGet() throws Exception {
                
            //URLs ERRONEAS
		//String url = "https://api.elsevier.com/content/abstract/citation-count?doi=10.1038/s41467-018-08078-w&apiKey=793b68c43d37babe90e3ee9125414f9d&httpAccept=application/json";
                //String url = "https://api.elsevier.com/authenticate/?httpAccept=application/json&apiKey=793b68c43d37babe90e3ee9125414f9d";
                //String url = "http://api.elsevier.com/content/search/scopus?query=AUTHLASTNAME%28brown%29%20AND%20SUBJAREA%28CHEM%29&apikey=793b68c43d37babe90e3ee9125414f9d&httpAccept=application/json";
                
            //URL valida
                String url = "https://api.elsevier.com/content/search/scopus?query=DOI%2810.1109/ISESE.2004.1334895%29&apikey=793b68c43d37babe90e3ee9125414f9d&httpAccept=application/json";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

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
		in.close();

		//print result
		System.out.println(response.toString());

	}

}
