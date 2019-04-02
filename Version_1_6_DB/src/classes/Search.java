package classes;

import com.mysql.jdbc.*;
import java.sql.*;
import java.util.ArrayList;
import java.io.*;
import java.sql.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.ss.usermodel.Row;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Search {

	public static void main(String[] args) throws Exception {
            ArrayList<String> dois = new ArrayList<String>();
            ArrayList<DatosArticulo> articulos = new ArrayList<DatosArticulo>(); 
            
          //  Conexion datos = new  Conexion();
          //  datos.conexion();
            
            LeeFichero fich = new LeeFichero();
            dois = fich.leer();
            
            
            for(String doi: dois){
                Busqueda busqueda = new Busqueda();
                System.out.println("\nSend Http GET request");
                articulos.add(busqueda.sendGet(doi));
            }
            //conexion a base de datos//
            try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/scopus", "root", "");
            //"scopus" es el nombre de la base de datos, "root" es el usuario ,"" es la contraseña
            con.setAutoCommit(false);
            PreparedStatement pstm = null ;
            FileInputStream input = new FileInputStream("C:/Documentos/Resultados.xls");
            //ubicacion del archivo 
            POIFSFileSystem fs = new POIFSFileSystem( input );
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for(int i=1; i<=sheet.getLastRowNum(); i++){
                row = sheet.getRow(i); 
                String doi    = row.getCell(0).getStringCellValue();
                String titulo = row.getCell(1).getStringCellValue();
                int numero   = (int) row.getCell(2).getNumericCellValue();
                String sjr    = row.getCell(3).getStringCellValue();
                String asjr   = row.getCell(4).getStringCellValue();
                String snip   = row.getCell(5).getStringCellValue();
                String asnip  = row.getCell(6).getStringCellValue();
                String cscore = row.getCell(7).getStringCellValue();
                String ascore = row.getCell(8).getStringCellValue();
                String sql = "INSERT INTO articulo VALUES('"+doi+"','"+titulo+"','"+numero+"','"+sjr+"','"+asjr+"','"+snip+"','"+asnip+"','"+cscore+"','"+ascore+"')";
                pstm = (PreparedStatement) con.prepareStatement(sql);
                pstm.execute();
                System.out.println("Registro :"+i);
            }
            con.commit();
            pstm.close();
            con.close();
            input.close();
            System.out.println("tabla de articulo");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }catch(SQLException ex){
            System.out.println(ex);
        }catch(IOException ioe){
            System.out.println(ioe);
        }
            System.out.println("terminando tabla de articulos");
         ////////////////////////////////////////////////////////////////////
         System.out.println("iniciando tabla de conference antes");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/scopus", "root", "");
            con.setAutoCommit(false);
            PreparedStatement pstm = null ;
            FileInputStream input = new FileInputStream("C:/Documentos/Resultados.xls");
            POIFSFileSystem fs = new POIFSFileSystem( input );
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(1);
            Row row;
            for(int i=1; i<=sheet.getLastRowNum(); i++){
                row = sheet.getRow(i); 
                String doi    = row.getCell(0).getStringCellValue();
                String titulo = row.getCell(1).getStringCellValue();
                int numero    = (int) row.getCell(2).getNumericCellValue();
                String sjr    = row.getCell(3).getStringCellValue();
                String asjr   = row.getCell(4).getStringCellValue();
                String snip   = row.getCell(5).getStringCellValue();
                String asnip  = row.getCell(6).getStringCellValue();
                String cscore = row.getCell(7).getStringCellValue();
                String ascore = row.getCell(8).getStringCellValue();
                String sql = "INSERT INTO confeantes VALUES('"+doi+"','"+titulo+"','"+numero+"','"+sjr+"','"+asjr+"','"+snip+"','"+asnip+"','"+cscore+"','"+ascore+"')";
                pstm = (PreparedStatement) con.prepareStatement(sql);
                pstm.execute();
                System.out.println("Registro :"+i);
            }
            con.commit();
            pstm.close();
            con.close();
            input.close();
            System.out.println("tabla de conference antes ");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println(ex);
        }catch(IOException ioe){
            System.out.println(ioe);
        }
        System.out.println("terminando tabla de conference antes");
         ////////////////////////////////////////////////////////////////////
         System.out.println("iniciando tabla de conference 2015");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/scopus", "root", "");
            con.setAutoCommit(false);
            PreparedStatement pstm = null ;
            FileInputStream input = new FileInputStream("C:/Documentos/Resultados.xls");
            POIFSFileSystem fs = new POIFSFileSystem( input );
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(2);
            Row row;
            for(int i=1; i<=sheet.getLastRowNum(); i++){
                row = sheet.getRow(i); 
                String doi    = row.getCell(0).getStringCellValue();
                String titulo = row.getCell(1).getStringCellValue();
                int numero   = (int) row.getCell(2).getNumericCellValue();
                String sjr    = row.getCell(3).getStringCellValue();
                String asjr   = row.getCell(4).getStringCellValue();
                String snip   = row.getCell(5).getStringCellValue();
                String asnip  = row.getCell(6).getStringCellValue();
                String cscore = row.getCell(7).getStringCellValue();
                String ascore = row.getCell(8).getStringCellValue();
                String sql = "INSERT INTO conference2015 VALUES('"+doi+"','"+titulo+"','"+numero+"','"+sjr+"','"+asjr+"','"+snip+"','"+asnip+"','"+cscore+"','"+ascore+"')";
                pstm = (PreparedStatement) con.prepareStatement(sql);
                pstm.execute();
                System.out.println("Registro :"+i);
            }
            con.commit();
            pstm.close();
            con.close();
            input.close();
            System.out.println("tabla de conference 2015");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }catch(SQLException ex){
            System.out.println(ex);
            ex.printStackTrace();
        }catch(IOException ioe){
            System.out.println(ioe);
        }
            System.out.println("terminando tabla de conference 2015");
        
        }
        
}
