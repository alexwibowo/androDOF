package org.isolution.androdof;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.math.BigDecimal;
import java.util.List;

import static org.isolution.androdof.DistanceFormatter.format;

public class AndroDOF extends Activity implements View.OnClickListener {
    public static final String TAG = AndroDOF.class.getName();

    private static final String STATE_SELECTED_CAMERA = "selectedCamera";
    private static final String STATE_SELECTED_MANUFACTURER = "selectedManufacturer";
    private static final String STATE_SELECTED_UNIT = "selectedUnit";
    private static final String STATE_RESULT = "calculationResult";

    private Bundle bundle;
    private Calculator.Result calculationResult;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating AndoDOF activity");
        super.onCreate(savedInstanceState);
        this.bundle = savedInstanceState;
        setContentView(R.layout.main);
        findViewById(R.id.calculate_button).setOnClickListener(this);

        initializeCameraSpinner();
        initializeFstopSpinner();
        initUnitSpinner();
        initializeManufacturerSpinner();

        loadDefaultManufacturer();
        loadDefaultUnit();
        restoreCalculationResult();
        Log.d(TAG, "Finished creating AndoDOF activity");
    }

    private void restoreSelectedCameraFromBundle() {
        if (bundle != null) {
            String selectedCamera = bundle.getString(STATE_SELECTED_CAMERA);
            if (!StringUtils.isBlank(selectedCamera)) {
                Log.d(TAG, "Restoring selected camera from saved bundle");
                setSelectedCamera(selectedCamera);
            }
        }
    }

    private void restoreSelectedManufacturerFromBundle() {
        if (bundle != null) {
            CameraData.Manufacturer selectedManufacturer = (CameraData.Manufacturer) bundle.getSerializable(STATE_SELECTED_MANUFACTURER);
            if (selectedManufacturer!=null) {
                Log.d(TAG, "Restoring selected manufacturer from saved bundle");
                setSelectedManufacturer(selectedManufacturer);
            }
        }
    }

    private void restoreSelectedUnitFromBundle() {
        if (bundle != null) {
            MeasurementUnit selectedUnit = (MeasurementUnit) bundle.getSerializable(STATE_SELECTED_UNIT);
            if (selectedUnit!=null) {
                Log.d(TAG, "Restoring selected unit from saved bundle");
                setSelectedUnit(selectedUnit);
            }
        }
    }

    private void restoreCalculationResult() {
        if (bundle != null) {
            calculationResult = (Calculator.Result) bundle.getSerializable(STATE_RESULT);
            if (calculationResult != null) {
                Log.d(TAG, "Restoring calculated result from saved bundle");
                displayResult(calculationResult);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Saving state to bundle");
        outState.putSerializable(STATE_SELECTED_MANUFACTURER, getSelectedManufacturer());
        outState.putString(STATE_SELECTED_CAMERA, getSelectedCamera());
        outState.putSerializable(STATE_SELECTED_UNIT, getSelectedUnit());
        if (calculationResult != null) {
            outState.putSerializable(STATE_RESULT, calculationResult);
        }
        Log.d(TAG, "Finished saving state to bundle");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_button:{
                Log.d(TAG, "Help button");
                startActivity(new Intent(this, Help.class));
                return true;
            }
            case R.id.feedback_button: {
                Log.d(TAG, "Feedback button");
                String builder = ApplicationHelper.getDeviceDetails(this);
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alexandroiddev@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroDOF");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, builder);
                startActivity(emailIntent);
                return true;
            }
            case R.id.about_button: {
                Log.d(TAG, "About button");
                startActivity(new Intent(this, About.class));
                return true;
            }
            case R.id.setting_button: {
                Log.d(TAG, "Setting button");
                startActivity(new Intent(this, Prefs.class));
                return true;
            }
        }
        return false;
    }

    private String getPreferenceValue(int preferenceKey, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(getString(preferenceKey), defaultValue);
    }

    private void loadDefaultUnit() {
        String defaultUnitAbbrev = getPreferenceValue(R.string.unit_setting_key, "");
        if (!StringUtils.isBlank(defaultUnitAbbrev)) {
            Log.d(TAG, "Loading default unit " + defaultUnitAbbrev);
            setSelectedUnit(defaultUnitAbbrev);
        }
        // Override if we have stored state value
        restoreSelectedUnitFromBundle();
    }

    private void loadDefaultManufacturer() {
        String defaultManufacturer = getPreferenceValue(R.string.manufacturer_setting_key, "");
        if (!StringUtils.isBlank(defaultManufacturer)) {
            Log.d(TAG, "Loading default manufacturer " + defaultManufacturer);
            setSelectedManufacturer(CameraData.Manufacturer.valueOf(defaultManufacturer));
        }
        // Override if we have stored state value
        restoreSelectedManufacturerFromBundle();
    }

    private void loadDefaultCamera() {
        String defaultCamera = getPreferenceValue(R.string.camera_setting_key, "");
        if (!StringUtils.isBlank(defaultCamera)) {
            Log.d(TAG, "Loading default camera " + defaultCamera);
            setSelectedCamera(defaultCamera);
        }
        // Override if we have stored state value
        restoreSelectedCameraFromBundle();
    }

    public void onClick(View view) {
        int eventGenerator = view.getId();

        switch (eventGenerator) {
            case R.id.calculate_button: {
                Log.i(TAG, "Calculate button is pressed");
                if (validateInput()) {
                    Log.i(TAG, "Calculating..");
                    calculationResult = calculate();
                    displayResult(calculationResult);
                }
                break;
            }
        }
    }

    private void initializeManufacturerSpinner() {
        Spinner manufacturerSpinner = getManufacturerSpinner();

        ArrayAdapter<CameraData.Manufacturer> arrayAdapter = new ArrayAdapter<CameraData.Manufacturer>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(arrayAdapter);

        for (CameraData.Manufacturer manufacturer : CameraData.getManufacturers()) {
            arrayAdapter.add(manufacturer);
        }
        manufacturerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Manufacturer selected");
                CameraData.Manufacturer manufacturer = (CameraData.Manufacturer) adapterView.getAdapter().getItem(i);
                populateCameraByManufacturer(manufacturer);
                loadDefaultCamera();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    @SuppressWarnings({"unchecked"})
    private void populateCameraByManufacturer(CameraData.Manufacturer manufacturer) {
        Log.i(TAG, "Populating camera list for selected manufacturer " + manufacturer);
        List<CameraData> cameraByManufacturer = CameraData.getCameraByManufacturer(manufacturer);

        // Clear the current camera values
        ArrayAdapter<String> cameraSpinnerAdapter = (ArrayAdapter<String>) getCameraSpinner().getAdapter();
        cameraSpinnerAdapter.clear();

        // Re-populate with cameras for manufacturer
        for (CameraData cameraData : cameraByManufacturer) {
            cameraSpinnerAdapter.add(cameraData.getLabel());
        }
        Log.i(TAG, "Finished populating camera list for  selected manufacturer");
    }

    private void initializeCameraSpinner() {
        ArrayAdapter<String> cameraArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        cameraArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCameraSpinner().setAdapter(cameraArrayAdapter);
    }

    private void initializeFstopSpinner() {
        ArrayAdapter<FStop> adapter = new ArrayAdapter<FStop>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getFStopSpinner().setAdapter(adapter);
        for (FStop fstop : FStop.getAllFStops()) {
            adapter.add(fstop);
        }
    }

    private void initUnitSpinner() {
        ArrayAdapter<MeasurementUnit> arrayAdapter = new ArrayAdapter<MeasurementUnit>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getUnitSpinner().setAdapter(arrayAdapter);
        for (MeasurementUnit measurementUnit : MeasurementUnit.getUnitsForSubjectDistance()) {
            arrayAdapter.add(measurementUnit);
        }
    }

    private boolean validateInput() {
        Log.d(TAG, "Validating input");
        Editable subjectDistanceValue = getSubjectDistanceValue();
        Editable focusLengthValue = getFocusLengthValue();

        InputValidator inputValidator = new InputValidator(subjectDistanceValue.toString(), focusLengthValue.toString());
        final List<String> errors = inputValidator.getErrors();

        if (!inputValidator.isValid()) {
            Log.d(TAG, "Invalid input");
            String[] strings = errors.toArray(new String[errors.size()]);
            new AlertDialog.Builder(this)
                    .setTitle("Validation error")
                    .setItems(strings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String errorItem = errors.get(i);
                            focusViewInError(errorItem);
                        }
                    }).show();
        }
        return inputValidator.isValid();
    }

    private void focusViewInError(String errorItem) {
         if (errorItem.indexOf(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE) != -1) {
            findViewById(R.id.focus_length_editText).requestFocus();
        }else if (errorItem.indexOf(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE) != -1) {
            findViewById(R.id.subject_distance_editText).requestFocus();
        }
    }

    private Calculator.Result calculate() {
        Calculator calculator = new Calculator()
                .withSubjectDistance(getSubjectDistanceInMillimeters())
                .withAperture(getSelectedFStop())
                .withFocusLength(new BigDecimal(getFocusLengthValue().toString()))
                .withCoc(CameraData.getCocForCamera(getSelectedManufacturer(), getSelectedCamera()));
        return calculator.calculate();
    }

    private BigDecimal getSubjectDistanceInMillimeters() {
        BigDecimal subjectDistance = new BigDecimal(getSubjectDistanceValue().toString());
        BigDecimal distanceInMillimeters = null;
        switch (getSelectedUnit()) {
            case METER:
                distanceInMillimeters = UnitConverter.fromMeterToMillimeters(subjectDistance);
                break;
            case FEET:
                distanceInMillimeters = UnitConverter.fromFeetToMillimeters(subjectDistance);
                break;
        }
        return distanceInMillimeters;
    }

    private Editable getSubjectDistanceValue() {
        return ((EditText) findViewById(R.id.subject_distance_editText)).getText();
    }

    private Editable getFocusLengthValue() {
        return ((EditText) findViewById(R.id.focus_length_editText)).getText();
    }

    private String getSelectedCamera() {
        return (String) getCameraSpinner().getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void setSelectedCamera(String camera) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getCameraSpinner().getAdapter();
        int position = adapter.getPosition(camera);
        getCameraSpinner().setSelection(position);
    }

    @SuppressWarnings("unchecked")
    private void setSelectedManufacturer(CameraData.Manufacturer manufacturer) {
        ArrayAdapter<CameraData.Manufacturer> adapter = (ArrayAdapter<CameraData.Manufacturer>) getManufacturerSpinner().getAdapter();
        int position = adapter.getPosition(manufacturer);
        getManufacturerSpinner().setSelection(position);
    }


    private Spinner getCameraSpinner() {
        return (Spinner) findViewById(R.id.camera_spinner);
    }

    private Spinner getUnitSpinner() {
        return (Spinner) findViewById(R.id.unit_spinner);
    }

    private MeasurementUnit getSelectedUnit() {
        return (MeasurementUnit) getUnitSpinner().getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void setSelectedUnit(String selectedUnit) {
        ArrayAdapter<MeasurementUnit> adapter = (ArrayAdapter<MeasurementUnit>) getUnitSpinner().getAdapter();
        int position = adapter.getPosition(MeasurementUnit.fromAbbrev(selectedUnit));
        getUnitSpinner().setSelection(position);
    }

    @SuppressWarnings("unchecked")
    private void setSelectedUnit(MeasurementUnit selectedUnit) {
        ArrayAdapter<MeasurementUnit> adapter = (ArrayAdapter<MeasurementUnit>) getUnitSpinner().getAdapter();
        int position = adapter.getPosition(selectedUnit);
        getUnitSpinner().setSelection(position);
    }

    private Spinner getFStopSpinner() {
        return (Spinner) findViewById(R.id.fstop_spinner);
    }

    private BigDecimal getSelectedFStop() {
        return ((FStop) getFStopSpinner().getSelectedItem()).getValue();
    }

    private CameraData.Manufacturer getSelectedManufacturer() {
        return (CameraData.Manufacturer) getManufacturerSpinner().getSelectedItem();
    }

    private Spinner getManufacturerSpinner() {
        return (Spinner) findViewById(R.id.manufacturer_spinner);
    }

    private void displayResult(Calculator.Result result) {
        Log.i(TAG, "Displaying calculated result");

        Log.d(TAG, "Displaying near limit value");
        TextView nearLimitView = (TextView) findViewById(R.id.near_limit_value);
        nearLimitView.setText(format(result.getNearDistance(), getSelectedUnit()));

        Log.d(TAG, "Displaying far limit value");
        TextView farLimitView = (TextView) findViewById(R.id.far_limit_value);
        farLimitView.setText(format(result.getFarDistance(), getSelectedUnit()));

        Log.d(TAG, "Displaying hyperfocal distance value");
        TextView hyperFocalView = (TextView) findViewById(R.id.hyperfocal_distance_value);
        hyperFocalView.setText(format(result.getHyperFocalDistance(), getSelectedUnit()));

        Log.d(TAG, "Displaying in front subject  value");
        TextView inFrontSubjectView = (TextView) findViewById(R.id.infront_subject_value);
        inFrontSubjectView.setText(format(result.getInFrontOfSubject(), getSelectedUnit()));

        Log.d(TAG, "Displaying in front subject hyperfocal value");
        TextView inFrontSubjectHyperfocalView = (TextView) findViewById(R.id.infront_subject_hyperfocal_value);
        inFrontSubjectHyperfocalView.setText(format(result.getInFrontOfSubjectForHyperfocal(), getSelectedUnit()));

        Log.d(TAG, "Displaying behind subject value");
        TextView behindSubjectView = (TextView) findViewById(R.id.behind_subject_value);
        behindSubjectView.setText(format(result.getBehindSubject(), getSelectedUnit()));

        Log.d(TAG, "Displaying total value");
        TextView totalView = (TextView) findViewById(R.id.total_value);
        totalView.setText(format(result.getTotal(), getSelectedUnit()));

        Log.d(TAG, "Displaying coc value");
        TextView cocView = (TextView) findViewById(R.id.coc_value);
        cocView.setText(format(result.getCoc(), MeasurementUnit.MILLIMETER));

        Log.i(TAG, "Finished displaying calculated result");
    }
}
