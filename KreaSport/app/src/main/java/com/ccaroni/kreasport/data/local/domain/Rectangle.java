package com.ccaroni.kreasport.data.local.domain;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Master on 10/02/2018.
 */
@Entity
public class Rectangle {

    @Id
    private long id;

    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
