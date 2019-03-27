
package classes;

import java.util.ArrayList;

public class DatosArticulo {

    //Indicadores de calidad
    private int citas;
    private ArrayList<String> hindex = new ArrayList<String>();
    private String snip;
    private String CiteScore;
    private String sjr;
    private String doi;
    private String titulo;
    private String anioSnip;
    private String anioCiteScore;
    private String anioSJR;
    private int tipo;
    private String issn;
    private ArrayList<String> idAutores = new ArrayList<String>();

    //Constructor
    public DatosArticulo(){
    }
    
    public DatosArticulo (String doi, String titulo, int citas, ArrayList<String> hindex, 
            String snip, String CiteScore, String sjr, String anioSnip, String anioCiteScore, String anioSJR){
        this.doi = doi;
        this.titulo = titulo;
        this.citas = citas;
        this.hindex = hindex;
        this.snip = snip;
        this.CiteScore = CiteScore;
        this.sjr = sjr;
        this.anioSnip = anioSnip;
        this.anioCiteScore = anioCiteScore;
        this.anioSJR = anioSJR;
    }
    
    //Getters
    public String getDoi(){
        return doi;
    }
    
    public String getTitulo(){
        return titulo;
    }
    
    public int getCitas(){
        return citas;
    }
    
    public ArrayList<String> getHIndex(){
        return hindex;
    }
    
    public String getSnip(){
        return snip;
    }
    
    public String getCiteScore(){
        return CiteScore;
    }
    
    public String getSjr(){
        return sjr;
    }
    
    public String getAnioSjr(){
        return anioSJR;
    }
    
    public String getAnioCiteScore(){
        return anioCiteScore;
    }
    
    public String getAnioSnip(){
        return anioSnip;
    }
    
    public int getTipo(){
        return tipo;
    }
    
    public String getIssn(){
        return issn;
    }
    
    public ArrayList<String> getIdAutores(){
        return idAutores;
    }
    
    //Setters
    public void setDoi (String doi){
        this.doi = doi;
    }
    
    public void setTitulo (String titulo){
        this.titulo = titulo;
    }
    
    public void setSnip (String snip){
        this.snip = snip;
    }
    
    public void setSjr (String sjr){
        this.sjr = sjr;
    }
    
    public void setCiteScore (String CiteScore){
        this.CiteScore = CiteScore;
    }
    
    public void setAnioSnip (String anioSnip){
        this.anioSnip = anioSnip;
    }
    
    public void setAnioSJR (String anioSJR){
        this.anioSJR = anioSJR;
    }
    
    public void setAnioCiteScore (String anioCiteScore){
        this.anioCiteScore = anioCiteScore;
    }
    
    public void setCitas (int citas){
        this.citas = citas;
    }
    
    public void setHIndex (ArrayList<String> hindex){
        this.hindex = hindex;
    }
    
    
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    
    public void setIdAutores (ArrayList<String> idAutores){
        this.idAutores = idAutores;
    }
    
    public void setIssn (String issn){
        this.issn = issn;
    }
    
    //Imprimir DatosArticulo
    public void imprimir(){
        System.out.println("\nEstos son los datos del artículo guardado");
        System.out.println("    doi: " + doi);
        System.out.println("    Titulo: " + titulo);
        System.out.println("    El numero de citas es: " + citas);
        System.out.println("    El snip es: " + snip + " del año: " + anioSnip);
        System.out.println("    El sjr es: " + sjr + " del año: " + anioSJR);
        System.out.println("    El CiteScore es: " + CiteScore + " del año: " + anioCiteScore);
        int i = 0;
        for(String num: hindex){
            System.out.println("    h-index del autor " + (i+1) + " : " + hindex.get(i));
            i++;    
        }
        System.out.println("    Paper del tipo: " + tipo);
        
    }
    
}
