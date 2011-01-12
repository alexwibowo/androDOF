package org.isolution.androdof;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 6/01/11
 * Time: 2:16 PM
 */
public class FStop {
    private final String label;
    private final BigDecimal value;

    private static final List<FStop> DATABASE = new ArrayList<FStop>();

    static {
        DATABASE.add(new FStop("1", new BigDecimal("1")));
        DATABASE.add(new FStop("1.2", new BigDecimal("1.189207")));
        DATABASE.add(new FStop("1.4", new BigDecimal("1.414214")));
        DATABASE.add(new FStop("1.6", new BigDecimal("1.587401")));
        DATABASE.add(new FStop("1.7", new BigDecimal("1.681793")));
        DATABASE.add(new FStop("1.8", new BigDecimal("1.781797")));
        DATABASE.add(new FStop("2", new BigDecimal("2.000000")));
        DATABASE.add(new FStop("2.2", new BigDecimal("2.244924")));
        DATABASE.add(new FStop("2.4", new BigDecimal("2.378414")));
        DATABASE.add(new FStop("2.5", new BigDecimal("2.519842")));
        DATABASE.add(new FStop("2.8", new BigDecimal("2.828427")));
        DATABASE.add(new FStop("3.2", new BigDecimal("3.174802")));
        DATABASE.add(new FStop("3.4", new BigDecimal("3.363586")));
        DATABASE.add(new FStop("3.6", new BigDecimal("3.563595")));
        DATABASE.add(new FStop("4", new BigDecimal("4.000000")));
        DATABASE.add(new FStop("4.5", new BigDecimal("4.489848")));
        DATABASE.add(new FStop("4.8", new BigDecimal("4.756828")));
        DATABASE.add(new FStop("5", new BigDecimal("5.039684")));
        DATABASE.add(new FStop("5.6", new BigDecimal("5.656854")));
        DATABASE.add(new FStop("6.4", new BigDecimal("6.349604")));
        DATABASE.add(new FStop("6.7", new BigDecimal("6.727171")));
        DATABASE.add(new FStop("7.1", new BigDecimal("7.127190")));
        DATABASE.add(new FStop("8", new BigDecimal("8.000000")));
        DATABASE.add(new FStop("9", new BigDecimal("8.979696")));
        DATABASE.add(new FStop("9.5", new BigDecimal("9.513657")));
        DATABASE.add(new FStop("10", new BigDecimal("10.07937")));
        DATABASE.add(new FStop("11", new BigDecimal("11.313708")));
        DATABASE.add(new FStop("12.7", new BigDecimal("12.699208")));
        DATABASE.add(new FStop("13.5", new BigDecimal("13.454343")));
        DATABASE.add(new FStop("14.3", new BigDecimal("14.254379")));
        DATABASE.add(new FStop("16", new BigDecimal("16.000000")));
        DATABASE.add(new FStop("18", new BigDecimal("17.959393")));
        DATABASE.add(new FStop("19", new BigDecimal("19.027314")));
        DATABASE.add(new FStop("20", new BigDecimal("20.158737")));
        DATABASE.add(new FStop("22", new BigDecimal("22.627417")));
        DATABASE.add(new FStop("25", new BigDecimal("25.398417")));
        DATABASE.add(new FStop("27", new BigDecimal("26.908685")));
        DATABASE.add(new FStop("28", new BigDecimal("28.508759")));
        DATABASE.add(new FStop("32", new BigDecimal("32")));
        DATABASE.add(new FStop("45", new BigDecimal("45.254834")));
        DATABASE.add(new FStop("64", new BigDecimal("64")));
    }

    public FStop(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public static List<FStop> getAllFStops() {
        return DATABASE;
    }

    public static FStop getFStop(String value) {
        for (FStop fStop : DATABASE) {
            if (fStop.getLabel().equals(value)) {
                return fStop;
            }
        }
        throw new IllegalArgumentException("Unknown fstop value: " + value);
    }
}
