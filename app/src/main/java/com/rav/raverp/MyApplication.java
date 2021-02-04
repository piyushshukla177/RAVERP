package com.rav.raverp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rav.raverp.data.local.prefs.PrefsHelper;
import com.rav.raverp.data.model.api.FollowUpListModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.utils.AppConstants;


public class MyApplication extends Application implements LifecycleObserver {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance ;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public static Context getAppContext() {
        return context;
    }

    public static boolean getAppOpenStatus() {


        return PrefsHelper.getBoolean(context, AppConstants.APP_FOREGROUNDED);
    }

    public static String getLoginId() {
        String loggedInUserDetails = PrefsHelper.getString(context, AppConstants.LOGIN);
        if (loggedInUserDetails != null) {
            Gson gson=new Gson();
            LoginModel loggedInUser=gson.fromJson(loggedInUserDetails, LoginModel.class);

                return loggedInUser.getStrLoginID();
        }
        return null;
    }
    public static LoginModel getLoginModel() {
        String loggedInUserDetails = PrefsHelper.getString(context, AppConstants.LOGIN);
        if (loggedInUserDetails != null) {
            Gson gson=new Gson();
            LoginModel loggedInUser=gson.fromJson(loggedInUserDetails, LoginModel.class);

            return loggedInUser;
        }

        return null;
    }

    public static FollowUpListModel getFollowUpModelList() {
        String FollowUpModelDetails = PrefsHelper.getString(context, AppConstants.GetAllLeadFollowUP);
        if (FollowUpModelDetails != null) {
            Gson gson=new Gson();
            FollowUpListModel FollowUpListModel=gson.fromJson(FollowUpModelDetails, FollowUpListModel.class);

            return FollowUpListModel;
        }

        return null;
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        // App in background
        PrefsHelper.putBoolean(context, AppConstants.APP_FOREGROUNDED, false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        PrefsHelper.putBoolean(context, AppConstants.APP_FOREGROUNDED, true);
    }
      //198159 operator 123
     // 539907 associate 123456
    // RAVC104703 Customer 410885


}
