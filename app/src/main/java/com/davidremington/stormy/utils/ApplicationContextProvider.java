package com.davidremington.stormy.utils;


import android.content.Context;

public class ApplicationContextProvider {

    /**
     * Keeps a reference of the application context
     */
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getAppContext() {
        return mContext;
    }
}
