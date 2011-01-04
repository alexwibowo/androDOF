package org.isolution.androdof;

import java.math.BigDecimal;
import java.util.*;

import static org.isolution.androdof.CameraData.Manufacturer.CANON;
import static org.isolution.androdof.CameraData.Manufacturer.NIKON;
import static org.isolution.androdof.CameraData.Manufacturer.SONY;

/**
 * User: agwibowo
 * Date: 4/01/11
 * Time: 1:35 AM
 */
public class CameraData {
    private Manufacturer manufacturer;
    private String label;
    private BigDecimal coc;

    private static final Map<Manufacturer, List<CameraData>> DATABASE  = new HashMap<Manufacturer, List<CameraData>>();

    public static enum Manufacturer{
        CANON,
        NIKON,
        SONY
    }

    static {
        List<CameraData> canonCameras = new ArrayList<CameraData>();
        canonCameras.add(new CameraData(CANON, "50D, 40D, 30D, 20D, 20Da, 10D", new BigDecimal("0.019")));
        canonCameras.add(new CameraData(CANON, "D60, D30", new BigDecimal("0.019")));
        canonCameras.add(new CameraData(CANON, "Digital Rebel, XS, XSi, XT, XTi, T1i", new BigDecimal("0.019")));
        canonCameras.add(new CameraData(CANON, "1000D, 500D, 450D, 400D, 350D, 300D", new BigDecimal("0.019")));
        canonCameras.add(new CameraData(CANON, "1D (Mark II, Mark II N, Mark III, Mark IV)", new BigDecimal("0.023")));
        canonCameras.add(new CameraData(CANON, "1Ds (Mark II, Mark III)", new BigDecimal("0.030")));
        canonCameras.add(new CameraData(CANON, "5D (Mark II)", new BigDecimal("0.030")));
        DATABASE.put(CANON, canonCameras);


        List<CameraData> nikonCameras = new ArrayList<CameraData>();
        nikonCameras.add(new CameraData(NIKON, "D300, D300s, D200, D100", new BigDecimal("0.020")));
        nikonCameras.add(new CameraData(NIKON, "D2X, D2Xs, D2H, D2Hs, D1H, D1X", new BigDecimal("0.020")));
        nikonCameras.add(new CameraData(NIKON, "D90, D80, D70, D70s, D60, D50, D40, D40x", new BigDecimal("0.020")));
        nikonCameras.add(new CameraData(NIKON, "D3x, D3s, D3, D700", new BigDecimal("0.030")));
        nikonCameras.add(new CameraData(NIKON, "D5000, D3000", new BigDecimal("0.020")));
        DATABASE.put(NIKON, nikonCameras);

        List<CameraData> sonyCameras = new ArrayList<CameraData>();
        sonyCameras.add(new CameraData(SONY, "A380, A350, A330, A300, A230, A200, A100", new BigDecimal("0.020")));
        sonyCameras.add(new CameraData(SONY, "A700, A550, A500", new BigDecimal("0.020")));
        sonyCameras.add(new CameraData(SONY, "A900, A850", new BigDecimal("0.030")));
        DATABASE.put(SONY, sonyCameras);
    }


    public CameraData(Manufacturer manufacturer,String label, BigDecimal coc) {
        this.manufacturer = manufacturer;
        this.label = label;
        this.coc = coc;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getCoc() {
        return coc;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public static Manufacturer[] getManufacturers() {
        return Manufacturer.values();
    }

    public static List<CameraData> getCameraByManufacturer(Manufacturer manufacturer) {
        return Collections.unmodifiableList(DATABASE.get(manufacturer));
    }

    public static BigDecimal getCocForCamera(Manufacturer manufacturer, String camera) {
        List<CameraData> camerasForManufacturer = DATABASE.get(manufacturer);
        for (CameraData cameraData : camerasForManufacturer) {
            if (cameraData.getLabel().equals(camera)) {
                return cameraData.getCoc();
            }
        }
        return null;
    }
}
