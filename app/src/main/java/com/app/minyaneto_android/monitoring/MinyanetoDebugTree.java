package com.app.minyaneto_android.monitoring;


import com.testfairy.TestFairy;

import timber.log.Timber;

public class MinyanetoDebugTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, tag, message, t);
        TestFairy.log(tag, message);
    }
}
