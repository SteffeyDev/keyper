package com.example.keypermobile.utils;

import android.content.Context;

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
}
