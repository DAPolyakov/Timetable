package ru.dmpolyakov.timetable.common;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import ru.dmpolyakov.timetable.main.MainActivity;

public class Drawer {
    private DrawerLayout mDrawer;

    public Drawer(MainActivity mainActivity, DrawerLayout drawerLayout) {
        mDrawer = drawerLayout;
        initViews(mainActivity);
    }

    public void setListener(DrawerLayout.SimpleDrawerListener listener) {
        mDrawer.addDrawerListener(listener);
    }


    private void initViews(MainActivity activity) {

    }

    public boolean closeDrawerIfOpened() {
        int gravity = GravityCompat.START;
        if (mDrawer.isDrawerOpen(gravity)) {
            mDrawer.closeDrawer(gravity);
            return false;
        }
        return true;
    }

    public boolean openDrawerIfClosed() {
        int gravity = GravityCompat.START;
        if (!mDrawer.isDrawerOpen(gravity)) {
            mDrawer.openDrawer(gravity);
            return false;
        }
        return true;
    }

}
