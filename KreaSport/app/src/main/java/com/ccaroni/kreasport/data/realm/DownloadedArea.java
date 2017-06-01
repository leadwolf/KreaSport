package com.ccaroni.kreasport.data.realm;

import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by Master on 02/06/2017.
 */

public class DownloadedArea extends RealmObject {

    private String id;
    private String name;
    private String path;
    private double size;
    private String dateDownloaded;
//    private OffsetDateTime dateTime;


    public DownloadedArea() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public DownloadedArea setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DownloadedArea setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public DownloadedArea setPath(String path) {
        this.path = path;
        return this;
    }

    public double getSize() {
        return size;
    }

    public DownloadedArea setSize(double size) {
        this.size = size;
        return this;
    }

    public String getDateDownloaded() {
        return dateDownloaded;
    }

    public DownloadedArea setDateDownloaded(String dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
        return this;
    }
}
