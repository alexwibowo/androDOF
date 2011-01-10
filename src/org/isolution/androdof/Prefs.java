package org.isolution.androdof;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 4/01/11
 * Time: 7:52 PM
 */
public class Prefs extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        initManufacturer();
        initCamera();
        initUnit();

        if (getUnitPreference().getValue() != null) {
            onUnitSelected(getUnitPreference().getValue());
        }
        if (getManufacturerPreference().getValue() != null) {
            onManufacturerSelected(getManufacturerPreference().getValue());
        }
    }

    private void initManufacturer() {
        final ListPreference manufacturerPref = getManufacturerPreference();
        String[] manufacturers = CameraData.getManufacturersAsString();
        manufacturerPref.setEntries(manufacturers);
        manufacturerPref.setEntryValues(manufacturers);
        manufacturerPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object selectedManufacturer) {
                getCameraPreference().setValue(null);
                onManufacturerSelected((String) selectedManufacturer);
                return true;
            }
        });
    }

    private void initUnit() {
        final ListPreference unitPreference = getUnitPreference();
        String[] unitsForSubjectDistance = MeasurementUnit.getUnitsForSubjectDistanceAsString();
        unitPreference.setEntries(unitsForSubjectDistance);
        unitPreference.setEntryValues(unitsForSubjectDistance);
        unitPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object o) {
                onUnitSelected((String) o);
                return true;
            }
        });
    }

    private void onUnitSelected(String selectedUnit) {
        getUnitPreference().setSummary(selectedUnit);
    }

    private void initCamera() {
        final ListPreference cameraPreference = getCameraPreference();
        cameraPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object camera) {
                onCameraSelected((String)camera);
                return true;
            }
        });
    }

    private ListPreference getManufacturerPreference() {
        return (ListPreference) super.findPreference(getString(R.string.manufacturer_setting_key));
    }

    private ListPreference getCameraPreference() {
        return (ListPreference) super.findPreference(getString(R.string.camera_setting_key));
    }
    private ListPreference getUnitPreference() {
        return (ListPreference) super.findPreference(getString(R.string.unit_setting_key));
    }

    private void onManufacturerSelected(String selectedManufacturer) {
        // set the label as the selected manufacturer preference
        ListPreference manufacturerPref = getManufacturerPreference();
        manufacturerPref.setSummary(selectedManufacturer);

        String[] cameraArray = fromCameraList(CameraData.getCameraByManufacturer(CameraData.Manufacturer.valueOf(selectedManufacturer)));
        ListPreference cameraPref = getCameraPreference();
        cameraPref.setEntries(cameraArray);
        cameraPref.setEntryValues(cameraArray);
        cameraPref.setEnabled(true);
        onCameraSelected(cameraPref.getValue());
    }

    private void onCameraSelected(String selectedCamera) {
        ListPreference cameraPreference = getCameraPreference();
        cameraPreference.setSummary(selectedCamera);
    }

    private String[] fromCameraList(List<CameraData> cameraData) {
        List<String> result = new ArrayList<String>();
        for (CameraData data : cameraData) {
            result.add(data.getLabel());
        }
        return result.toArray(new String[result.size()]);
    }
}