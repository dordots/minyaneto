package com.app.minyaneto_android.models.minyan;

import android.content.res.Resources;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.acivities.MainActivity;

import java.util.Date;
/**
 * Created by משה on 25/08/2017.
 */

public class RelativeTime implements Time {

    private RelativeTimeType relativeTimeType;
    private int offset;

    public RelativeTime(RelativeTimeType relativeTimeType, int offset) {
        this.relativeTimeType = relativeTimeType;
        this.offset = offset;
    }

    @Override
    public int getHour() {
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public int getMinutes() {
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public Date toDate(WeekDay weekDay) {
        // Implementation depends on Kosher Java
        return null;
    }

    @Override
    public String toString() {
        Resources res = MainActivity.resources;
        String nounMinutes =  res.getString(R.string.nouns_minutes);
        String nounAfterOrBefore =  offset > 0 ? res.getString(R.string.nouns_after): res.getString(R.string.nouns_before);
        String nounThe =  offset < 0 ? res.getString(R.string.nouns_the) : "";
        String nounAt =  res.getString(R.string.nouns_at);
        if (offset == 0) {
            return String.format("%s%s", nounAt, relativeTimeType.toString());
        }
        return String.format("%d %s %s %s %s", offset, nounMinutes, nounAfterOrBefore, nounThe,
                relativeTimeType.toString());
    }
}
