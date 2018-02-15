package com.ccaroni.kreasport.data.model;

import android.support.constraint.solver.widgets.Rectangle;

/**
 * Created by Master on 10/02/2018.
 */

public interface IDownloadedArea {
    long getId();

    Rectangle getDTOBoundingBox();

    String getName();

    String getPath();

    double getSize();

    String getDateDownloaded();

    int getMinZoom();
}
