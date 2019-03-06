package gr.kremmydas.scopusApi;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class SResultAuthor {
    
    private String source;
    private String hindex;
    
    public SResultAuthor(String source) throws ParseException {
		super();
		this.source = source;
		this.parse();
    }
    
    private void parse() throws ParseException {
		JSONParser parser2 = new JSONParser();             
                JSONArray jsonObject = (JSONArray)((JSONObject) parser2.parse(this.source)).get("author-retrieval-response");
                String primerostring = jsonObject.get(0).toString();
                JSONParser parser3 = new JSONParser();
                JSONObject primero = (JSONObject) parser3.parse(primerostring);
                hindex = primero.get("h-index").toString();
                
    }
    
    public String getSource() {
	return source;
    }
    
    public String getHIndex() {
        return hindex;
    }
}
