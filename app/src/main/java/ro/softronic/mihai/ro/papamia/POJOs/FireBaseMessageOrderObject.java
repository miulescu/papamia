package ro.softronic.mihai.ro.papamia.POJOs;

/**
 * Created by mihai on 11.10.16.
 */

public class FireBaseMessageOrderObject {
    String restaurant_nume;
    String restaurant_phone;
    String user_login;
    String user_phone;
    String order_body;
    String order_price;
    String timp_livrare;

    // Empty constructor
    public FireBaseMessageOrderObject() {

    }

    // constructor
    public FireBaseMessageOrderObject(String restaurant_nume,
                                      String restaurant_phone,
                                      String user_login,
                                      String user_phone,
                                      String order_body,
                                      String order_price,
                                      String timp_livrare) {
        this.restaurant_nume = restaurant_nume;
        this.restaurant_phone = restaurant_phone;
        this.user_login = user_login;
        this.user_phone = user_phone;
        this.order_body = order_body;
        this.order_price = order_price;
        this.timp_livrare = timp_livrare;
    }


    public String getRestaurant_nume() {
        return this.restaurant_nume;
    }

    public void setRestaurant_nume(String restaurant_nume ) {
        this.restaurant_nume = restaurant_nume;
    }

    public String getRestaurant_phone() {
        return this.restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getUser_login() {
        return this.user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getOrder_body() {
        return this.order_body;
    }

    public void setOrder_body(String order_body) {
        this.order_body = order_body;
    }

    public String getOrder_price(){
        return order_price;
    }

    public void setOrder_price(String order_price){
        this.order_price = order_price;
    }

    public String getTimp_livrare(){
        return this.order_price;
    }

    public void setTimp_livrare(String timp_livrare){
        this.timp_livrare = timp_livrare;
    }
}
