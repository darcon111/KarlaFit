package app.karlafit.com.holder;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Recetas {

    private String id;
    private String titulo;
    private String calorias;
    private String tiempo;
    private String descripcion;
    private String ingredientes;
    private String preparacion;
    private String imagen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTutilo(String title) {
        this.titulo = title;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public Recetas() {
    }

    public Recetas(String id, String titulo, String calorias, String tiempo, String descripcion, String ingredientes, String preparacion, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.calorias = calorias;
        this.tiempo = tiempo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.preparacion = preparacion;
        this.imagen = imagen;
    }
}
