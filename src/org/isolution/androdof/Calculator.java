package org.isolution.androdof;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: agwibowo
 * Date: 3/01/11
 * Time: 1:11 AM
 */
public class Calculator {
    /** Subject distance, in millimeters */
    private BigDecimal subjectDistance;

    /** F-stop. e.g.: 1.4, 1.8, 2, 2.8, 4, 5.6, etc*/
    private BigDecimal aperture;

    /** Focus length, in millimeters */
    private BigDecimal focusLength;

    /** Circle of confusion for the camera, in millimeters */
    private BigDecimal coc;

    /** Unit for measurement */
    private MeasurementUnit unit= MeasurementUnit.DEFAULT_MEASUREMENT_UNIT;

    /** Calculation result */
    public Calculator() {
    }

    public Result calculate() {
        Result result = new Result();
        result.setHyperFocalDistance(calculateHyperFocalDistance());
        result.setNearDistance(calculateNearDistance(result.getHyperFocalDistance()));
        result.setFarDistance(calculateFarDistance(result.getHyperFocalDistance()));
        result.setTotal(result.getFarDistance().subtract(result.getNearDistance()));
        result.setSubjectDistance(subjectDistance);
        result.setInFrontOfSubject(result.getSubjectDistance().subtract(result.getNearDistance()));
        result.setBehindSubject(result.getFarDistance().subtract(result.getSubjectDistance()));
        result.setInFrontOfSubjectForHyperfocal(calculateNearDistanceForHyperfocal(result.getHyperFocalDistance()));
        result.setCoc(coc);
        return result;
    }

    public BigDecimal calculateHyperFocalDistance() {
        BigDecimal fSquare = focusLength.pow(2);
        BigDecimal nc = aperture.multiply(coc);
        return (fSquare.divide(nc, RoundingMode.HALF_EVEN)).add(focusLength);
    }

    public BigDecimal calculateNearDistance(BigDecimal hyperFocalDistance) {
        BigDecimal top = subjectDistance.multiply(hyperFocalDistance.subtract(focusLength));
        BigDecimal twiceFocusLength = new BigDecimal(2).multiply(focusLength);
        BigDecimal bottom = hyperFocalDistance.add(subjectDistance).subtract(twiceFocusLength);
        return top.divide(bottom, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateNearDistanceForHyperfocal(BigDecimal hyperFocalDistance) {
        BigDecimal top = hyperFocalDistance.multiply(hyperFocalDistance.subtract(focusLength));
        BigDecimal twiceFocusLength = new BigDecimal(2).multiply(focusLength);
        BigDecimal bottom = hyperFocalDistance.add(hyperFocalDistance).subtract(twiceFocusLength);
        return top.divide(bottom, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateFarDistance(BigDecimal hyperFocalDistance) {
        BigDecimal top = subjectDistance.multiply(hyperFocalDistance.subtract(focusLength));
        BigDecimal bottom = hyperFocalDistance.subtract(subjectDistance);
        return top.divide(bottom, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getSubjectDistance() {
        return subjectDistance;
    }

    public Calculator withSubjectDistance(BigDecimal subjectDistance) {
        this.subjectDistance = subjectDistance;
        return this;
    }

    public BigDecimal getAperture() {
        return aperture;
    }

    public Calculator withAperture(BigDecimal aperture) {
        this.aperture = aperture;
        return this;
    }

    public BigDecimal getFocusLength() {
        return focusLength;
    }

    public Calculator withFocusLength(BigDecimal focusLength) {
        this.focusLength = focusLength;
        return this;
    }

    public BigDecimal getCoc() {
        return coc;
    }

    public Calculator withCoc(BigDecimal coc) {
        this.coc = coc;
        return this;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }

    public void setUnit(MeasurementUnit unit) {
        this.unit = unit;
    }

    /**
     * A class to encapsulate calculation result
     */
    public static class Result {
        private BigDecimal subjectDistance;
        private BigDecimal hyperFocalDistance;
        private BigDecimal nearDistance;
        private BigDecimal farDistance;
        private BigDecimal total;
        private BigDecimal coc;
        private BigDecimal inFrontOfSubjectForHyperfocal;
        private BigDecimal inFrontOfSubject;
        private BigDecimal behindSubject;

        public BigDecimal getHyperFocalDistance() {
            return hyperFocalDistance;
        }

        public void setHyperFocalDistance(BigDecimal hyperFocalDistance) {
            this.hyperFocalDistance = hyperFocalDistance;
        }

        public BigDecimal getNearDistance() {
            return nearDistance;
        }

        public void setNearDistance(BigDecimal nearDistance) {
            this.nearDistance = nearDistance;
        }

        public BigDecimal getFarDistance() {
            return farDistance;
        }

        public void setFarDistance(BigDecimal farDistance) {
            this.farDistance = farDistance;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getSubjectDistance() {
            return subjectDistance;
        }

        public void setSubjectDistance(BigDecimal subjectDistance) {
            this.subjectDistance = subjectDistance;
        }

        public BigDecimal getCoc() {
            return coc;
        }

        public void setCoc(BigDecimal coc) {
            this.coc = coc;
        }

        public BigDecimal getInFrontOfSubject() {
            return inFrontOfSubject;
        }

        public void setInFrontOfSubject(BigDecimal inFrontOfSubject) {
            this.inFrontOfSubject = inFrontOfSubject;
        }

        public BigDecimal getBehindSubject() {
            return behindSubject;
        }

        public void setBehindSubject(BigDecimal behindSubject) {
            this.behindSubject = behindSubject;
        }

        public BigDecimal getInFrontOfSubjectForHyperfocal() {
            return inFrontOfSubjectForHyperfocal;
        }

        public void setInFrontOfSubjectForHyperfocal(BigDecimal inFrontOfSubjectForHyperfocal) {
            this.inFrontOfSubjectForHyperfocal = inFrontOfSubjectForHyperfocal;
        }
    }
}
