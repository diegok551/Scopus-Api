
package classes;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class GeneraExcel {
    
    private ArrayList<DatosArticulo> articulosTipo0 = new ArrayList<DatosArticulo>();
    private ArrayList<DatosArticulo> articulosTipo1 = new ArrayList<DatosArticulo>();
    private ArrayList<DatosArticulo> articulosTipo2 = new ArrayList<DatosArticulo>();
    
    public void generaExcel (ArrayList<DatosArticulo> articulos) throws IOException{

        //Clasificación de los articulos    
        for(DatosArticulo articulo: articulos){
            articulo.imprimir();
            if(articulo.getTipo() == 0)
                articulosTipo0.add(articulo);
            else if (articulo.getTipo() == 1)
                articulosTipo1.add(articulo);

            else if (articulo.getTipo() == 2)
                articulosTipo2.add(articulo);
        }
        //Creación del fichero
        HSSFWorkbook workbook = new HSSFWorkbook();
        generarHoja(articulosTipo0,workbook,"Articulos");
        generarHoja(articulosTipo1,workbook,"Conference proceedings de antes de 2015");
        generarHoja(articulosTipo2,workbook,"Conference proceedings de 2015 en adelante");
    }
    
    private void generarHoja (ArrayList<DatosArticulo> articulosTipo, HSSFWorkbook workbook, String nombre) throws FileNotFoundException, IOException{
        
        HSSFSheet sheet = workbook.createSheet(nombre);
        Row fila = sheet.createRow(0);
        File archivo = new File("Resultados.xls");
        Cell celda;
        
        //Crear cabeceras
        celda = fila.createCell(0);
        celda.setCellValue("DOI");
        celda = fila.createCell(1);
        celda.setCellValue("TITULO");
        celda = fila.createCell(2);
        celda.setCellValue("NUMERO CITAS");
        celda = fila.createCell(3);
        celda.setCellValue("SJR");
        celda = fila.createCell(4);
        celda.setCellValue("AÑO SJR");
        celda = fila.createCell(5);
        celda.setCellValue("SNIP");
        celda = fila.createCell(6);
        celda.setCellValue("AÑO SNIP");
        celda = fila.createCell(7);
        celda.setCellValue("CITESCORE");
        celda = fila.createCell(8);
        celda.setCellValue("AÑO CITESCORE");
        celda = fila.createCell(9);
        celda.setCellValue("H-INDEX");
        
        int i = 1;
        for(DatosArticulo articulo: articulosTipo){
            Row filas = sheet.createRow(i);
            celda = filas.createCell(0);
            celda.setCellValue(articulo.getDoi());
            celda = filas.createCell(1);
            celda.setCellValue(articulo.getTitulo());
            celda = filas.createCell(2);
            celda.setCellValue(articulo.getCitas());
            celda = filas.createCell(3);
            celda.setCellValue(articulo.getSjr());
            celda = filas.createCell(4);
            celda.setCellValue(articulo.getAnioSjr());
            celda = filas.createCell(5);
            celda.setCellValue(articulo.getSnip());
            celda = filas.createCell(6);
            celda.setCellValue(articulo.getAnioSnip());
            celda = filas.createCell(7);
            celda.setCellValue(articulo.getCiteScore());
            celda = filas.createCell(8);
            celda.setCellValue(articulo.getAnioCiteScore());
            
            int j=9;
            for(String indice: articulo.getHIndex()){
                celda = filas.createCell(j);
                celda.setCellValue(indice);
                j++;
            }
            i++;
        }
        
        try {
            FileOutputStream out = new FileOutputStream(archivo);
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            System.out.println("Error de escritura");
            e.printStackTrace();
        }


    }
    
}
