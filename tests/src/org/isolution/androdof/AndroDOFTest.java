package org.isolution.androdof;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    private AndroDOF mActivity;
    private EditText subjectDistanceEditText;
    private EditText focusLengthEditText;
    private Spinner manufacturerSpinner;
    private Spinner fstopSpinner;
    private Spinner unitSpinner;
    private Button calculateButton;
    private TextView resultNearLimit;
    private TextView resultFarLimit;
    private TextView resultTotalValue;
    private Instrumentation instrumentation;

    public AndroDOFTest() {
        super("org.isolution.androdof", AndroDOF.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        /*
         * prepare to send key events to the app under test by turning off touch mode.
         * Must be done before the first call to getActivity()
         */
        setActivityInitialTouchMode(false);

        /*
         * Start the app under test by starting its main activity. The test runner already knows
         * which activity this is from the call to the super constructor, as mentioned
         * previously. The tests can now use instrumentation to directly access the main
         * activity through mActivity.
         */
        mActivity = this.getActivity();

        subjectDistanceEditText = (EditText) mActivity.findViewById(R.id.subject_distance_editText);
        focusLengthEditText = (EditText) mActivity.findViewById(R.id.focus_length_editText);
        manufacturerSpinner = (Spinner) mActivity.findViewById(R.id.manufacturer_spinner);
        fstopSpinner = (Spinner) mActivity.findViewById(R.id.fstop_spinner);
        unitSpinner = (Spinner) mActivity.findViewById(R.id.unit_spinner);
        calculateButton = (Button) mActivity.findViewById(R.id.calculate_button);

        resultNearLimit = (TextView) mActivity.findViewById(R.id.near_limit_value);
        resultFarLimit = (TextView) mActivity.findViewById(R.id.far_limit_value);
        resultTotalValue = (TextView) mActivity.findViewById(R.id.total_value);
        instrumentation = getInstrumentation();
    }

    /**
     * Verifies that the activity under test can be launched.
     */
    @SmallTest
    public void testViewsCreated() {
        assertNotNull("activity should be launched successfully", getActivity());
        assertNotNull(subjectDistanceEditText);
        assertNotNull(focusLengthEditText);
        assertNotNull(manufacturerSpinner);
        assertNotNull(fstopSpinner);
        assertNotNull(unitSpinner);
        assertNotNull(calculateButton);
        assertNotNull(resultNearLimit);
        assertNotNull(resultFarLimit);
        assertNotNull(resultTotalValue);
    }

    @SmallTest
    public void testViewsVisible() {
        ViewAsserts.assertOnScreen(subjectDistanceEditText.getRootView(),subjectDistanceEditText);
        ViewAsserts.assertOnScreen(focusLengthEditText.getRootView(),focusLengthEditText);
        ViewAsserts.assertOnScreen(manufacturerSpinner.getRootView(),manufacturerSpinner);
        ViewAsserts.assertOnScreen(fstopSpinner.getRootView(),fstopSpinner);
        ViewAsserts.assertOnScreen(unitSpinner.getRootView(),unitSpinner);
        ViewAsserts.assertOnScreen(calculateButton.getRootView(),calculateButton);
//        ViewAsserts.assertOnScreen(resultNearLimit.getRootView(),resultNearLimit);
//        ViewAsserts.assertOnScreen(resultFarLimit.getRootView(),resultFarLimit);
//        ViewAsserts.assertOnScreen(resultTotalValue.getRootView(),resultTotalValue);
    }

    @SmallTest
    public void testShouldHaveEmptyInitialValue() {
        assertTrue("".equals(focusLengthEditText.getText().toString()));
        assertTrue("".equals(subjectDistanceEditText.getText().toString()));
    }

    @SmallTest
    public void testCalculate() {
        TouchUtils.tapView(this, focusLengthEditText);
        sendKeys("5 0");
        assertTrue(focusLengthEditText.isFocused());
        assertEquals("50", focusLengthEditText.getText().toString());



    }


}
