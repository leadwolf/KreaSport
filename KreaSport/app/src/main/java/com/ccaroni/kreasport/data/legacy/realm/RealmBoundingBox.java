package com.ccaroni.kreasport.data.legacy.realm;

import org.osmdroid.util.BoundingBox;

import io.realm.RealmObject;

/**
 * Created by Master on 02/06/2017.
 */

public class RealmBoundingBox extends RealmObject {

    private double mLatNorth;
    private double mLatSouth;
    private double mLonEast;
    private double mLonWest;

    public RealmBoundingBox() {
    }

    public double getmLatNorth() {
        return mLatNorth;
    }

    public RealmBoundingBox setmLatNorth(double mLatNorth) {
        this.mLatNorth = mLatNorth;
        return this;
    }

    public double getmLatSouth() {
        return mLatSouth;
    }

    public RealmBoundingBox setmLatSouth(double mLatSouth) {
        this.mLatSouth = mLatSouth;
        return this;
    }

    public double getmLonEast() {
        return mLonEast;
    }

    public RealmBoundingBox setmLonEast(double mLonEast) {
        this.mLonEast = mLonEast;
        return this;
    }

    public double getmLonWest() {
        return mLonWest;
    }

    public RealmBoundingBox setmLonWest(double mLonWest) {
        this.mLonWest = mLonWest;
        return this;
    }

    public void importFromBoundingBox(BoundingBox currentBB) {
        this.mLatNorth = currentBB.getLatNorth();
        this.mLatSouth = currentBB.getLatSouth();
        this.mLonEast = currentBB.getLonEast();
        this.mLonWest = currentBB.getLonWest();
    }

    public BoundingBox toBareBoundingBox() {
        return new BoundingBox(mLatNorth, mLonEast, mLatSouth, mLonWest);
    }
}
