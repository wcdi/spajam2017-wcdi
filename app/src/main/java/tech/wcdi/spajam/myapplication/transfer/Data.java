package tech.wcdi.spajam.myapplication.transfer;

/**
 * Created by acq on 6/4/17.
 */

import java.io.Serializable;

/**
 * Created by acq on 16/06/06.
 * Title:
 * Author:
 * Memo:
 * Todo:
 */

public class Data implements Serializable {
    public boolean status = false;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}