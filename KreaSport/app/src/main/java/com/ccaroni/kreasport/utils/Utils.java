package com.ccaroni.kreasport.utils;

import com.ccaroni.kreasport.map.views.CustomMapView;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.util.BoundingBox;

/**
 * Created by Master on 01/06/2017.
 */

public class Utils {
    public static double getDownloadSizeForBoundingBox(BoundingBox currentBB, CustomMapView mMapView) {

        CacheManager cacheManager = new CacheManager(mMapView, null);

        int nTiles = cacheManager.possibleTilesInArea(currentBB, mMapView.getZoomLevel(), mMapView.getMaxZoomLevel());
        return 0.001 * (Constants.TILE_KB_SIZE * nTiles);
    }

    public static String getBaseString(String simpleName) {
        return "com.ccaroni.kreasport." + simpleName;
    }
}
