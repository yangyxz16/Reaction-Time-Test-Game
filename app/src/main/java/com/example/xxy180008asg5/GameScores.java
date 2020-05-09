package com.example.xxy180008asg5;

import java.io.Serializable;
import java.util.Date;


/**
 * GamesScore class has time to finish, number of missed and date,
 * which is serializable passing between activities,
 * and comparable on comparing scores
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class GameScores implements Serializable, Comparable<GameScores> {
    private int time; // Variable for the time to complete the game
    private int missed;
    private String date;

    public GameScores(int time, int missed, String date) {
        this.time = time;
        this.missed = missed;
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public int getMissed() {
        return missed;
    }

    /**
     * Function to convert time from integer to string
     * @return time string
     */
    public String getTimeInString() {
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        return timeString;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(time).append(",")
                .append(missed).append(",")
                .append(date);

        return sb.toString();
    }

    /**
     * Method used to compare GameScores objects on scores
     */
    @Override
    public int compareTo(GameScores gs) {
        if (this.getTime() == gs.getTime()) return 0;
        else return this.getTime() > gs.getTime() ? 1 : -1;
    }
}
