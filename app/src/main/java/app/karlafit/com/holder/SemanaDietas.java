package app.karlafit.com.holder;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SemanaDietas {

    private String id;
    private String title;
    private String subtitle;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public SemanaDietas(String id, String title, String subtitle, String imagen) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.imagen = imagen;
    }

    public SemanaDietas() {
    }
}
