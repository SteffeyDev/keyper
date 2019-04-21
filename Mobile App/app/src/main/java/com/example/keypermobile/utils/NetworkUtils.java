package com.example.keypermobile.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;

public class NetworkUtils {
    static ClearableCookieJar cookieJar = null;
    static OkHttpClient okHttpClient = null;
    public static ANRequest.PostRequestBuilder injectCookies(ANRequest.PostRequestBuilder builder, Context context) {
        if (cookieJar == null)
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        return builder.setOkHttpClient(okHttpClient);
    }

    public static ANRequest.GetRequestBuilder injectCookies(ANRequest.GetRequestBuilder builder, Context context) {
        if (cookieJar == null)
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        return builder.setOkHttpClient(okHttpClient);
    }

    public static String getApiUrl(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("api_url");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Keyper", "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }
}
