
package classes;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatosArticulos {
    
    private String respuestaArticulo;
    private String respuestaAutor;
    private String respuestaPublicacion;
    private ArrayList<String> dois = new ArrayList<String>();
    private ArrayList<DatosArticulo> articulos = new ArrayList<DatosArticulo>(); 

    public void start() throws IOException, Exception {
        
        //Leer fichero bibText
        LeeFichero fich = new LeeFichero();
        dois = fich.leer();

        //Hacer peticion a la api de scopus por cada doi
        for(String doi: dois){
            DatosArticulo articulo = new DatosArticulo();
            System.out.println("\nSend Http GET request");
            Scopus busqueda = new Scopus();
            respuestaArticulo = busqueda.sendRequestArticulo(doi);
            articulo.setDoi(doi);
            trataJsonArticulo(articulo, respuestaArticulo);
            
            //Hacer peticion a la api de medio de publicacion
            if(articulo.getIssn() != ""){
                respuestaPublicacion = busqueda.sendRequestPublicacion(articulo.getIssn());
                trataJsonPublicacion(articulo, respuestaPublicacion);
            }else{
                System.out.println("No tiene issn");
                articulo.setSnip(" Snip no disponible ");
                articulo.setAnioSnip(" no disponible ");
                articulo.setSjr(" Sjr no disponible ");
                articulo.setAnioSJR(" no disponible ");
                articulo.setCiteScore(" CiteScore no disponible ");
                articulo.setAnioCiteScore(" no disponible ");
            }
            
            //Hacer peticion a la api de autor
            ArrayList<String> hindex= new ArrayList<>();
            for(String idAutor: articulo.getIdAutores()){ 
                respuestaAutor = busqueda.sendRequestAutor(idAutor);
                hindex.add(trataJsonAutor(respuestaAutor));
            }
            articulo.setHIndex(hindex);
            
            //Añadir articulo a la lista
            articulos.add(articulo);
            articulo.imprimir();
        }
        
        //Generar fichero excel
        Excel fichero = new Excel();
        fichero.generaExcel(articulos);
    }
        
    private void trataJsonArticulo (DatosArticulo articulo, String json){
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)((JSONObject)parser.parse(json)).get("search-results");

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
                        e.printStackTrace();
                    }

                    //Obtener el titulo
                    try{
                    articulo.setTitulo((jo.get("dc:title")!=null?(String)jo.get("dc:title"):""));
                    }catch(Exception e){
                        System.out.println("Fallo en la obtencion del titulo");
                        articulo.setTitulo("Titulo no disponible");
                        e.printStackTrace();
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
                        e.printStackTrace();
                    }


                    //Obtener el issn
                    if(jo.get("prism:issn")!=null)
                        articulo.setIssn((String)jo.get("prism:issn"));
                    else if (jo.get("prism:eIssn")!=null)
                        articulo.setIssn((String)jo.get("prism:eIssn"));
                    else
                        articulo.setIssn("");

                    //Obtener autores
                    JSONArray jsAuthors = (JSONArray)jo.get("author");
                    ArrayList<String> idAutores= new ArrayList<>();
                    if(jsAuthors!=null) {
                        for(Object objeto: jsAuthors) {
                            JSONObject jo2 = (JSONObject)objeto;
                            idAutores.add((jo2.get("authid")!=null?((String)jo2.get("authid")):""));
                        }
                    articulo.setIdAutores(idAutores);
                    }
                }
            }
        }catch(NumberFormatException | ParseException e){
        }
    }
    
    private void trataJsonPublicacion (DatosArticulo articulo, String json){
            try{
                JSONParser parser = new JSONParser();
                JSONObject jo3 = (JSONObject)((JSONObject) parser.parse(json)).get("serial-metadata-response");
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
                        e.printStackTrace();
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
                        e.printStackTrace();
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
                        e.printStackTrace();
                    }


                }
            }catch(Exception e){
                System.out.println("Fallo de issn");
                e.printStackTrace();
                articulo.setSnip(" Snip no disponible ");
                articulo.setAnioSnip(" no disponible ");
                articulo.setSjr(" Sjr no disponible ");
                articulo.setAnioSJR(" no disponible ");
                articulo.setCiteScore(" CiteScore no disponible ");
                articulo.setAnioCiteScore(" no disponible ");
            }

        }
    
    private String trataJsonAutor (String json){
        try{
            JSONParser parser = new JSONParser();
            JSONArray jsonObject2 = (JSONArray)((JSONObject) parser.parse(json)).get("author-retrieval-response");
            String primerostring = jsonObject2.get(0).toString();
            JSONParser parser3 = new JSONParser();
            JSONObject primero = (JSONObject) parser3.parse(primerostring);
            return (primero.get("h-index").toString());
        
        }catch(ParseException e){
            return "";
        } 
    }   
}
