package com.app.minyaneto_android.zmanim;

import java.util.Date;

public interface ZmanimContract {
    interface View {
        void displayAlosHashahar(Date alos);

        void displayMisheyakir(Date misheyakir);

        void displayTzaisHakochavim(Date tzais);
    }

    interface UserActionsListener {
        void showZmanim();
    }
}
