
package classes;

import java.util.ArrayList;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
///////////////////////
import com.mysql.jdbc.*;
import com.mysql.jdbc.Connection;
import java.sql.*;
import java.util.logging.*;
///////////////////



public class Busqueda {
    
    private DatosArticulo articulo = new DatosArticulo();
    
    public Busqueda(){
    }
    
    // HTTP GET request
    public DatosArticulo sendGet(String doi) throws Exception {
        articulo.setDoi(doi);
        
        try{
            String url = "https://api.elsevier.com/content/search/scopus?query=DOI%28" + doi + "%29&view=complete";

            //Realizar peticion
            Request peticion1 = new Request(url);
            String source = peticion1.sendRequest();
            

            //Tratamiento de la respuesta
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)((JSONObject)parser.parse(source)).get("search-results");

            int totalResults=Integer.parseInt((String)jsonObject.get("opensearch:totalResults"));
            if(totalResults>0) {
                    JSONArray jsEntries = (JSONArray)jsonObject.get("entry");

                    for(Object o: jsEntries) {
                        JSONObject jo = (JSONObject)o;

                        //Obtener el numero de citas
                        try{
                            articulo.setCitas((jo.get("citedby-count")!=null?Integer.parseInt((String)jo.get("citedby-count")):0));
                        }catch(Exception e){
                            System.out.println("Fallo en la obtencion del numero de citas");
                            articulo.setCitas(-1);
//                            e.printStackTrace();
                        }

                        //Obtener el titulo
                        try{
                        articulo.setTitulo((jo.get("dc:title")!=null?(String)jo.get("dc:title"):""));
                        }catch(Exception e){
                            System.out.println("Fallo en la obtencion del titulo");
                            articulo.setTitulo("Titulo no disponible");
//                            e.printStackTrace();
                        }
                        
                        //Obtener el tipo
                        try{
                            String subTipo = (jo.get("subtype")!=null?(String)jo.get("subtype"):"");
                            if("cp".equals(subTipo) || "cr".equals(subTipo)){
                                String anioAux = (jo.get("prism:coverDate")!=null?(String)jo.get("prism:coverDate"):"");
                                int anio = Integer.parseInt(anioAux.substring(0, 4));
                                if(anio<2015)
                                    articulo.setTipo(1); //Tipo 1 para conference papers de antes de 2015
                                else
                                    articulo.setTipo(2); //Tipo 2 para conference papers de 2015 en adelante
                            }
                            else
                                articulo.setTipo(0); //Tipo 0 para el resto de papers

                        }catch(Exception e){
                            System.out.println("Fallo en la obtencion del tipo");
                            articulo.setTipo(-1);
//                            e.printStackTrace();
                        }


                        //Obtener el issn
                        String issn;
                        if(jo.get("prism:issn")!=null)
                            issn = (String)jo.get("prism:issn");
                        else if (jo.get("prism:eIssn")!=null)
                            issn = (String)jo.get("prism:eIssn");
                        else
                            issn = "";

                        //Obtener parametros de calidad del medio de publicacion
                        if(issn != ""){
                            try{
                                String url3 = "https://api.elsevier.com/content/serial/title/?issn=" + issn;
                                Request peticion3 = new Request(url3);
                                String source3 = peticion3.sendRequest();
                                System.out.println(source3);
                                JSONObject jo3 = (JSONObject)((JSONObject) parser.parse(source3)).get("serial-metadata-response");
                                JSONArray issnEntry = (JSONArray)jo3.get("entry");
                                for(Object o2: issnEntry) {
                                JSONObject snipList = (JSONObject)o2;

                                    //Obtener SNIP
                                    try{
                                        JSONObject snipList2 = (JSONObject) snipList.get("SNIPList");
                                        JSONArray snipList3 = (JSONArray) snipList2.get("SNIP");
                                        for (Object o3: snipList3){
                                            JSONObject snipList4 = (JSONObject) o3;
                                            articulo.setAnioSnip((String)snipList4.get("@year"));
                                            articulo.setSnip((String)snipList4.get("$"));
                                        }
                                    }catch(Exception e){
                                        System.out.println("Fallo en la obtencion del snip");
                                        articulo.setSnip(" Snip no disponible ");
                                        articulo.setAnioSnip(" no disponible ");
//                                        e.printStackTrace();
                                    }

                                    //Obtener SJR
                                    try{
                                        JSONObject sjrList = (JSONObject) snipList.get("SJRList");
                                        JSONArray sjrList2 = (JSONArray) sjrList.get("SJR");
                                        for (Object o4: sjrList2){
                                            JSONObject sjrList3 = (JSONObject) o4;
                                            articulo.setAnioSJR((String)sjrList3.get("@year"));
                                            articulo.setSjr((String)sjrList3.get("$"));
                                        }
                                    }catch(Exception e){
                                        System.out.println("Fallo en la obtencion del Ajr");
                                        articulo.setSjr(" Sjr no disponible ");
                                        articulo.setAnioSJR(" no disponible ");
 //                                       e.printStackTrace();
                                    }


                                    //Obtener CiteScore
                                    try{
                                        JSONObject citeScoreList = (JSONObject) snipList.get("citeScoreYearInfoList");
                                        articulo.setAnioCiteScore((String)citeScoreList.get("citeScoreCurrentMetricYear"));
                                        articulo.setCiteScore((String)citeScoreList.get("citeScoreCurrentMetric"));
                                    }catch(Exception e){
                                        System.out.println("Fallo en la obtencion del CiteScore");
                                        articulo.setCiteScore(" CiteScore no disponible ");
                                        articulo.setAnioCiteScore(" no disponible ");
   //                                     e.printStackTrace();
                                    }


                                }
                            }catch(Exception e){
                                System.out.println("Fallo de issn");
  //                              e.printStackTrace();
                                articulo.setSnip(" Snip no disponible ");
                                articulo.setAnioSnip(" no disponible ");
                                articulo.setSjr(" Sjr no disponible ");
                                articulo.setAnioSJR(" no disponible ");
                                articulo.setCiteScore(" CiteScore no disponible ");
                                articulo.setAnioCiteScore(" no disponible ");
                            }
                       
                        }
                        else{
                            System.out.println("No tiene issn");
                            articulo.setSnip(" Snip no disponible ");
                            articulo.setAnioSnip(" no disponible ");
                            articulo.setSjr(" Sjr no disponible ");
                            articulo.setAnioSJR(" no disponible ");
                            articulo.setCiteScore(" CiteScore no disponible ");
                            articulo.setAnioCiteScore(" no disponible ");
                        }
                        //Crear autores
                        JSONArray jsAuthors = (JSONArray)jo.get("author");
                        ArrayList<String> idAutores= new ArrayList<>();
                        if(jsAuthors!=null) {
                            for(Object objeto: jsAuthors) {
                                JSONObject jo2 = (JSONObject)objeto;
                                idAutores.add((jo2.get("authid")!=null?((String)jo2.get("authid")):""));
                            }
                        }

                        //Obtener h-index de cada autor
                        //int i = 0;
                        ArrayList<String> hindex= new ArrayList<>();
                        for (String autor: idAutores){
                            //i++;

                            String url2 = "https://api.elsevier.com/content/author/author_id/%7B" + autor + "%7D?httpAccept=application/json&&apiKey=793b68c43d37babe90e3ee9125414f9d&view=METRICS";
                            Request peticion2 = new Request(url2);
                            String source2 = peticion2.sendRequest();

                            JSONArray jsonObject2 = (JSONArray)((JSONObject) parser.parse(source2)).get("author-retrieval-response");
                            String primerostring = jsonObject2.get(0).toString();
                            JSONParser parser3 = new JSONParser();
                            JSONObject primero = (JSONObject) parser3.parse(primerostring);
                            hindex.add(primero.get("h-index").toString());
                        }
                        articulo.setHIndex(hindex);
                    }
                }
            
        Conexion datos = new  Conexion();
        Connection registro = datos.conexion();
        String sql="";
        }
                
        catch(Exception e){
         //       System.out.println("NO HAY CONEXION A INTERNET");
            }
        
        return articulo;
    }
    
}
