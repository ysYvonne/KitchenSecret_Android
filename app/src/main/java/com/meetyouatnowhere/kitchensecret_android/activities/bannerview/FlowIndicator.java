package com.meetyouatnowhere.kitchensecret_android.activities.bannerview;

/**
 * Created by Charlene on 2016/7/6.
 */

import com.meetyouatnowhere.kitchensecret_android.activities.bannerview.ViewFlow.*;

public interface FlowIndicator extends ViewSwitchListener {

    /**
     * Set the current ViewFlow. This method is called by the ViewFlow when the
     * FlowIndicator is attached to it.
     *
     * @param view
     */
    public void setViewFlow(ViewFlow view);

    /**
     * The scroll position has been changed. A FlowIndicator may implement this
     * method to reflect the current position
     *
     * @param h
     * @param v
     * @param oldh
     * @param oldv
     */
    public void onScrolled(int h, int v, int oldh, int oldv);
}

