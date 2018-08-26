package ro.stefanhalus.android.blindtransport.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import ro.stefanhalus.android.blindtransport.R;
import ro.stefanhalus.android.blindtransport.WaitingActivity;

public class PlayBackUtil {
//    private Context context = WaitingActivity.context;

    public static void play() {
        MediaPlayer mMediaPlayer;
        mMediaPlayer = MediaPlayer.create(WaitingActivity.context, R.raw.l35);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }
}
