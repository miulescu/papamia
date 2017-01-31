package ro.softronic.mihai.ro.papamia.POJOs;

/**
 * Created by mihai on 11.10.16.
 */

public class Offer {
    int _id;
    int _offer_id;
    String _subiect_oferta_name;
    int _subiect_item_qty;
    String _de_oferit_item_name;
    int _de_oferit_item_id;


    // Empty constructor
    public Offer() {

    }

    // constructor
    public Offer(int id, int offer_id,  String subiect_oferta_name, int subiect_item_qty, int de_oferit_item_id, String de_oferit_item_name) {
        this._id = id;
        this._offer_id = offer_id;
        this._subiect_oferta_name = subiect_oferta_name;
        this._subiect_item_qty = subiect_item_qty;
        this._de_oferit_item_id = de_oferit_item_id;
        this._de_oferit_item_name = de_oferit_item_name;
    }

    public Offer(int offer_id,  String subiect_oferta_name, int subiect_item_qty , int de_oferit_item_id, String de_oferit_item_name) {
        this._offer_id = offer_id;
        this._subiect_oferta_name = subiect_oferta_name;
        this._subiect_item_qty = subiect_item_qty;
        this._de_oferit_item_id = de_oferit_item_id;
        this._de_oferit_item_name = de_oferit_item_name;
    }
    //Id - ul bazei de date
    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }
    // Index
    public int getOfferId() {
        return this._offer_id;
    }
    public void setOfferId(int offerId) {
        this._offer_id = offerId;
    }

    //cum se numeste ce primesti la oferta: Ex: primesti o bere la o pizza
    public void set_subiect_oferta_name(String _subiect_oferta_name){this._subiect_oferta_name = _subiect_oferta_name;}
    public String get_subiect_oferta_name(){
        return _subiect_oferta_name;
    }

    //ce se ofera la oferta de ex: o bautura la o pizza
    public void set_de_oferit_item_name(String de_oferit_item_name){ this._de_oferit_item_name = de_oferit_item_name;}
    public String get_de_oferit_item_name() {
        return this._de_oferit_item_name;
    }

    //la cate itemuri se declaseaza oferta. de ex: la 2 pizza primesti un gratis
    public void set_subiect_item_qty(int subiect_item_qty) { this._subiect_item_qty = subiect_item_qty;}
    public int get_subiect_item_qty(){
        return _subiect_item_qty;
    }

    //Id-ul itemului care se primeste ca oferta
    public  void  set_de_oferit_item_id(int de_oferit_item_id){this._de_oferit_item_id = de_oferit_item_id;}
    public int get_de_oferit_item_id( ){ return _de_oferit_item_id;};

}
