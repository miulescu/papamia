package ro.softronic.mihai.ro.papamia.POJOs;

/**
 * Created by mihai on 11.10.16.
 */

public class Order {
    int _id;
    int _item_id;
    String _item_name;
    int _item_qty;
    double _item_total_price;

    // Empty constructor
    public Order() {

    }

    // constructor
    public Order(int id, int item_id, String item_name, int item_qty, double item_total_price) {
        this._id = id;
        this._item_id = item_id;
        this._item_name = item_name;
        this._item_qty = item_qty;
        this._item_total_price = item_total_price;
    }

    public Order(int item_id, String item_name, int item_qty, double item_total_price) {
        this._item_id = item_id;
        this._item_name = item_name;
        this._item_qty = item_qty;
        this._item_total_price = item_total_price;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getItemID() {
        return this._item_id;
    }

    public void setItemID(int item_id) {
        this._item_id = item_id;
    }

    public String getItemName() {
        return this._item_name;
    }

    public void setItemName(String item_name) {
        this._item_name = item_name;
    }

    public int getItemQTY() {
        return this._item_qty;
    }

    public void setItemQTY(int item_qty) {
        this._item_qty = item_qty;
    }

    public double getItemTotalPrice() {
        return this._item_total_price;
    }

    public void setItemTotalPrice(double item_total_price) {
        this._item_total_price = item_total_price;

    }

    public double getItemPrice(){
        return _item_total_price/_item_qty;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Order) {
            Order o = (Order) obj;
            if (o.getItemQTY() == this.getItemQTY()) {
                return true;
            }
            return false;
        }
        return false;
    }
}
