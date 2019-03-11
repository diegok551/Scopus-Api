package gr.kremmydas.scopusApi.restClient;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.parser.ParseException;

import gr.kremmydas.scopusApi.SearchResult;

public class RESTclient {
	
	private String ApiKey;
    private String search_uri = "http://api.elsevier.com/content/search/index:SCOPUS";
    private String view = "complete";
    private int RESULTS_COUNT = 10;
    
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private HttpGet httpget = new HttpGet();
    private CloseableHttpResponse response;
    private HttpEntity httpEntity = new BasicHttpEntity();
    
    
	public RESTclient(String apiKey) {
		super();
		ApiKey = apiKey;
	}


	public SearchResult search(String query) {
		
		
			SearchResult sr;
			URI theURI = null; String res;
			
			try {
				sr = new SearchResult("");
				return sr;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
	       
			/*
			//String theString= "query=doi("+URLEncoder.encode( doi , "UTF8" )+")"+ "&count="+ this.RESULTS_COUNT+"&view=" + this.view + "&" + this.ApiKey;
	 
	        try {
	             theURI = new URI(this.search_uri + "?" + theString);
	//System.out.println(theURI.toString());
	        } catch (URISyntaxException ex) {
	            Logger.getLogger(RESTclient.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	        try {
	            this.httpget.setURI(theURI);
	            this.response = httpclient.execute(this.httpget);
	        } catch (IOException ex) {
	            Logger.getLogger(RESTclient.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        //res = entityToString(this.response.getEntity());
	        this.response.close();
	        */
	        
	        //return sr;
	}
	
	
    
    
    

}
