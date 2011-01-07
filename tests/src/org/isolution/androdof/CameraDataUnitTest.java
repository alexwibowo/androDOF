package org.isolution.androdof;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 7/01/11
 * Time: 12:37 PM
 */
public class CameraDataUnitTest {

    @Test
    public void should_have_one_or_more_manufacturers() {
        CameraData.Manufacturer[] manufacturers = CameraData.getManufacturers();
        assertThat(manufacturers, not(nullValue()));
        assertThat(manufacturers.length, greaterThan(0));
    }

    @Test
    public void should_only_return_unique_manufacturers() {
        CameraData.Manufacturer[] manufacturers = CameraData.getManufacturers();
        Set<CameraData.Manufacturer> uniq = new HashSet<CameraData.Manufacturer>(Arrays.asList(manufacturers));
        assertThat(uniq.size(), is(manufacturers.length));
    }

    @Test
    public void should_be_able_to_return_manufacturers_as_string() {
        String[] manufacturersString = CameraData.getManufacturersAsString();
        CameraData.Manufacturer[] manufacturersEnum = CameraData.getManufacturers();
        assertThat(manufacturersString.length, is(manufacturersEnum.length));
    }

    @Test
    public void verify_camera_data_manufacturer() {
        for (CameraData.Manufacturer manufacturer : CameraData.Manufacturer.values()) {
            List<CameraData> cameras = CameraData.getCameraByManufacturer(manufacturer);
            for (CameraData camera : cameras) {
                assertThat(camera.getManufacturer(), is(manufacturer));
            }
        }
    }

    @Test
    public void verify_camera_data_coc() {
        for (CameraData.Manufacturer manufacturer : CameraData.Manufacturer.values()) {
            List<CameraData> cameras = CameraData.getCameraByManufacturer(manufacturer);
            for (CameraData camera : cameras) {
                assertThat(camera.getCoc(), not(nullValue()));
                assertThat(camera.getCoc(), greaterThan(new BigDecimal(0)));
                assertThat(camera.getCoc(), lessThan(new BigDecimal(1)));
            }
        }
    }
}
