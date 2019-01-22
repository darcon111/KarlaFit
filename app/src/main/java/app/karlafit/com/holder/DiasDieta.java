package app.karlafit.com.holder;

import javax.xml.transform.sax.SAXResult;

public class DiasDieta {

    private String id;
    private String title;
    private String calorias;
    private String descripcion;
    private String dia;
    private String semana;
    private String imagen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public DiasDieta(String id, String title, String calorias, String descripcion, String dia, String semana, String imagen) {
        this.id = id;
        this.title = title;
        this.calorias = calorias;
        this.descripcion = descripcion;
        this.dia = dia;
        this.semana = semana;
        this.imagen = imagen;
    }

    public DiasDieta() {
    }
}
