package app.karlafit.com.holder;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Dias {

    private String id;
    private String title;
    private String subtitle;
    private String semana_id;
    private String imagen;
    private String tiempo;
    private String descripcion;
    private String video;


    public Dias(String id, String title, String subtitle, String semana_id, String imagen, String tiempo, String descripcion, String video) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.semana_id = semana_id;
        this.imagen = imagen;
        this.tiempo = tiempo;
        this.descripcion = descripcion;
        this.video = video;
    }


    public Dias() {
    }

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSemana_id() {
        return semana_id;
    }

    public void setSemana_id(String semana_id) {
        this.semana_id = semana_id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
