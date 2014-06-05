package com.androidsx.rateme;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class RateMeDialogTimer {

    private static final String TAG = RateMeDialogTimer.class.getSimpleName();

    private static final String PREF_NAME = "RateThisApp";
    private static final String KEY_INSTALL_DATE = "rta_install_date";
    private static final String KEY_LAUNCH_TIMES = "rta_launch_times";
    private static final String KEY_OPT_OUT = "rta_opt_out";
    private static int installDays;
    private static int launchTimes;

    private static Date mInstallDate = new Date();
    private static int mLaunchTimes = 0;
    private static boolean mOptOut = false;

    /**
     * class constructor
     * 
     * @param installDate, launchTimes
     */
    public RateMeDialogTimer(int installDate, int launchTimes) {
        this.installDays = installDate;
        this.launchTimes = launchTimes;

    }

    public static void onStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        // If it is the first launch, save the date in shared preference.
        if (pref.getLong(KEY_INSTALL_DATE, 0) == 0L) {
            Date now = new Date();
            editor.putLong(KEY_INSTALL_DATE, now.getTime());
            Log.d(TAG, "First install: " + now.toString());
        }
        // Increment launch times
        int launchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0);
        launchTimes++;
        editor.putInt(KEY_LAUNCH_TIMES, launchTimes);
        Log.d(TAG, "Launch times; " + launchTimes);

        editor.commit();

        mInstallDate = new Date(pref.getLong(KEY_INSTALL_DATE, 0));
        mLaunchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0);
        mOptOut = pref.getBoolean(KEY_OPT_OUT, false);

    }

    /**
     * Check whether the rate dialog shoule be shown or not
     * 
     * @return
     */
    public static boolean shouldShowRateDialog(final Context context) {
        if (mOptOut) {
            return false;
        } else {
            if (mLaunchTimes >= launchTimes) {
                clearSharedPreferences(context);
                return true;
            }
            long threshold = installDays * 24 * 60 * 60 * 1000L; // msec
            if (new Date().getTime() - mInstallDate.getTime() >= threshold) {
                clearSharedPreferences(context);
                return true;
            }
            return false;
        }
    }

    /**
     * Clear data in shared preferences
     * 
     * @param context
     */
    public static void clearSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.remove(KEY_INSTALL_DATE);
        editor.remove(KEY_LAUNCH_TIMES);
        editor.commit();
    }

    /**
     * Set opt out flag. If it is true, the rate dialog will never shown unless app data is cleared.
     * 
     * @param context
     * @param optOut
     */
    public static void setOptOut(final Context context, boolean optOut) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_OPT_OUT, optOut);
        editor.commit();
    }

}
