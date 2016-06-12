/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplanDisplayer.Utils;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;

public class Matcher {

    public static org.hamcrest.Matcher<View> withTextSize(final float textSize) {
        Checks.checkNotNull(textSize);

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public boolean matchesSafely(TextView label) {
                return label.getTextSize() == textSize;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("With text size: " + textSize);
            }
        };
    }

    public static org.hamcrest.Matcher<View> withTextPaintFlags(final int paintFlag) {
        Checks.checkNotNull(paintFlag);

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public boolean matchesSafely(TextView label) {
                return (paintFlag & label.getPaintFlags()) > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with paint flag: ");
            }
        };
    }
}
