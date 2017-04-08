package fr.univ_lille1.iut_info.caronic.kreasport.map.views;

import android.content.Context;

import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

/**
 * Created by Master on 05/04/2017.
 */

public class CustomOverlayWithFocus<Item extends OverlayItem> extends ItemizedOverlayWithFocus<Item> {


    public CustomOverlayWithFocus(Context pContext, List<Item> aList, OnItemGestureListener aOnItemTapListener) {
        super(pContext, aList, aOnItemTapListener);

    }

    @Override
    public boolean addItem(final Item item) {
        return mItemList.add(item);
    }
}
