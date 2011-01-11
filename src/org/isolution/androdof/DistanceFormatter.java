package org.isolution.androdof;

import java.math.BigDecimal;

import static org.isolution.androdof.UnitConverter.fromMillimetersToFeet;
import static org.isolution.androdof.UnitConverter.fromMillimetersToMeters;

/**
 * User: agwibowo
 * Date: 11/01/11
 * Time: 10:45 PM
 */
public class DistanceFormatter {

    /**
     * Format the given millimeters value in the given {@link MeasurementUnit}
     *
     * @param millimetersValue value in millimeters
     * @param measurementUnit measurement unit to use. If null, then use {@link MeasurementUnit#DEFAULT_MEASUREMENT_UNIT}
     * @return value in the requested measurement unit, or 'infinite' if value is less than zero
     */
    public static String format(BigDecimal millimetersValue, MeasurementUnit measurementUnit) {
        if (millimetersValue == null) {
            millimetersValue = BigDecimal.ZERO;
        }
        if (measurementUnit == null) {
            measurementUnit = MeasurementUnit.DEFAULT_MEASUREMENT_UNIT;
        }
        BigDecimal value = millimetersValue;
        switch (measurementUnit) {
            case METER:
                value = fromMillimetersToMeters(millimetersValue);
                break;
            case FEET:
                value = fromMillimetersToFeet(millimetersValue);
                break;
            case MILLIMETER:
                value = value.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                break;
        }
        return millimetersValue.intValue() < 0 ? "infinite" : value + " " + measurementUnit.getAbbrev();
    }

}
