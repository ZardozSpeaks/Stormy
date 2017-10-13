/* Copyright (C) 2017 Acceptto Corporation. All Rights Reserved.
 * This file contains information which is confidential and proprietary.
 * Permission to use, copy, modify and distribute this software is bound
 * by the terms of the official Acceptto user license agreement.
 */

package com.davidremington.stormy.utils;


import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES = "Preferences";

    public static String getSettingsParam(String paramName) {
        SharedPreferences settings = getPreferences();
        return settings.getString(paramName, "");
    }

    private static SharedPreferences getPreferences() {
        return ApplicationContextProvider.getAppContext()
                .getSharedPreferences(PREFERENCES, 0);
    }

    public static void setSettingsParam(String paramName, String paramValue) {
        SharedPreferences settings = getPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(paramName, paramValue);
        editor.apply();
    }
}
