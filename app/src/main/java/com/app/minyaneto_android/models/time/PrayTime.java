package com.app.minyaneto_android.models.time;

public class PrayTime {
    private RelativeTime relativeTime;
    private ExactTime exactTime;

    public PrayTime(ExactTime exactTime) {
        this.exactTime = exactTime;
    }
    public PrayTime(RelativeTime relativeTime) {
        this.relativeTime = relativeTime;
    }

    public RelativeTime getRelativeTime() {
        return relativeTime;
    }

    public ExactTime getExactTime() {
        return exactTime;
    }

    public boolean isRelative() {
        return relativeTime != null;
    }
}
