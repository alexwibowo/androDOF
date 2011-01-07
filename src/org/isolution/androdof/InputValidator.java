package org.isolution.androdof;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 4/01/11
 * Time: 12:59 AM
 */
public class InputValidator {
    public static final String FOCUS_LENGTH_ERROR_MESSAGE = "focus length";

    public static final String SUBJECT_DISTANCE_ERROR_MESSAGE = "subject distance";

    private String subjectDistanceValue;

    private String focusLengthValue;

    private List<String> errors = new ArrayList<String>();

    private boolean valid;

    public InputValidator(String subjectDistanceValue, String focusLengthValue) {
        this.subjectDistanceValue = subjectDistanceValue;
        this.focusLengthValue = focusLengthValue;
        this.valid = validate();
    }

    private boolean validate() {
        boolean validFocusLength = validateFocusLength();
        boolean validSubjectDistance = validateSubjectDistance();
        return validFocusLength && validSubjectDistance;
    }

    private boolean validateSubjectDistance() {
        if (subjectDistanceValue== null || subjectDistanceValue.trim().length() == 0) {
            errors.add("Must supply subject distance");
            return false;
        }
        try {
            new BigDecimal(subjectDistanceValue);
        } catch (NumberFormatException e) {
            errors.add("Must supply numeric subject distance value");
            return false;
        }
        return true;
    }


    private boolean validateFocusLength() {
        if (focusLengthValue == null || focusLengthValue.trim().length() == 0) {
            errors.add("Must supply focus length");
            return false;
        }
        try {
            new BigDecimal(focusLengthValue);
        } catch (NumberFormatException e) {
            errors.add("Must supply numeric focus length value");
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
