package com.przyjaznyplanDisplayer;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

public class FirstTest extends ActivityInstrumentationTestCase2<PlanActivityView> {

    @TargetApi(Build.VERSION_CODES.FROYO)
    public FirstTest() {
        super(PlanActivityView.class);
    }

    public void testExample() {
        assertEquals(true, true);
    }
}
