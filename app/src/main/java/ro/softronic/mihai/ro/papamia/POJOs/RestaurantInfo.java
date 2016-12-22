package  ro.softronic.mihai.ro.papamia.POJOs;

public class RestaurantInfo {
    private String name;
    private double rating;
    private String rest_id;
    private String info_deschis;
    private String info_orar;

    public RestaurantInfo() {
    }

    public RestaurantInfo(String info_deschis, String info_orar, double rating) {
        this.name = name;
        this.info_deschis = info_deschis;
        this.rating = rating;
        this.info_orar = info_orar;
    }

    public String getInfo_orar() {
        return info_orar ;
    }
    public void setInfo_orar(String info_orar) {
        this.info_orar = info_orar;
    }

    public String getInfo_deschis() {
        return info_deschis;
    }
    public void setInfo_deschis(String thumbnailUrl) {
        this.info_deschis = thumbnailUrl;
    }


    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }




}




