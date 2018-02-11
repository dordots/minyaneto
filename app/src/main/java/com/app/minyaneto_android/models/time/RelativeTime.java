package com.app.minyaneto_android.models.time;

public class RelativeTime {
    private RelativeTimeType relativeTimeType;
    private int offset;

    public RelativeTime(RelativeTimeType relativeTimeType, int offset) {
        this.relativeTimeType = relativeTimeType;
        this.offset = offset;
    }

    public RelativeTimeType getRelativeTimeType() {
        return relativeTimeType;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        String nounMinutes = "דקות";
        String nounAfterOrBefore = offset > 0 ? "אחרי" : "לפני";
        String nounThe = offset < 0 ? "ה-" : "";
        String nounAt = "ב-";
        if (offset == 0) {
            return String.format("%s%s", nounAt, relativeTimeType.toString());
        }
        return String.format("%d %s %s %s %s", offset, nounMinutes, nounAfterOrBefore, nounThe,
                relativeTimeType.toString());
    }

}
