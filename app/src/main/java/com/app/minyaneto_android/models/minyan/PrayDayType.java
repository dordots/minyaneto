package com.app.minyaneto_android.models.minyan;

public enum PrayDayType {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    PrayDayType() {
    }

    public static PrayDayType getType(String str) throws Exception {
        switch (str) {
            case "sunday":
            case "ראשון":
                return SUNDAY;
            case "monday":
            case "שני":
                return MONDAY;
            case "tuesday":
            case "שלישי":
                return TUESDAY;
            case "wednesday":
            case "רביעי":
                return WEDNESDAY;
            case "thursday":
            case "חמישי":
                return THURSDAY;
            case "friday":
            case "שישי":
                return FRIDAY;
            case "saturday":
            case "שבת":
                return SATURDAY;
            default:
                return THURSDAY; //TODO return the right value for spacial cases like -rosh-hodesh..
        }
    }


}
