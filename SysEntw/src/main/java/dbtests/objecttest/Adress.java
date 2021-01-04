package dbtests.objecttest;

public class Adress {

    private String name;
    private City city;
    private String string;
    private String street;

    public Adress(String string, City city, String street)
    {
        this.street =street;
        this.city = city;
        this.string = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Adress residence) {

    }
}
