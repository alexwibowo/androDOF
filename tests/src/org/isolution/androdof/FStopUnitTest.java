package org.isolution.androdof;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 7/01/11
 * Time: 1:24 PM
 */
public class FStopUnitTest {

    @Test
    public void verify_fstop_value() {
        for (FStop fStop : FStop.getAllFStops()) {
            assertThat(fStop.getValue(), not(nullValue()));
            assertThat(fStop.getValue(), greaterThan(new BigDecimal(0)));
        }
    }

    @Test
    public void verify_fstop_label() {
           for (FStop fStop : FStop.getAllFStops()) {
               BigDecimal labelAsNumber = new BigDecimal(fStop.getLabel());
               assertThat(labelAsNumber.doubleValue(), closeTo(fStop.getValue().doubleValue(), 0.7));
           }
    }

}
