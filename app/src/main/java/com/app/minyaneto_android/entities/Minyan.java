package com.app.minyaneto_android.entities;

import java.util.Date;

/**
 * Created by User on 15/08/2017.
 */

public class Minyan {
    private String id;
    private String type;
    private Time time;

    public Minyan(String type, Time time) {
        this.type = type;
        this.time = time;
    }



}
