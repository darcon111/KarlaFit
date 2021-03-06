package app.karlafit.com.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppPreferences {

    private SharedPreferences appPreferences;
    private Editor editor;

    public static final String user = "app.karlafit.com.user";
    public static final String userImagen = "app.karlafit.com.imagen";
    public static final String userId = "app.karlafit.com.userId";
    public static final String firebasetoken="app.karlafit.com.token";
    public static final String flag="app.karlafit.com.flag";
    public static final String language="app.karlafit.com.language";
    public static final String tour = "app.karlafit.com.tour";
    public static final String sound = "app.karlafit.com.sound";
    public static final String vibrate = "app.karlafit.com.vibrate";
    public static final String light = "app.karlafit.com.light";
    public static final String noti = "app.karlafit.com.noti";
    public static final String mensajes = "com.advancehdt.flyers.mensajes";
    public static final String actualizar = "com.advancehdt.flyers.actualizar";


    public  String getFirebasetoken() {
        return appPreferences.getString(firebasetoken, "");
    }

    public AppPreferences(Context context) {

        appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getUser() {
        return appPreferences.getString(user, "");
    }

    public String getImagen() {
        return appPreferences.getString(userImagen, "");
    }

    public String getFlag() {
        return appPreferences.getString(flag, "");
    }

    public String getLanguage() {
        return appPreferences.getString(language, "es");
    }

    public String getTour() {
        return appPreferences.getString(tour, "0");
    }

    public String getUserId() {
        return appPreferences.getString(userId, "0");
    }
    public String getSound() {
        return appPreferences.getString(sound, "0");
    }
    public String getVibrate() {
        return appPreferences.getString(vibrate, "0");
    }
    public String getLight() {
        return appPreferences.getString(light, "0");
    }

    public String getNoti()
    {
        return  appPreferences.getString(noti,"0");
    }

    public String getMensajes()
    {
        return  appPreferences.getString(mensajes,"0");
    }


    public String getActualizar()
    {
        return  appPreferences.getString(actualizar,"0");
    }

    public void setActualizar(String mActualizar) {
        editor = appPreferences.edit();
        editor.putString(actualizar, mActualizar);
        editor.commit();
    }


    public void setUser(String mUser) {
        editor = appPreferences.edit();
        editor.putString(user, mUser);
        editor.commit();
    }

    public void setUserId(String mUserId) {
        editor = appPreferences.edit();
        editor.putString(userId, mUserId);
        editor.commit();
    }


    public void setImagen(String mImagen) {
        editor = appPreferences.edit();
        editor.putString(userImagen, mImagen);
        editor.commit();
    }


    public void setFirebasetoken(String token) {
        editor = appPreferences.edit();
        editor.putString(firebasetoken, token);
        editor.commit();
    }

    public void setFlag(String value) {
        editor = appPreferences.edit();
        editor.putString(flag,value);
        editor.commit();
    }

    public void setLanguage(String value) {
        editor = appPreferences.edit();
        editor.putString(language,value);
        editor.commit();
    }

    public void setTour(String value) {
        editor = appPreferences.edit();
        editor.putString(tour,value);
        editor.commit();
    }

    public void setSound(String value) {
        editor = appPreferences.edit();
        editor.putString(sound,value);
        editor.commit();
    }

    public void setVibrate(String value) {
        editor = appPreferences.edit();
        editor.putString(vibrate,value);
        editor.commit();
    }

    public void setLight(String value) {
        editor = appPreferences.edit();
        editor.putString(light,value);
        editor.commit();
    }

    public  void setNoti(int value)
    {
        editor = appPreferences.edit();
        editor.putString(noti,String.valueOf(value));
        editor.commit();
    }

    public  void setMensajes(int value)
    {
        editor = appPreferences.edit();
        editor.putString(mensajes,String.valueOf(value));
        editor.commit();
    }


}
