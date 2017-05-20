package com.ccaroni.kreasport.data.dto;

import android.databinding.BaseObservable;

import com.ccaroni.kreasport.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Master on 05/04/2017.
 */

public abstract class BaseItem extends BaseObservable {

    String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public BaseItem() {
    }

    public BaseItem(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public BaseItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BaseItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BaseItem setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public BaseItem setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public BaseItem setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
