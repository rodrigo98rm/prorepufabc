package mayer.rodrigo.prorepufabc.Model;

public class User {

    private String name, imgUrl;

    public User(){

    }

    public User(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
