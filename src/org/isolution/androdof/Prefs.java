package org.isolution.androdof;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        if (getManufacturerPreference().getValue() != null) {
            initCameraValues(getManufacturerPreference().getValue());
        }
    }

    private void initManufacturer() {
        ListPreference manufacturerPref = getManufacturerPreference();
        String[] manufacturers = CameraData.getManufacturersAsString();
        manufacturerPref.setEntries(manufacturers);
        manufacturerPref.setEntryValues(manufacturers);
        manufacturerPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object selectedManufacturer) {
                initCameraValues((String)selectedManufacturer);
                 new AlertDialog.Builder(Prefs.this)
                    .setTitle("Notice")
                    .setMessage("Please remember to select default camera.").show();
                return true;
            }
        });
    }

    private ListPreference getManufacturerPreference() {
        return (ListPreference) super.findPreference(getString(R.string.manufacturer_setting_key));
    }

    private void initCameraValues(String selectedManufacturer) {
        CameraData.Manufacturer manufacturer = CameraData.Manufacturer.valueOf(selectedManufacturer);
        ListPreference cameraPref = (ListPreference) super.findPreference(getString(R.string.camera_setting_key));
        String[] cameraArray = fromCameraList(CameraData.getCameraByManufacturer(manufacturer));
        cameraPref.setEntries(cameraArray);
        cameraPref.setEntryValues(cameraArray);
    }

    private String[] fromCameraList(List<CameraData> cameraData) {
        List<String> result = new ArrayList<String>();
        for (CameraData data : cameraData) {
            result.add(data.getLabel());
        }
        return result.toArray(new String[result.size()]);
    }
}