package com.ccaroni.kreasport.data;

import android.support.constraint.solver.widgets.Rectangle;

/**
 * Created by Master on 09/02/2018.
 */

public interface IDownloadedArea {
    void setId(String id);

    void setBoundingBox(Rectangle boundingBox);

    void setName(String name);

    void setPath(String path);

    void setSize(double size);

    void setDateDownloaded(String dateDownloaded);

    void setMinZoom(int minZoom);
}
