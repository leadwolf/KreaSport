package com.ccaroni.kreasport.data.remote;

import android.support.constraint.solver.widgets.Rectangle;

import com.ccaroni.kreasport.data.model.IDownloadedArea;

/**
 * Created by Master on 09/02/2018.
 */

public class DownloadedArea implements IDownloadedArea {

    private long id;

    private Rectangle boundingBox;

    private String name;
    private String path;

    private double size;
    private String dateDownloaded;
    //    private OffsetDateTime dateTime;

    private int minZoom;

    public DownloadedArea() {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Rectangle getDTOBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String getDateDownloaded() {
        return dateDownloaded;
    }

    public void setDateDownloaded(String dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    @Override
    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }
}
