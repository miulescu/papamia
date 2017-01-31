package  ro.softronic.mihai.ro.papamia.POJOs;

import java.util.Comparator;

public class Restaurant implements Comparable<Restaurant> {
    private String name, thumbnailUrl;
    private double rating;
    private String descriere;
    private String rest_id;
    private int ora_deschidere;
    private int ora_inchidere;
    private  Integer open;
    private int timp_livrare;
    private String specialitati;
    private double pret_transport;
    private String phone_no;
    private String email;
    private boolean _has_offers;

    public Restaurant() {
    }

    public Restaurant(String name, String thumbnailUrl, double rating,
                 String descriere, String rest_id,int open) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
        this.descriere = descriere;
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

    public int getOra_deschidere(){return ora_deschidere;}
    public void setOra_deschidere(int ora_deschidere){this.ora_deschidere = ora_deschidere;}

    public int getOra_inchidere(){return ora_inchidere;}
    public void setOra_inchidere(int  ora_inchidere){this.ora_inchidere = ora_inchidere;}

    public Integer getOpen(){return  open;}
    public   void setOpen(Integer open){this.open = open;}

    public int getTimp_livrare(){return  timp_livrare;}
    public   void setTimp_livrare(int timp_livrare){this.timp_livrare = timp_livrare;}

    public String getSpecialitati() {
        return specialitati;
    }
    public void setSpecialitati(String specialitati) {
        this.specialitati = specialitati;
    }

    public double getPret_transport() {
        return pret_transport;
    }
    public void setPret_transport(double pret_transport) {
        this.pret_transport = pret_transport;
    }

    public String getPhone_no() {
        return phone_no;
    }
    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getHas_offers(){return _has_offers;}
    public void setHas_offers(String has_offers){
            _has_offers = has_offers.equals("null") ? false: true;
    }

    @Override
    public int compareTo(Restaurant r) {
        return Comparators.NAME.compare(this, r);
    }

    public static class Comparators {

        public static Comparator<Restaurant> NAME = new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return o1.name.compareTo(o2.name);
            }
        };
        public static Comparator<Restaurant> TIMP_LIVRARE = new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return o1.getTimp_livrare() - o2.getTimp_livrare();
            }
        };
//        public static Comparator<Restaurant> NAMEANDAGE = new Comparator<Restaurant>() {
//            @Override
//            public int compare(Restaurant o1, Restaurant o2) {
//                int i = o1.name.compareTo(o2.name);
//                if (i == 0) {
//                    i = o1.age - o2.age;
//                }
//                return i;
//            }
//        };
    }


}




