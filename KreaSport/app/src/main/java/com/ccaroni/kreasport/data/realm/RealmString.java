package com.ccaroni.kreasport.data.realm;

import io.realm.RealmObject;

/**
 * Created by Master on 19/05/2017.
 */

public class RealmString extends RealmObject {

    private String string;

    public RealmString() {
    }

    public RealmString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
