package org.isolution.androdof;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.*;
import org.junit.Test;

import java.math.BigDecimal;

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
    private Spinner cameraSpinner;
    private Spinner fstopSpinner;
    private Spinner unitSpinner;
    private Button calculateButton;


    private TextView resultNearLimit;
    private TextView resultFarLimit;
    private TextView resultTotalValue;
    private TextView resultInfrontSubjectValue;
    private TextView resultBehindSubjectValue;
    private TextView resultInfrontHyperfocalValue;
    private TextView resultHyperfocalValue;
    private TextView resultCocValue;


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
        cameraSpinner = (Spinner) mActivity.findViewById(R.id.camera_spinner);
        fstopSpinner = (Spinner) mActivity.findViewById(R.id.fstop_spinner);
        unitSpinner = (Spinner) mActivity.findViewById(R.id.unit_spinner);
        calculateButton = (Button) mActivity.findViewById(R.id.calculate_button);

        resultNearLimit = (TextView) mActivity.findViewById(R.id.near_limit_value);
        resultFarLimit = (TextView) mActivity.findViewById(R.id.far_limit_value);
        resultTotalValue = (TextView) mActivity.findViewById(R.id.total_value);
        resultInfrontSubjectValue = (TextView) mActivity.findViewById(R.id.infront_subject_value);
        resultBehindSubjectValue = (TextView) mActivity.findViewById(R.id.behind_subject_value);
        resultInfrontHyperfocalValue = (TextView) mActivity.findViewById(R.id.infront_subject_hyperfocal_value);
        resultHyperfocalValue = (TextView) mActivity.findViewById(R.id.hyperfocal_distance_value);
        resultCocValue = (TextView) mActivity.findViewById(R.id.coc_value);
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
        assertNotNull(cameraSpinner);
        assertNotNull(fstopSpinner);
        assertNotNull(unitSpinner);
        assertNotNull(calculateButton);
        assertNotNull(resultNearLimit);
        assertNotNull(resultFarLimit);
        assertNotNull(resultTotalValue);
        assertNotNull(resultInfrontSubjectValue);
        assertNotNull(resultBehindSubjectValue);
        assertNotNull(resultInfrontHyperfocalValue);
        assertNotNull(resultHyperfocalValue);
        assertNotNull(resultCocValue);
    }

    @SmallTest
    public void testViewsVisible() {
        ViewAsserts.assertOnScreen(subjectDistanceEditText.getRootView(),subjectDistanceEditText);
        ViewAsserts.assertOnScreen(focusLengthEditText.getRootView(),focusLengthEditText);
        ViewAsserts.assertOnScreen(manufacturerSpinner.getRootView(),manufacturerSpinner);
        ViewAsserts.assertOnScreen(cameraSpinner.getRootView(),cameraSpinner);
        ViewAsserts.assertOnScreen(fstopSpinner.getRootView(),fstopSpinner);
        ViewAsserts.assertOnScreen(unitSpinner.getRootView(),unitSpinner);
        ViewAsserts.assertOnScreen(calculateButton.getRootView(),calculateButton);
//        ViewAsserts.assertOnScreen(resultNearLimit.getRootView(),resultNearLimit);
//        ViewAsserts.assertOnScreen(resultFarLimit.getRootView(),resultFarLimit);
//        ViewAsserts.assertOnScreen(resultTotalValue.getRootView(),resultTotalValue);
    }

    @SmallTest
    public void testShouldHaveCorrectInitialValue() {
        assertTrue("Focus length should have an empty initial value","".equals(focusLengthEditText.getText().toString()));
        assertTrue("Subject distance should have an empty initial value", "".equals(subjectDistanceEditText.getText().toString()));
        assertEquals("Near limit should have empty initial value", "", getResultNearLimit());
        assertEquals("Far limit should have empty initial value", "", getResultFarLimit());
        assertEquals("Total value should have empty initial value", "", getResultTotalValue());
        assertEquals("Coc should have empty initial value", "", getResultCoc());
        assertEquals("Hyperfocal should have empty initial value", "", getResultHyperfocal());
        assertEquals("Infront hyperfocal should have empty initial value","", getResultInfrontHyperfocal());
        assertEquals("In front subject should have empty initial value","", getResultInfrontSubject());
        assertEquals("Behind subject should have empty initial value","", getResultBehindSubject());
    }

    @UiThreadTest
    public void test_change_screen_orientation() {
        setManufacturer(CameraData.Manufacturer.NIKON);
        setModel("D90");
        setFocusLength("50");
        setSubjectDistance("10");
        setFStop("2");
        MeasurementUnit selectedUnit = MeasurementUnit.METER;
        setUnit(selectedUnit);
        calculateButton.performClick();

        Calculator.Result result = new Calculator().withAperture(new BigDecimal("2"))
                .withCoc(CameraData.getCocForCamera(CameraData.Manufacturer.NIKON, "D90"))
                .withFocusLength(new BigDecimal("50"))
                .withSubjectDistance(new BigDecimal("10").multiply(new BigDecimal("1000")))
                .calculate();

        verifyValues(selectedUnit, result);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        verifyValues(selectedUnit, result);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verifyValues(selectedUnit, result);
    }

    private void verifyValues(MeasurementUnit selectedUnit, Calculator.Result result) {
        assertEquals(DistanceFormatter.format(result.getNearDistance(), selectedUnit), getResultNearLimit());
        assertEquals(DistanceFormatter.format(result.getFarDistance(), selectedUnit), getResultFarLimit());
        assertEquals(DistanceFormatter.format(result.getTotal(), selectedUnit), getResultTotalValue());
        assertEquals(DistanceFormatter.format(result.getInFrontOfSubject(), selectedUnit), getResultInfrontSubject());
        assertEquals(DistanceFormatter.format(result.getBehindSubject(), selectedUnit), getResultBehindSubject());
        assertEquals(DistanceFormatter.format(result.getInFrontOfSubjectForHyperfocal(), selectedUnit), getResultInfrontHyperfocal());
        assertEquals(DistanceFormatter.format(result.getHyperFocalDistance(), selectedUnit), getResultHyperfocal());
        assertEquals("Should display coc in millimeter",DistanceFormatter.format(result.getCoc(), MeasurementUnit.MILLIMETER), getResultCoc());
    }


    @UiThreadTest
    public void test_calculate() {
        setManufacturer(CameraData.Manufacturer.NIKON);
        setModel("D90");
        setFocusLength("50");
        setSubjectDistance("10");
        setFStop("2");
        MeasurementUnit selectedUnit = MeasurementUnit.METER;
        setUnit(selectedUnit);
        calculateButton.performClick();

// use this if not using UiThreadTest
//        TouchUtils.tapView(this, focusLengthEditText);
//        sendKeys("5 0");
//        TouchUtils.tapView(this, subjectDistanceEditText);
//        sendKeys("1 0");
//        TouchUtils.clickView(this, calculateButton);

        Calculator.Result result = new Calculator().withAperture(new BigDecimal("2"))
                .withCoc(CameraData.getCocForCamera(CameraData.Manufacturer.NIKON, "D90"))
                .withFocusLength(new BigDecimal("50"))
                .withSubjectDistance(new BigDecimal("10").multiply(new BigDecimal("1000")))
                .calculate();
        verifyValues(selectedUnit, result);
    }

    private String getResultCoc() {
        return resultCocValue.getText().toString();
    }

    private String getResultHyperfocal() {
        return resultHyperfocalValue.getText().toString();
    }

    private String getResultInfrontHyperfocal() {
        return resultInfrontHyperfocalValue.getText().toString();
    }

    private String getResultBehindSubject() {
        return resultBehindSubjectValue.getText().toString();
    }

    private String getResultInfrontSubject() {
        return resultInfrontSubjectValue.getText().toString();
    }

    private String getResultTotalValue() {
        return resultTotalValue.getText().toString();
    }

    private String getResultFarLimit() {
        return resultFarLimit.getText().toString();
    }

    private String getResultNearLimit() {
        return resultNearLimit.getText().toString();
    }

    @SuppressWarnings("unchecked")
    private void setModel(String value) {
        cameraSpinner.requestFocus();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) cameraSpinner.getAdapter();
        cameraSpinner.setSelection(adapter.getPosition(value));
    }

    @SuppressWarnings("unchecked")
    private void setManufacturer(CameraData.Manufacturer value) {
        manufacturerSpinner.requestFocus();
        ArrayAdapter<CameraData.Manufacturer> adapter = (ArrayAdapter<CameraData.Manufacturer>) manufacturerSpinner.getAdapter();
        manufacturerSpinner.setSelection(adapter.getPosition(value));
    }

    @SuppressWarnings("unchecked")
    private void setFStop(String value) {
        fstopSpinner.requestFocus();
        ArrayAdapter<FStop> adapter = (ArrayAdapter<FStop>) fstopSpinner.getAdapter();
        fstopSpinner.setSelection(adapter.getPosition(FStop.getFStop(value)));
    }

    @SuppressWarnings("unchecked")
    private void setUnit(MeasurementUnit value) {
        unitSpinner.requestFocus();
        ArrayAdapter<MeasurementUnit> adapter = (ArrayAdapter<MeasurementUnit>) unitSpinner.getAdapter();
        unitSpinner.setSelection(adapter.getPosition(value));
    }

    private void setSubjectDistance(String value) {
        subjectDistanceEditText.requestFocus();
        subjectDistanceEditText.setText(value);
    }

    private void setFocusLength(String value) {
        focusLengthEditText.requestFocus();
        focusLengthEditText.setText(value);
    }


}
