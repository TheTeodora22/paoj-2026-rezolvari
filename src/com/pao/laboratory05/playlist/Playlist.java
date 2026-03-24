package com.pao.laboratory05.playlist;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    Playlist(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }
    void addSong(Song song) {
        Song[] newSongs = new Song[songs.length + 1];
        System.arraycopy(songs, 0, newSongs, 0, songs.length);
        newSongs[songs.length] = song;
        songs = newSongs;
    }
    void printSortedByTitle() {
        Song[] sortedSongs = songs.clone();
        java.util.Arrays.sort(sortedSongs);
        for (Song song : sortedSongs) {
            System.out.println(song);
        }
    }
    void printSortedByDuration() {
        Song[] sortedSongs = songs.clone();
        java.util.Arrays.sort(sortedSongs, new SongDurationComparator());
        for (Song song : sortedSongs) {
            System.out.println(song);
        }
    }
    int getTotalDuration() {
        int total = 0;
        for (Song song : songs) {
            total += song.durationSeconds();
        }
        return total;
    }
}
