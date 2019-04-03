package com.rockbass2560.megacode.models.database;

public class Score implements Comparable<Score> {
    public String nombre;
    public int score;
    public String id;

    @Override
    public int compareTo(Score o) {
        return o.score - this.score;
    }
}
