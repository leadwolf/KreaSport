package com.ccaroni.kreasport.data.local;


import com.ccaroni.kreasport.data.Converter;
import com.ccaroni.kreasport.data.model.IDownloadedArea;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Master on 10/02/2018.
 */
@Entity
public class DownloadedArea implements IDownloadedArea {

    @Id
    private long id;


    private ToOne<Rectangle> boundingBox;

    private String name;
    private String path;

    private double size;
    private String dateDownloaded;
    //    private OffsetDateTime dateTime;

    private int minZoom;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public android.support.constraint.solver.widgets.Rectangle getDTOBoundingBox() {
        return Converter.daoRectangleToDTO(boundingBox.getTarget());
    }

    public void setBoundingBoxFromDTO(Rectangle boundingBox) {
        this.boundingBox.setTarget(boundingBox);
    }

    public ToOne<Rectangle> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(ToOne<Rectangle> boundingBox) {
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
