package com.ccaroni.kreasport.data.dto;

import android.support.constraint.solver.widgets.Rectangle;

import com.ccaroni.kreasport.data.IDownloadedArea;

/**
 * Created by Master on 09/02/2018.
 */

public class DownloadedArea implements IDownloadedArea {

    private String id;

    private Rectangle boundingBox;

    private String name;
    private String path;

    private double size;
    private String dateDownloaded;
    //    private OffsetDateTime dateTime;

    private int minZoom;

    public DownloadedArea() {
    }

    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    @Override
    public void setSize(double size) {
        this.size = size;
    }

    public String getDateDownloaded() {
        return dateDownloaded;
    }

    @Override
    public void setDateDownloaded(String dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    public int getMinZoom() {
        return minZoom;
    }

    @Override
    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }
}
