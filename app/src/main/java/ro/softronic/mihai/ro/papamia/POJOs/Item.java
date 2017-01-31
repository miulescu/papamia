package  ro.softronic.mihai.ro.papamia.POJOs;

public class Item {
    private String name, thumbnailUrl;
    private double rating;
    private String descriere;
    private String item_id, rest_id;
    private Double pret;
    private boolean extras;


    public Item() {
    }

    public Item(String name, String thumbnailUrl, double rating,
                      String descriere, String item_id, Double pret, String rest_id) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
        this.descriere = descriere;
        this.item_id = item_id;
        this.pret = pret;
        this.rest_id = rest_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescriere() {
        return descriere;
    }
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getRestID() {
        return rest_id;
    }
    public void setRestID(String rest_id) {
        this.rest_id = rest_id;
    }

    public Double getPrice() {
        return pret;
    }
    public void setPrice(Double pret) {
        this.pret = pret;
    }

    public String getItemID() {
        return item_id;
    }
    public void setItemID(String item_id) {
        this.item_id = item_id;
    }

    public void setExtras(boolean extras){
        this.extras = extras;
    }
    public boolean isExtras(){
        return  extras;
    }



}




