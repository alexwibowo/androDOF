package org.isolution.androdof;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.isolution.androdof.DistanceFormatter.format;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 11/01/11
 * Time: 11:40 PM
 */
public class DistanceFormatterUnitTest {

    @Test
    public void should_display_infinite_for_negative_number() {
        String formatted = format(new BigDecimal("-1"), MeasurementUnit.DEFAULT_MEASUREMENT_UNIT);
        assertThat(formatted, is("infinite"));
    }

    @Test
    public void test_zero_value() {
        assertThat(format(new BigDecimal("0"), MeasurementUnit.FEET), is("0.000 ft"));
        assertThat(format(new BigDecimal("0"), MeasurementUnit.METER), is("0.000 m"));
        assertThat(format(new BigDecimal("0"), MeasurementUnit.MILLIMETER), is("0.000 mm"));
    }

    @Test
    public void should_display_meter_value_if_meter_is_requested() {
        assertThat(format(new BigDecimal("1"), MeasurementUnit.METER), is("0.001 m"));
    }

    @Test
    public void should_display_feet_value_if_feet_is_requested() {
        assertThat(format(new BigDecimal("1"), MeasurementUnit.FEET), is("0.003 ft"));
    }

    @Test
    public void should_display_null_as_zero() {
        assertThat(format(null, MeasurementUnit.FEET), is("0.000 ft"));
    }

    @Test
    public void should_use_default_measurement_unit_if_not_given() {
        String formatted = format(new BigDecimal("1"), null);
        assertThat(formatted, is("0.001 m"));

    }
}
