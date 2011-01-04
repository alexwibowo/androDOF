package org.isolution.androdof;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AndroDOF extends Activity implements View.OnClickListener {
    public static final String TAG = AndroDOF.class.getName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating AndoDOF activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.calculate_button).setOnClickListener(this);
        initializeCameraSpinner();
        initializeManufacturerSpinner();
        Log.d(TAG, "Finished creating AndoDOF activity");
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
            case R.id.about_button: {
                Log.d(TAG, "About button");
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                return true;
            }
        }
        return false;
    }

    public void onClick(View view) {
        int eventGenerator = view.getId();

        switch (eventGenerator) {
            case R.id.calculate_button: {
                Log.d(TAG, "Calculate button is pressed");
                if (validateInput()){
                    Calculator.Result result = calculate();
                    displayValues(result);
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
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
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

    private boolean validateInput() {
        Log.d(TAG, "Validating input");
        Editable subjectDistanceValue = getSubjectDistanceValue();
        Editable focusLengthValue = getFocusLengthValue();
        Editable apertureValue = getApertureValue();

        InputValidator inputValidator = new InputValidator(subjectDistanceValue, focusLengthValue, apertureValue);
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
        if (errorItem.indexOf(InputValidator.APERTURE_ERROR_MESSAGE) != -1) {
            findViewById(R.id.aperture_editText).requestFocus();
        }else if (errorItem.indexOf(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE) != -1) {
            findViewById(R.id.focus_length_editText).requestFocus();
        }else if (errorItem.indexOf(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE) != -1) {
            findViewById(R.id.subject_distance_editText).requestFocus();
        }
    }

    private Calculator.Result calculate() {
        BigDecimal aperture = new BigDecimal(getApertureValue().toString());
        BigDecimal focusLength = new BigDecimal(getFocusLengthValue().toString());
        BigDecimal subjectDistance = new BigDecimal(getSubjectDistanceValue().toString());
        String camera = getSelectedCamera();
        CameraData.Manufacturer manufacturer = getSelectedManufacturer();

        Log.d(TAG,"Selected camera is " + camera);


        Calculator calculator = new Calculator()
                .withSubjectDistance(subjectDistance.multiply(new BigDecimal(1000)))
                .withAperture(aperture)
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

    private Editable getApertureValue() {
        return ((EditText) findViewById(R.id.aperture_editText)).getText();
    }

    private String getSelectedCamera() {
        return (String) getCameraSpinner().getSelectedItem();
    }

    private Spinner getCameraSpinner() {
        return (Spinner) findViewById(R.id.camera_spinner);
    }

    private CameraData.Manufacturer getSelectedManufacturer() {
        return (CameraData.Manufacturer) getManufacturerSpinner().getSelectedItem();
    }

    private Spinner getManufacturerSpinner() {
        return (Spinner) findViewById(R.id.manufacturer_spinner);
    }


    private void displayValues(Calculator.Result result) {
        TextView subjectDistanceView = (TextView) findViewById(R.id.subjet_distance_value);
        subjectDistanceView.setText(displayMeterValue(result.getSubjectDistance()));

        TextView nearLimitView = (TextView) findViewById(R.id.near_limit_value);
        nearLimitView.setText(displayMeterValue(result.getNearDistance()));

        TextView farLimitView = (TextView) findViewById(R.id.far_limit_value);
        farLimitView.setText(displayMeterValue(result.getFarDistance()));

        TextView hyperFocalView = (TextView) findViewById(R.id.hyperfocal_distance_value);
        hyperFocalView.setText(displayMeterValue(result.getHyperFocalDistance()));

        TextView totalView = (TextView) findViewById(R.id.total_value);
        totalView.setText(displayMeterValue(result.getTotal()));

        TextView cocView = (TextView) findViewById(R.id.coc_value);
        cocView.setText(displayMillimetersValue(result.getCoc()));
    }

    private BigDecimal asMeters(BigDecimal millimetersValue) {
        return millimetersValue.divide(new BigDecimal(1000), 3,RoundingMode.HALF_EVEN);
    }

    private String displayMeterValue(BigDecimal millimetersValue) {
        return asMeters(millimetersValue) + " m";
    }

    private String displayMillimetersValue(BigDecimal millimetersValue) {
        return millimetersValue + " mm";
    }
}
