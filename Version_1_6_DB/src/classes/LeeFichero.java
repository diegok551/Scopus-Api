package classes;

import java.io.*;
import java.util.ArrayList;
public class LeeFichero {

   private ArrayList<String> dois = new ArrayList<String>();
   
   public ArrayList<String> leer() {
      File archivo = null;
      FileReader fr = null;
      BufferedReader br = null;

      try {
         archivo = new File ("archivo.bib");
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);

         String linea;
         while((linea=br.readLine())!=null){
             System.out.println(linea);
             if (linea.startsWith("doi")){
                 String cadena = linea.substring(5,linea.length()-2);
                 dois.add(cadena);
             }
         }
        System.out.println("Esto es dois: " + dois); 
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
      return dois;
   }
}

