package com.example.crazy_roads.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer soundPlayer;

    public static void playBackgroundMusic() {
        if (mediaPlayer == null) {
            String musicPath = MusicManager.class.getResource("/audios/music.mp3").toExternalForm();
            Media media = new Media(musicPath);
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // boucle infinie
            mediaPlayer.setVolume(Settings.getInstance().getMusicVolume());
        }
        mediaPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static void pauseBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resumeBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public static void updateVolume() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Settings.getInstance().getMusicVolume());
        }
    }

    public static void playSound() {
        try {
            String soundPath = MusicManager.class.getResource("/audios/crash.mp3").toExternalForm();
            Media media = new Media(soundPath);
            soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(Settings.getInstance().getSoundVolume());
            soundPlayer.play();
            soundPlayer.setOnEndOfMedia(() -> soundPlayer.dispose());
        } catch (Exception e) {
        }
    }
}

