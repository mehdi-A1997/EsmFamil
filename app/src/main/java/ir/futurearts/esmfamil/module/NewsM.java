package ir.futurearts.esmfamil.module;

public class NewsM {
    private String title, description;
    private int id, type;

    public NewsM( int id,String title, String description, int type) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.type = type;
    }

    public NewsM() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
