package org.isolution.androdof;

import android.util.AndroidRuntimeException;

/**
 * User: agwibowo
 * Date: 12/01/11
 * Time: 9:37 PM
 */
public class UnknownCameraModelException extends AndroidRuntimeException {

    private final CameraData.Manufacturer manufacturer;
    private final String model;

    public UnknownCameraModelException(CameraData.Manufacturer manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public CameraData.Manufacturer getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String getMessage() {
        return String.format("Unknown camera model. Manufacturer: [%s], model: [%s]", manufacturer, model);
    }
}
