package gr.kremmydas.scopusApi;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;
import javax.swing.*;
import org.json.*;

public class Search {
    
    //PONER EL DOI AQUI
        //doi con issn y parametros de calidad
        private final String doi = "10.1186/s40658-018-0240-9";
        //doi con numero de citas
        //private final String doi = "10.1016/j.eswa.2015.07.061";
        
        //Indicadores de calidad
        private int citas;
        private String[] hindex = new String[15];
        private String snip;
        private String CiteScore;
        private String sjr;
        
        //Variables auxiliares
        private String issn;
        private String source;
        private String source2;
        private String source3;
        private String anioSnip;
        private String anioCiteScore;
        private String anioSJR;
	private String ti;
        private int totalResults;
        private ArrayList<String> idAutores= new ArrayList<>();
        
	public static void main(String[] args) throws Exception {

		Search http = new Search();
	//	System.out.println("Send Http GET request");
		http.sendGet();
                	
	}

	// HTTP GET request
	private void sendGet() throws Exception {
        String a ;
        /*Scanner tec=new Scanner(System.in);
        System.out.println("Recuerda no dejar espacion en blanco\npor favor introduce el DOI");
        a=tec.nextLine();*/
        a=JOptionPane.showInputDialog("introduce el DOI a Buscar");
                
    //  String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28" + doi + "%29&view=complete";
        /*se debe quitar la parte comentada para habilitar la busqueda manual */
	String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28" + a + "%29&view=complete";
		
                //Realizar peticion
                Request peticion1 = new Request(url);
                source = peticion1.sendRequest();
                
                //Tratamiento de la respuesta
                
                JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(this.source)).get("search-results");
                
                //............................................................//
		
