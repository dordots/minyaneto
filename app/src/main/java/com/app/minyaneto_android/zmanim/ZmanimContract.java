package com.app.minyaneto_android.zmanim;

import java.util.Date;

public interface ZmanimContract {

  interface View {

    void displayAlosHashahar(Date zman);

    void displayMisheyakir(Date zman);

    void displayHenezHahama(Date zman);

    void displayShkiaa(Date zman);

    void displayTzaisHakochavim(Date zman);

    void displayNoLocationFound();
  }

  interface UserActionsListener {

    void showZmanim();
  }
}
