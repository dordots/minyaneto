package com.app.minyaneto_android.models.client;

/**
 * Created by User on 19/10/2017.
 */

public class ModelObject {
    String name;
    int val;
    boolean status;
    double f;

    public ModelObject(String name, int val,
                       boolean status, double f) {
        super();
        this.name = name;
        this.val = val;
        this.status = status;
        this.f = f;
    }

    @Override
    public String toString() {
        return "ModelObject [name=" + name + ", " +
                "val=" + val + ", status="
                + status + ", f=" + f + "]";
    }

}