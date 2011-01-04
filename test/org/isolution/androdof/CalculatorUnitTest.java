package org.isolution.androdof;

import java.math.BigDecimal;

/**
 * User: agwibowo
 * Date: 3/01/11
 * Time: 1:20 AM
 */
public class CalculatorUnitTest {

    public void test() {
        Calculator calculator = new Calculator();
        calculator.withFocusLength(new BigDecimal(50));
        calculator.withAperture(new BigDecimal(5.6));
        calculator.withCoc(new BigDecimal(0.02));
        calculator.withSubjectDistance(new BigDecimal(10000));
        System.out.println(calculator.calculateHyperFocalDistance());
//        System.out.println(calculator.calculateNearDistance(calculator.hyperFocalDistance));
//        System.out.println(calculator.calculateFarDistance(calculator.hyperFocalDistance));
    }

    public static void main(String[] args) {
        new BigDecimal("20.5.1");
        new CalculatorUnitTest().test();
    }
}
