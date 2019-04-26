package ir.futurearts.esmfamil.Module;

import android.graphics.Bitmap;

import com.parse.ParseFile;

public class UserM {
    private String Id,Name,Username,Email,Score;
    private ParseFile Img=null;
    private int online;

    public UserM(String id, String name, String username, String email, String score) {
        Id = id;
        Name = name;
        Username = username;
        Email = email;
        Score = score;
    }


    public UserM() {
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public ParseFile getImg() {
        return Img;
    }

    public void setImg(ParseFile img) {
        Img = img;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
