package project.senior.holdit.model;

public class Bank {
    private String name;
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Bank(String name, String logo) {

        this.name = name;
        this.logo = logo;
    }
}
