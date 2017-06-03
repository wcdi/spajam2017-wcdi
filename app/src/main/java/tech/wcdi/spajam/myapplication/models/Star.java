package tech.wcdi.spajam.myapplication.models;

/**
 * Created by acq on 6/4/17.
 */

public class Star {
    int id;
    String constellation;
    String name;
    int ra;
    int dec;
    int light_class;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRa() {
        return ra;
    }

    public void setRa(int ra) {
        this.ra = ra;
    }

    public int getDec() {
        return dec;
    }

    public void setDec(int dec) {
        this.dec = dec;
    }

    public int getLight_class() {
        return light_class;
    }

    public void setLight_class(int light_class) {
        this.light_class = light_class;
    }
}
