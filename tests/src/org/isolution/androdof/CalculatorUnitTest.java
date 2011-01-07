package org.isolution.androdof;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 7/01/11
 * Time: 11:37 AM
 */
public class CalculatorUnitTest {

    private Calculator.Result result;

    @Before
    public void setup() {
        Calculator calculator = new Calculator();
        calculator.withAperture(new BigDecimal("5.656854")).withCoc(new BigDecimal("0.02")).withFocusLength(new BigDecimal("50")).withSubjectDistance(new BigDecimal("10000"));
        result = calculator.calculate();
    }

    private BigDecimal asMeter(BigDecimal asMillimeters, int scale) {
        return asMillimeters.divide(new BigDecimal(1000), scale, BigDecimal.ROUND_HALF_EVEN);
    }

    @Test
    public void should_calculate_hyperfocal_correctly() {
        assertThat(asMeter(result.getHyperFocalDistance(), 1), is(new BigDecimal("22.1")));
    }

    @Test
    public void should_calculate_near_limit_correctly() {
        assertThat(asMeter(result.getNearDistance(), 1), is(new BigDecimal("6.9")));
    }

    @Test
    public void should_calculate_far_limit_correctly() {
        assertThat(asMeter(result.getFarDistance(), 1), is(new BigDecimal("18.2")));
    }

    @Test
    public void should_calculate_total_distance_correctly() {
        assertThat(asMeter(result.getTotal(), 1), is(new BigDecimal("11.3")));
    }

    @Test
    public void should_calculate_infront_subject_correctly() {
        assertThat(asMeter(result.getInFrontOfSubject(), 1), is(new BigDecimal("3.1")));
    }

    @Test
    public void should_calculate_behind_subject_correctly() {
        assertThat(asMeter(result.getBehindSubject(), 1), is(new BigDecimal("8.2")));
    }
}
