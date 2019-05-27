package ir.futurearts.esmfamil.Module;

import java.io.Serializable;

public class ItemsM implements Serializable {
    private String name, family, city, country
            , food, animal, color, flower, fruit, object;

    public ItemsM(String name, String family, String city, String country, String food, String animal,
                  String color, String flower, String fruit, String object) {
        this.name = name;
        this.family = family;
        this.city = city;
        this.country = country;
        this.food = food;
        this.animal = animal;
        this.color = color;
        this.flower = flower;
        this.fruit = fruit;
        this.object = object;
    }

    public ItemsM() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
