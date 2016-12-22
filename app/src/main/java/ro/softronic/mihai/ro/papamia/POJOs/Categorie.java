package  ro.softronic.mihai.ro.papamia.POJOs;

public class Categorie {
    private String cat_name;
    private int cat_id;

    public Categorie() {
    }

    public Categorie(String cat_name){
        this.cat_name = cat_name;
    }

    public String getName() {
        return cat_name;
    }

    public void setName(String name) {
        this.cat_name = name;
    }
    public int getCatID() {
        return cat_id;
    }

    public void setCatID(int cat_id) {
        this.cat_id = cat_id;
    }

}




