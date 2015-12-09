package com.newproject.jhull3341.trackiteq;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by jhull3341 on 12/7/2015.
 */
public class SoundPlayer {

    public static final int S1 = R.raw.bronzebell2;

    private static SoundPool soundPool;
    private static HashMap soundPoolMap;
    /** Populate the SoundPool*/
//    public static void initSounds(Context context) {
//        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
//        soundPoolMap = new HashMap(3);
//        soundPoolMap.put( S1, soundPool.load(context, R.raw.bronzebell2, 1) );
//
//    }
    /** Play a given sound in the soundPool */
//    public static void playSound(Context context, int soundID) {
//        if(soundPool == null || soundPoolMap == null){
//            initSounds(context);
//        }
//        float volume = Float.parseFloat("0.75"); // whatever in the range = 0.0 to 1.0
//        // play sound with same right and left volume, with a priority of 1,
//        // zero repeats (i.e play once), and a playback rate of 1f
//        //soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
//        MediaPlayer mp = MediaPlayer.create(context,soundID);
//        mp.setVolume((float)10.75,(float)10.75);
//        mp.start();
//
//    }

    public static void initSounds(Context context) {

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);

        soundPoolMap = new HashMap<Integer, Integer>();

        soundPoolMap.put(S1, soundPool.load(context, R.raw.bronzebell2, 1));

    }

    public static void playSound(Context context, int sound) {

        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

        soundPool.play( sound, streamVolume, streamVolume, 1, 0, 1f);

    }

}
