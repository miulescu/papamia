package ro.softronic.mihai.ro.papamia.POJOs;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyLinkedHashMap<Integer, Order> extends LinkedHashMap<Integer, Order> {

    // the MountainBike subclass has one constructor
    public MyLinkedHashMap(){
        super();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Order> eldest)
    {
        return this.size() > 1;
    }
}