package com.lin.calc;

public class SingleRank {
    int difficulty;
    int level;
    float acc;
    float ratio;

    public int getDifficulty() {
        return difficulty;
    }

    public int getLevel() {
        return level;
    }

    public float getAcc() {
        return acc;
    }

    public float getRatio() {
        return ratio;
    }

    public SingleRank(int difficulty, int level, float acc) {
        this.difficulty = difficulty;
        this.level = level;
        this.acc = acc;
        this.ratio = (level + 2) * acc;
    }

    @Override
    public String toString() {
        return "SingleRank{" +
                "difficulty=" + difficulty +
                ", level=" + level +
                ", acc=" + acc +
                ", ratio=" + ratio +
                '}';
    }
}
