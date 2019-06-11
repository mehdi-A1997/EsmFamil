package ir.futurearts.esmfamil.module;

import java.io.Serializable;

public class GameM implements Serializable {
    private int id, uid, oid, type, status, uscore, oscore,utime, otime;
    private String timestamp, letter;
    private ItemsM items;

    public GameM(int id, int uid, int oid, int type, int status, int uscore,
                 int oscore, int utime, int otime, String letter, String timestamp, ItemsM items) {
        this.id = id;
        this.uid = uid;
        this.oid = oid;
        this.type = type;
        this.status = status;
        this.uscore = uscore;
        this.oscore = oscore;
        this.utime = utime;
        this.otime = otime;
        this.letter = letter;
        this.timestamp = timestamp;
        this.items = items;
    }

    public GameM() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUscore() {
        return uscore;
    }

    public void setUscore(int uscore) {
        this.uscore = uscore;
    }

    public int getOscore() {
        return oscore;
    }

    public void setOscore(int oscore) {
        this.oscore = oscore;
    }

    public int getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }

    public int getOtime() {
        return otime;
    }

    public void setOtime(int otime) {
        this.otime = otime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ItemsM getItems() {
        return items;
    }

    public void setItems(ItemsM items) {
        this.items = items;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
