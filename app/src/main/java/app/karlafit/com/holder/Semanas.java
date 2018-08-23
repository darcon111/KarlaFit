package app.karlafit.com.holder;

public class Semanas {

    private String id;
    private String title;
    private String subtitle;
    private String libras;
    private String imagen;
    private String title2;
    private String descripcion;

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

    public String getLibras() {
        return libras;
    }

    public void setLibras(String libras) {
        this.libras = libras;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Semanas(String id, String title, String subtitle, String libras) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.libras = libras;
    }
}
