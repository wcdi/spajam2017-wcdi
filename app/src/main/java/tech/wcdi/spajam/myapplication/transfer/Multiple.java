package tech.wcdi.spajam.myapplication.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acq on 6/4/17.
 */

public class Multiple<T> extends ArrayList<T> implements List<T> {

    public boolean add(T object) {
        super.add(object);
        return false;
    }

    public void set(T object) {
        this.clear();
        super.add(object);
    }
}