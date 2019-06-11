package ir.futurearts.esmfamil.module;


public class UserM {
    private String id, name, username, email, score, img, coin, token;
    private int online;

    public UserM(String id, String name, String username, String email, String score, String img, int online, String coin, String token) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.score = score;
        this.img = img;
        this.online = online;
        this.coin = coin;
        this.token = token;
    }

    public UserM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
