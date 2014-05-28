package com.androidsx.rateme;

public class CustomShowDialog {
    
private static final String TAG = CustomShowDialog.class.getSimpleName();
    
    private static final String PREF_NAME = "RateThisApp";
    private static final String KEY_INSTALL_DATE = "rta_install_date";
    private static final String KEY_LAUNCH_TIMES = "rta_launch_times";
    private static final String KEY_OPT_OUT = "rta_opt_out";
    
    private static Date mInstallDate = new Date();
    private static int mLaunchTimes = 0;
    private static boolean mOptOut = false;
    
    /**
     * Days after installation until showing rate dialog
     */
    public static final int INSTALL_DAYS = 7;
    /**
     * App launching times until showing rate dialog
     */
    public static final int LAUNCH_TIMES = 5;
    
    /**
     * If true, print LogCat
     */
    public static final boolean DEBUG = false;

}
