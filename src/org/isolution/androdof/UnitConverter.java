package org.isolution.androdof;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: agwibowo
 * Date: 9/01/11
 * Time: 6:34 PM
 */
public class UnitConverter {

    public static BigDecimal fromMillimetersToFeet(BigDecimal value) {
        return value.multiply(new BigDecimal("0.00328083989501312")).setScale(3, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal fromFeetToMillimeters(BigDecimal value) {
        return value.multiply(new BigDecimal("304.8"));
    }

    public static BigDecimal fromMillimetersToMeters(BigDecimal value) {
        return value.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal fromMeterToMillimeters(BigDecimal value) {
        return value.multiply(new BigDecimal("1000"));
    }

}
