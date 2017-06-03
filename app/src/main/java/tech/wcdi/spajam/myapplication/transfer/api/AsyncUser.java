package tech.wcdi.spajam.myapplication.transfer.api;

import tech.wcdi.spajam.myapplication.transfer.AsyncSender;
import tech.wcdi.spajam.myapplication.transfer.Data;

/**
 * Created by acq on 6/4/17.
 */


public class AsyncUser extends AsyncSender.Sender {

    public AsyncUser(Data data) {
        allData = data;
        super.setPath("/useradd");
    }
}