package a2018.warhammer.alex.warhammerbattlesimulator.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class MusiqueIntentService extends IntentService {

    public static final String ACTION_START = "a2018.warhammer.alex.warhammerbattlesimulator.action.MUSIC_START";
    public static final String ACTION_STOP = "a2018.warhammer.alex.warhammerbattlesimulator.action.MUSIC_STOP";
    public static final String ACTION_PAUSE = "a2018.warhammer.alex.warhammerbattlesimulator.action.MUSIC_PAUSE";
    public static final String ACTION_RESUME = "a2018.warhammer.alex.warhammerbattlesimulator.action.MUSIC_RESUME";
    public static final String ACTION_BOUTON = "a2018.warhammer.alex.warhammerbattlesimulator.action.MUSIC_BOUTON";

    private static MediaPlayer mediaplayer;

    private static void SendIntentService(Context context, String action) {
        Intent intent = new Intent(context, MusiqueIntentService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public static void start(Context context) {
        SendIntentService(context, ACTION_START);
    }

    public static void pressed(Context context) {
        SendIntentService(context, ACTION_BOUTON);
    }

    public MusiqueIntentService() {
        super("MusiqueIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) return;

        final String music = intent.getAction();

        switch (music) {
            case ACTION_START:
                handleStartMusic();
                break;
            case ACTION_STOP:
                handleStopMusic();
                break;
            case ACTION_PAUSE:
                handlePauseMusic();
                break;
            case ACTION_RESUME:
                handleResumeMusic();
                break;
            case ACTION_BOUTON:
                handleButtonPressed();
                break;

        }
    }

    private void handleStartMusic() {
        mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.fondsonore);
        mediaplayer.setLooping(true);
        mediaplayer.start();
    }

    private void handleStopMusic() {
        if (mediaplayer == null) return;
        mediaplayer.stop();
        mediaplayer.seekTo(0);
    }

    private void handlePauseMusic() {
        if (mediaplayer == null) return;
        mediaplayer.pause();
    }

    private void handleResumeMusic() {
        if (mediaplayer == null) return;
        mediaplayer.start();
    }

    private void handleButtonPressed() {
        mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.bouton);
        if (mediaplayer == null) return;
        mediaplayer.start();
    }
}
