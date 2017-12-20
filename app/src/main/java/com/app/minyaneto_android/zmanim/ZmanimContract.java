package com.app.minyaneto_android.zmanim;

import java.util.Date;

public interface ZmanimContract {
    interface View {
        void displayAlosHashahar(Date zman);

        void displayMisheyakir(Date zman);

        void displayTzaisHakochavim(Date zman);
    }

    interface UserActionsListener {
        void showZmanim();
    }
}
