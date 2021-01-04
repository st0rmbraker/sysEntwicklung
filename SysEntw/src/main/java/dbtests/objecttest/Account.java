package dbtests.objecttest;

public class Account {

    private String name;
    private String surname;
    private Adress adress;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Adress getAdress() {
        return adress;
    }

    public void printName() {
        System.out.println(this.name);
    }
}
