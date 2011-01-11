package org.isolution.androdof;

import org.junit.Assert;
import org.junit.Test;

import static org.isolution.androdof.StringUtils.isBlank;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: agwibowo
 * Date: 11/01/11
 * Time: 11:32 PM
 */
public class StringUtilsUnitTest {

    @Test
    public void isBlank_should_return_true_for_null_string() {
        assertTrue(isBlank(null));
    }

    @Test
    public void isBlank_should_return_true_for_empty_string() {
        assertTrue(isBlank(""));
    }

    @Test
    public void isBlank_should_return_true_for_whitespaces_string() {
        assertTrue(isBlank("     "));
    }

    @Test
    public void isBlank_should_return_false_for_non_blank_string() {
        assertFalse(isBlank("alex"));
    }

    @Test
    public void isBlank_should_return_false_for_string_with_leading_and_trailing_whitespaces() {
        assertFalse(isBlank(" alex "));
    }


}
