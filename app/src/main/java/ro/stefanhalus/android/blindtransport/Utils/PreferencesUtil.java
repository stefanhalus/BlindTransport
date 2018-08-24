package ro.stefanhalus.android.blindtransport.Utils;

import android.content.Context;
import android.content.SharedPreferences;

class PreferencesUtil {

    private static String PREFS_FILE_NAME = "blind_transport_preferences";
    private static int MODE_PRIVATE;

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }

    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true);
    }
}
