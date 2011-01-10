package org.isolution.androdof;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AndroDOF extends Activity implements View.OnClickListener {
    public static final String TAG = AndroDOF.class.getName();
    private static final String STATE_SELECTED_CAMERA = "selectedCamera";
    private static final String STATE_SELECTED_MANUFACTURER = "selectedManufacturer";
    private static final String STATE_SELECTED_UNIT = "selectedUnit";

    private Bundle bundle;

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
        Log.d(TAG, "Finished creating AndoDOF activity");
    }

    private void restoreSelectedCameraFromBundle() {
        if (bundle != null) {
            String selectedCamera = bundle.getString(STATE_SELECTED_CAMERA);
            if (selectedCamera != null && selectedCamera.trim().length() > 0) {
                setSelectedCamera(selectedCamera);
            }
        }
    }

    private void restoreSelectedManufacturerFromBundle() {
        if (bundle != null) {
            String selectedManufacturer = bundle.getString(STATE_SELECTED_MANUFACTURER);
            if (selectedManufacturer != null && selectedManufacturer.trim().length() > 0) {
                setSelectedManufacturer(CameraData.Manufacturer.valueOf(selectedManufacturer));
            }
        }
    }

    private void restoreSelectedUnitFromBundle() {
        if (bundle != null) {
            String selectedUnit = bundle.getString(STATE_SELECTED_UNIT);
            if (selectedUnit != null && selectedUnit.trim().length() > 0) {
                setSelectedUnit(selectedUnit);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);    //To change body of overridden methods use File | Settings | File Templates.
        outState.putString(STATE_SELECTED_MANUFACTURER, getSelectedManufacturer().toString());
        outState.putString(STATE_SELECTED_CAMERA, getSelectedCamera());
        outState.putString(STATE_SELECTED_UNIT, getSelectedUnit().getAbbrev());
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

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void loadDefaultUnit() {
        SharedPreferences preferences = getPreferences();
        String defaultUnitAbbrev  = preferences.getString(getString(R.string.unit_setting_key), "");
        if (defaultUnitAbbrev != null && defaultUnitAbbrev.trim().length() > 0) {
            setSelectedUnit(defaultUnitAbbrev);
        }
        restoreSelectedUnitFromBundle();
    }

    private void loadDefaultManufacturer() {
        SharedPreferences preferences = getPreferences();
        String defaultManufacturer = preferences.getString(getString(R.string.manufacturer_setting_key), "");
        if (defaultManufacturer != null && defaultManufacturer.trim().length() > 0) {
            setSelectedManufacturer(CameraData.Manufacturer.valueOf(defaultManufacturer));
        }
        restoreSelectedManufacturerFromBundle();
    }

    private void loadDefaultCamera() {
        SharedPreferences preferences = getPreferences();
        String defaultCamera = preferences.getString(getString(R.string.camera_setting_key), "");
        if (defaultCamera != null && defaultCamera.trim().length() > 0) {
            setSelectedCamera(defaultCamera);
        }
        // Override if we have bundled values
        restoreSelectedCameraFromBundle();
    }



    public void onClick(View view) {
        int eventGenerator = view.getId();

        switch (eventGenerator) {
            case R.id.calculate_button: {
                Log.d(TAG, "Calculate button is pressed");
                if (validateInput()){
                    displayResult(calculate());
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
                CameraData.Manufacturer manufacturer = (CameraData.Manufacturer) adapterView.getAdapter().getItem(i);
                populateCameraByManufacturer(manufacturer);
                loadDefaultCamera();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    @SuppressWarnings({"unchecked"})
    private void populateCameraByManufacturer(CameraData.Manufacturer manufacturer) {
        List<CameraData> cameraByManufacturer = CameraData.getCameraByManufacturer(manufacturer);

        // Clear the current camera values
        ArrayAdapter<String> cameraSpinnerAdapter = (ArrayAdapter<String>) getCameraSpinner().getAdapter();
        cameraSpinnerAdapter.clear();

        // Re-populate with cameras for manufacturer
        for (CameraData cameraData : cameraByManufacturer) {
            cameraSpinnerAdapter.add(cameraData.getLabel());
        }
    }

    @SuppressWarnings({"unchecked"})
    private void initializeCameraSpinner() {
        Spinner spinner = getCameraSpinner();
        ArrayAdapter cameraArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        cameraArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(cameraArrayAdapter);
    }

    private void initializeFstopSpinner() {
        Spinner spinner = getFStopSpinner();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for (FStop fstop : FStop.getAllFStops()) {
            adapter.add(fstop);
        }
    }

    private void initUnitSpinner() {
        Spinner unitSpinner = getUnitSpinner();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(arrayAdapter);
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
        BigDecimal focusLength = new BigDecimal(getFocusLengthValue().toString());
        BigDecimal subjectDistance = new BigDecimal(getSubjectDistanceValue().toString());
        BigDecimal fstop = getSelectedFStop();
        String camera = getSelectedCamera();
        CameraData.Manufacturer manufacturer = getSelectedManufacturer();

        Calculator calculator = new Calculator()
                .withSubjectDistance(shouldDisplayAsMeter()?UnitConverter.fromMeterToMillimeters(subjectDistance ) : UnitConverter.fromFeetToMillimeters(subjectDistance))
                .withAperture(fstop)
                .withFocusLength(focusLength)
                .withCoc(CameraData.getCocForCamera(manufacturer, camera));
        return calculator.calculate();
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

    private void setSelectedCamera(String camera) {
        ArrayAdapter adapter = (ArrayAdapter) getCameraSpinner().getAdapter();
        int position = adapter.getPosition(camera);
        getCameraSpinner().setSelection(position);
    }

    private void setSelectedManufacturer(CameraData.Manufacturer manufacturer) {
        ArrayAdapter adapter = (ArrayAdapter) getManufacturerSpinner().getAdapter();
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

    private void setSelectedUnit(String selectedUnit) {
        ArrayAdapter adapter = (ArrayAdapter) getUnitSpinner().getAdapter();
        int position = adapter.getPosition(MeasurementUnit.fromAbbrev(selectedUnit));
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

    private boolean shouldDisplayAsMeter() {
        return MeasurementUnit.METER.equals(getSelectedUnit());
    }


    private void displayResult(Calculator.Result result) {
        TextView nearLimitView = (TextView) findViewById(R.id.near_limit_value);
        nearLimitView.setText(displayDistanceValue(result.getNearDistance()));

        TextView farLimitView = (TextView) findViewById(R.id.far_limit_value);
        farLimitView.setText(displayDistanceValue(result.getFarDistance()));

        TextView hyperFocalView = (TextView) findViewById(R.id.hyperfocal_distance_value);
        hyperFocalView.setText(displayDistanceValue(result.getHyperFocalDistance()));

        TextView inFrontSubjectView  = (TextView) findViewById(R.id.infront_subject_value);
        inFrontSubjectView.setText(displayDistanceValue(result.getInFrontOfSubject()));

        TextView inFrontSubjectHyperfocalView  = (TextView) findViewById(R.id.infront_subject_hyperfocal_value);
        inFrontSubjectHyperfocalView.setText(displayDistanceValue(result.getInFrontOfSubjectForHyperfocal()));

        TextView behindSubjectView  = (TextView) findViewById(R.id.behind_subject_value);
        behindSubjectView.setText(displayDistanceValue(result.getBehindSubject()));

        TextView totalView = (TextView) findViewById(R.id.total_value);
        totalView.setText(displayDistanceValue(result.getTotal()));

        TextView cocView = (TextView) findViewById(R.id.coc_value);
        cocView.setText(displayMillimetersValue(result.getCoc()));
    }

    private String displayDistanceValue(BigDecimal millimetersValue) {
        return millimetersValue.intValue() < 0? "infinite" :
                (shouldDisplayAsMeter()?UnitConverter.fromMillimetersToMeters(millimetersValue):UnitConverter.fromMillimetersToFeet(millimetersValue)) + " " +
                 (shouldDisplayAsMeter()?MeasurementUnit.METER.getAbbrev():MeasurementUnit.FEET) ;
    }

    private String displayMillimetersValue(BigDecimal millimetersValue) {
        return millimetersValue + " " + MeasurementUnit.MILLIMETER.getAbbrev();
    }
}
