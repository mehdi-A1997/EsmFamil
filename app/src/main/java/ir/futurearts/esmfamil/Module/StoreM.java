package ir.futurearts.esmfamil.Module;

public class StoreM {
    private String name,sku, price;

    public StoreM(String name, String sku, String price) {
        this.name = name;
        this.sku = sku;
        this.price = price;
    }

    public StoreM() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
