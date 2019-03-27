package classes;

import java.util.ArrayList;


public class Search {

	public static void main(String[] args) throws Exception {
            ArrayList<String> dois = new ArrayList<String>();
            ArrayList<DatosArticulo> articulos = new ArrayList<DatosArticulo>(); 
            
            LeeFichero fich = new LeeFichero();
            dois = fich.leer();
            
            for(String doi: dois){
                Busqueda busqueda = new Busqueda();
                System.out.println("\nSend Http GET request");
                articulos.add(busqueda.sendGet(doi));
            }
            GeneraExcel fichero = new GeneraExcel();
            fichero.generaExcel(articulos);
        }
}
