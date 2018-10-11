package app.karlafit.com.clases;

/**
 * Created by USUARIO-PC on 20/03/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String id;
    private String email;
    private String url_imagen;
    private String firebase_code;
    private String provider;
    private String date_created;
    private String firebaseId;
    private String name;
    private String lastname;
    private String fecha_nac;
    private String lat;
    private String log;
    private String mobile;
    private String estatura;
    private String edad;
    private String peso;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getFirebase_code() {
        return firebase_code;
    }

    public void setFirebase_code(String firebase_code) {
        this.firebase_code = firebase_code;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public User(String id, String email, String url_imagen, String firebase_code, String provider, String date_created, String firebaseId, String name, String lastname, String fecha_nac, String lat, String log, String mobile, String estatura, String edad, String peso) {
        this.id = id;
        this.email = email;
        this.url_imagen = url_imagen;
        this.firebase_code = firebase_code;
        this.provider = provider;
        this.date_created = date_created;
        this.firebaseId = firebaseId;
        this.name = name;
        this.lastname = lastname;
        this.fecha_nac = fecha_nac;
        this.lat = lat;
        this.log = log;
        this.mobile = mobile;
        this.estatura = estatura;
        this.edad = edad;
        this.peso = peso;
    }

    public User() {
    }
}
