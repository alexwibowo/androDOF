package org.isolution.androdof;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.isolution.androdof.AndroDOFTest \
 * org.isolution.androdof.tests/android.test.InstrumentationTestRunner
 */
public class AndroDOFTest extends ActivityInstrumentationTestCase2<AndroDOF> {

    public AndroDOFTest() {
        super("org.isolution.androdof", AndroDOF.class);
    }

}
