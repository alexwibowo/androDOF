package org.isolution.androdof;

/**
 * Generic utility methods for String class. Attempt to mimic apache's common lang, but
 * try to be as slim as possible. Might deprecate this if the class becoming large.
 *
 * User: agwibowo
 * Date: 11/01/11
 * Time: 10:42 PM
 */
public class StringUtils {

    /**
     * @param value string to test
     * @return true if the string is null or blank.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
