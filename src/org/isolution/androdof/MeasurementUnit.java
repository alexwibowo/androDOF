package org.isolution.androdof;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of measurement unit. Implements serializable so that we can serialize in the application bundle
 * during state transition.
 *
 * User: agwibowo
 * Date: 9/01/11
 * Time: 6:26 PM
 */
public enum MeasurementUnit implements Serializable{
    MILLIMETER("millimeter","mm"),
    FEET("feet","ft"),
    METER("meter","m");

    static final MeasurementUnit DEFAULT_MEASUREMENT_UNIT = METER;

    private final String label;
    private final String abbrev;

    MeasurementUnit(String label,String abbrev) {
        this.label = label;
        this.abbrev = abbrev;
    }

    public String getLabel() {
        return label;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public static MeasurementUnit[] getUnitsForSubjectDistance() {
        return new MeasurementUnit[]{FEET, METER};
    }

    public static String[] getUnitsForSubjectDistanceAsString() {
        List<String> result = new ArrayList<String>();
        for (MeasurementUnit measurementUnit : getUnitsForSubjectDistance()) {
            result.add(measurementUnit.getAbbrev());
        }
        return result.toArray(new String[result.size()]);
    }

    public static MeasurementUnit fromAbbrev(String abbrev) {
        for (MeasurementUnit measurementUnit : values()) {
            if (measurementUnit.getAbbrev().equals(abbrev)) {
                return measurementUnit;
            }
        }
        throw new IllegalArgumentException("Invalid measurement unit : " + abbrev);
    }

    @Override
    public String toString() {
        return abbrev;
    }
}
