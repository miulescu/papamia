package  ro.softronic.mihai.ro.papamia.POJOs;

public class NavItem {
    private String name;
    private boolean selected;
    private  int img_resource;



    public NavItem() {
    }

    public NavItem(String name, boolean selected, int img_resource ) {
        this.name = name;
        this.selected = selected;
        this.img_resource = img_resource;
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

    public int getImg_resource(){return  img_resource;}
    public void setImg_resource(int img_resource){this.img_resource = img_resource;}
}




