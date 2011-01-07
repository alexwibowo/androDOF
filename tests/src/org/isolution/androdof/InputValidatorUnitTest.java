package org.isolution.androdof;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 7/01/11
 * Time: 2:12 PM
 */
public class InputValidatorUnitTest {

    @Test
    public void should_fail_when_focusLength_is_null() {
        InputValidator inputValidator = new InputValidator("5.1", null);
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply")));
    }

    @Test
    public void should_fail_when_focusLength_is_blank() {
        InputValidator inputValidator = new InputValidator("5.1", " ");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply")));
    }

    @Test
    public void should_fail_when_focusLength_is_not_numeric() {
        InputValidator inputValidator = new InputValidator("5.1", "abcdef");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply numeric")));
    }

    @Test
    public void should_fail_when_focusLength_is_an_invalid_number() {
        InputValidator inputValidator = new InputValidator("5.1", "5.1.2");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.FOCUS_LENGTH_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply numeric")));
    }

    @Test
    public void  should_fail_when_subjectDistance_is_null() {
        InputValidator inputValidator = new InputValidator(null, "5.1");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply")));
    }

    @Test
    public void should_fail_when_subjectDistance_is_blank() {
        InputValidator inputValidator = new InputValidator(" ", "5.1");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply")));
    }

    @Test
    public void should_fail_when_subjectDistance_is_not_numeric() {
        InputValidator inputValidator = new InputValidator("abcdef", "5.1");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply numeric")));
    }

    @Test
    public void should_fail_when_subjectDistance_is_an_invalid_number() {
        InputValidator inputValidator = new InputValidator("5.1.2", "5.1");
        assertThat(inputValidator.isValid(), is(false));
        assertThat(inputValidator.getErrors(), hasItem(containsString(InputValidator.SUBJECT_DISTANCE_ERROR_MESSAGE)));
        assertThat(inputValidator.getErrors(), hasItem(containsString("Must supply numeric")));
    }


}
