package com.ccaroni.kreasport.data.secondary.realm.impl;

import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by Master on 02/06/2017.
 */

public class DownloadedArea extends RealmObject {

    private String id;

    private RealmBoundingBox boundingBox;

    private String name;
    private String path;
    private double size;
    private String dateDownloaded;
    //    private OffsetDateTime dateTime;

    private int minZoom;

    private boolean ongoing;
    private double progress;


    public DownloadedArea() {
        id = UUID.randomUUID().toString();
        ongoing = true;
        progress = 0;
    }

    public String getId() {
        return id;
    }

    public DownloadedArea setId(String id) {
        this.id = id;
        return this;
    }

    public RealmBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public DownloadedArea setBoundingBox(RealmBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
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

    public int getMinZoom() {
        return minZoom;
    }

    public DownloadedArea setMinZoom(int minZoom) {
        this.minZoom = minZoom;
        return this;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public DownloadedArea setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
        return this;
    }

    public double getProgress() {
        return progress;
    }

    public DownloadedArea setProgress(double progress) {
        this.progress = progress;
        return this;
    }
}
