package org.isolution.androdof;

import android.text.Editable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 4/01/11
 * Time: 12:59 AM
 */
public class InputValidator {
    public static final String APERTURE_ERROR_MESSAGE = "aperture";
    public static final String FOCUS_LENGTH_ERROR_MESSAGE = "focus length";
    public static final String SUBJECT_DISTANCE_ERROR_MESSAGE = "subject distance";

    private Editable subjectDistanceValue;
    private Editable focusLengthValue;
    private Editable apertureValue;
    private List<String> errors = new ArrayList<String>();
    private boolean valid;

    public InputValidator(Editable subjectDistanceValue, Editable focusLengthValue, Editable apertureValue) {
        this.subjectDistanceValue = subjectDistanceValue;
        this.focusLengthValue = focusLengthValue;
        this.apertureValue = apertureValue;
        this.valid = validate();
    }

    private boolean validate() {
        boolean validAperture = validateApertureValue();
        boolean validFocusLength = validateFocusLength();
        boolean validSubjectDistance = validateSubjectDistance();
        return validAperture && validFocusLength && validSubjectDistance;
    }

    private boolean validateSubjectDistance() {
        if (subjectDistanceValue.toString() == null || subjectDistanceValue.toString().trim().length() == 0) {
            errors.add("Must supply subject distance");
            return false;
        }
        try {
            new BigDecimal(subjectDistanceValue.toString());
        } catch (NumberFormatException e) {
            errors.add("Must supply numeric subject distance value");
            return false;
        }
        return true;
    }


    private boolean validateFocusLength() {
        if (focusLengthValue.toString() == null || focusLengthValue.toString().trim().length() == 0) {
            errors.add("Must supply focus length");
            return false;
        }
        try {
            new BigDecimal(focusLengthValue.toString());
        } catch (NumberFormatException e) {
            errors.add("Must supply numeric focus length value");
            return false;
        }
        return true;
    }

    private boolean validateApertureValue() {
        if (apertureValue.toString() == null || apertureValue.toString().trim().length() == 0) {
            errors.add("Must supply aperture");
            return false;
        }
        try {
            new BigDecimal(apertureValue.toString());
        } catch (NumberFormatException e) {
            errors.add("Must supply numeric aperture value");
            return false;
        }
        return true;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return valid;
    }
}
