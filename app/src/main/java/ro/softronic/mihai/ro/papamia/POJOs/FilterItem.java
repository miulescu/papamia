package  ro.softronic.mihai.ro.papamia.POJOs;

public class FilterItem {
    private String name;
    private boolean selected;



    public FilterItem() {
    }

    public FilterItem(String name, boolean selected ) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean getSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}




