package ro.stefanhalus.android.blindtransport.Utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrateUtil {

    public static long[] patternBusMorse = new long[]{20, 400, 50, 200, 50, 200, 50, 200, 300, 200, 50, 200, 50, 400, 300, 200, 50, 200, 50, 200, 500};
    /**
     * Morse code saying bus
     */
    public static void busNotification(Context mContext) {
        long[] mVibratePattern = new long[]{20, 400, 50, 200, 50, 200, 50, 200, 300, 200, 50, 200, 50, 400, 300, 200, 50, 200, 50, 200, 500};
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        assert vibrator != null;
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500); // for 500 ms
        }
        vibrator.vibrate(mVibratePattern, -1);
    }

    public void checked(Context mContext){
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        assert vibrator != null;
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500); // for 500 ms
        }
        vibrator.vibrate(50);
    }
}