		this.totalResults=Integer.parseInt((String)jsonObject.get("opensearch:totalResults"));
		if(this.totalResults>0) {
			JSONArray jsEntries = (JSONArray)jsonObject.get("entry");
                        
			for(Object o: jsEntries) {
                            JSONObject jo = (JSONObject)o;
                                
                        //  Obtener el numero de citas
                            citas = (jo.get("citedby-count")!=null?Integer.parseInt((String)jo.get("citedby-count")):0);
                            ti=(jo.get("dc:title")!=null?( (String)jo.get("dc:title")):"");
                            
                            //Obtener el issn
                            if(jo.get("prism:issn")!=null)
                                issn = (String)jo.get("prism:issn");
                            else if (jo.get("prism:eIssn")!=null)
                                issn = (String)jo.get("prism:eIssn");
                            else
                                issn = "";
                            

                            String jsonString = "{\"infile\": [{\"Titulo\":"+ti+",\"ISSN\":"+ issn +",\"Citas\":"+ citas +"}]}";
                            //String jsonString = "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\": 13},{\"field1\": 21,\"field2\": 22,\"field3\": 23},{\"field1\": 31,\"field2\": 32,\"field3\": 33}]}";
                            org.json.JSONObject output;
                            try {
                                output = new org.json.JSONObject(jsonString);
                                org.json.JSONArray docs = output.getJSONArray("infile");
                                File file=new File("C:/Documentos/ExTixxx"+ti+".csv");
                                String csv = CDL.toString(docs);
                                FileUtils.writeStringToFile(file, csv);
                                JOptionPane.showMessageDialog(null,
                                                        "El docuemnto excel ya esta creado: "                                 
                                                      , "NOTIFICACION",JOptionPane.INFORMATION_MESSAGE);
                            } catch (JSONException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                               //TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            
                            //Obtener parametros de calidad del medio de publicacion
                            String url3 = "https://api.elsevier.com/content/serial/title/?issn=" + issn;
                            Request peticion3 = new Request(url3);
                            source3 = peticion3.sendRequest();
                            
                            JSONObject jo3 = (JSONObject)((JSONObject) parser.parse(this.source3)).get("serial-metadata-response");
                            JSONArray issnEntry = (JSONArray)jo3.get("entry");
                            for(Object o2: issnEntry) {
                                JSONObject snipList = (JSONObject)o2;
                                
                                //Obtener SNIP
                                JSONObject snipList2 = (JSONObject) snipList.get("SNIPList");
                                JSONArray snipList3 = (JSONArray) snipList2.get("SNIP");
                                for (Object o3: snipList3){
                                    JSONObject snipList4 = (JSONObject) o3;
                                    anioSnip = (String)snipList4.get("@year");
                                    snip = (String)snipList4.get("$");
//                                    System.out.println("El snip es: " + snip + " del a\u00f1o: " + anioSnip);
                                }
                                //Obtener SJR
                                JSONObject sjrList = (JSONObject) snipList.get("SJRList");
                                JSONArray sjrList2 = (JSONArray) sjrList.get("SJR");
                                for (Object o4: sjrList2){
                                    JSONObject sjrList3 = (JSONObject) o4;
                                    anioSJR = (String)sjrList3.get("@year");
                                    sjr = (String)sjrList3.get("$");
//                                    System.out.println("El sjr es: " + sjr + " del a\u00f1o: " + anioSJR);
                                }
                                
                                //Obtener CiteScore
                                JSONObject citeScoreList = (JSONObject) snipList.get("citeScoreYearInfoList");
                                anioCiteScore = (String)citeScoreList.get("citeScoreCurrentMetricYear");
                                CiteScore = (String)citeScoreList.get("citeScoreCurrentMetric");
//                                System.out.println("El CiteScore es: " + CiteScore + " del a\u00f1o: " + anioCiteScore);
                                
                            }
                            // muestra de informacion 
                                    JOptionPane.showMessageDialog(null,
                                    "El n\u00famero de citas es : " + citas +
                                    "\n El    doi   es : " + doi +
                                    "\n El   titulo es : " + ti +
                                    "\n El    snip  es : " + snip + " del a\u00f1o: " + anioSnip +
                                    "\n El    sjr   es : " + sjr  + " del a\u00f1o: " + anioSJR +
                                    "\nEl CiteScore es : " + CiteScore + " del a\u00f1o: " + anioCiteScore + 
                                    "\nEl documento se llama ExTixxx"+ti
                                  , "Datos de la Busqueda",JOptionPane.INFORMATION_MESSAGE);
                            //Crear autores
                            JSONArray jsAuthors = (JSONArray)jo.get("author");
                            if(jsAuthors!=null) {
                                for(Object objeto: jsAuthors) {
                                    JSONObject jo2 = (JSONObject)objeto;
                                    this.idAutores.add((jo2.get("authid")!=null?((String)jo2.get("authid")):""));
                                }
                            }
                            
                            //Obtener h-index de cada autor
                            int i = 0;
                            for (String autor: idAutores){
                            i++;

                            String url2 = "https://api.elsevier.com/content/author/author_id/%7B" + autor + "%7D?httpAccept=application/json&&apiKey=793b68c43d37babe90e3ee9125414f9d&view=METRICS";
                            Request peticion2 = new Request(url2);
                            source2 = peticion2.sendRequest();
                            JSONArray jsonObject2 = (JSONArray)((JSONObject) parser.parse(this.source2)).get("author-retrieval-response");
                            String primerostring = jsonObject2.get(0).toString();
                            JSONParser parser3 = new JSONParser();
                            JSONObject primero = (JSONObject) parser3.parse(primerostring);
                            hindex[i-1] = primero.get("h-index").toString();
                        //    System.out.println("h-index del autor " + i + " : " + hindex[i-1]);
                            JOptionPane.showMessageDialog(null,"h-index del autor " + i + " : " + hindex[i-1]+"\n"                                   
                                  , "Index de Autor # "+ i +"",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                }
                JOptionPane.showMessageDialog(null,
                                                        "Fin de la informacion mostrada", "AVISO",JOptionPane.INFORMATION_MESSAGE);
	}
        
}
