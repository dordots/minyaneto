package com.app.minyaneto_android.models.minyan;

public enum PrayType {
    MORNING,
    AFTER_NOON,
    EVENING;

    PrayType() {
    }

    public static PrayType getType(String string) {

        if(null == string){
            return PrayType.MORNING;
        }
        try {
            switch (string) {
                case "shachrit":
                case "shacharit":
                case "shaharit":
                case "שחרית":
                    return PrayType.MORNING;
                case "mincha":
                case "minha":
                case "מנחה":
                    return PrayType.AFTER_NOON;
                case "maariv":
                case "arvit":
                case "ערבית":
                    return PrayType.EVENING;
                default:
                    return PrayType.MORNING; //TODO: fix it !!!!!!!!!!!!!!!!!!

            }
        }
        catch (Exception e){
            return PrayType.MORNING; //TODO: fix it !!!!!!!!!!!!!!!!!!
        }
    }

}
