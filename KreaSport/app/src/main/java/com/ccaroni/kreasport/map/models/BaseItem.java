package com.ccaroni.kreasport.map.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ccaroni.kreasport.BR;

/**
 * Created by Master on 05/04/2017.
 */

public abstract class BaseItem extends BaseObservable {

    int id;
    String title;
    String description;

    public BaseItem() {
    }

    public BaseItem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
}
