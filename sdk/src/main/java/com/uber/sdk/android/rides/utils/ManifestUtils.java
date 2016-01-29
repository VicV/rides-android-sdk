package com.uber.sdk.android.rides.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by V on 1/29/2016.
 */
public class ManifestUtils {

    public static String getManifestData(Context c, String name) {

        String data = null;
        try {
            ApplicationInfo ai = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = ai.metaData;
            data = metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
