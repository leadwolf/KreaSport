package com.ccaroni.kreasport.data.realm;

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

}
