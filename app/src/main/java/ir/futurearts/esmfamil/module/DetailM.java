package ir.futurearts.esmfamil.module;

public class DetailM {
    private String title, utext, uscore, otext, oscore;

    public DetailM(String title, String utext, String uscore, String otext, String oscore) {
        this.title = title;
        this.utext = utext;
        this.uscore = uscore;
        this.otext = otext;
        this.oscore = oscore;
    }

    public DetailM() {
    }

    public String getUtext() {
        return utext;
    }

    public void setUtext(String utext) {
        this.utext = utext;
    }

    public String getUscore() {
        return uscore;
    }

    public void setUscore(String uscore) {
        this.uscore = uscore;
    }

    public String getOtext() {
        return otext;
    }

    public void setOtext(String otext) {
        this.otext = otext;
    }

    public String getOscore() {
        return oscore;
    }

    public void setOscore(String oscore) {
        this.oscore = oscore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
