/* vim: set sw=4 ts=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.irradiance;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.atomsphere.airmass.Airmass;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.collection.ModelCollection;
import jp.oist.unit.ios.solarsystemlib.irradiance.extraradiation.ExtraRadiationModel;
import jp.oist.unit.ios.solarsystemlib.irradiance.extraradiation.ExtraRadiationSpencerModel;
import jp.oist.unit.ios.solarsystemlib.irradiance.skydiffuse.SkyDiffuseHaydaviesModel;
import jp.oist.unit.ios.solarsystemlib.irradiance.skydiffuse.SkyDiffuseModel;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class IrradianceTest extends TestCase {

    private ZonedDateTime ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
    private Location loc = new Location(26.462, 127.831, 42.962);
    private double ghi = 272.0;
    private double pressure = 100720.0;
    private double tempAir = 30.7;

    private double surfaceTilt = 27.5;
    private double surfaceAzimuth = 225.0;

    private SolarPosition.Variable pos;
    private Airmass airmass;

    private ModelCollection models = new DefaultModelCollection() {
        @Override
        public ExtraRadiationModel getExtraRadiationModel() {
            return new ExtraRadiationSpencerModel(this);
        }
        @Override
        public SkyDiffuseModel getSkyDiffuseModel() {
            return new SkyDiffuseHaydaviesModel(this);
        }
    };

    Irradiance irradiance = null;

    public void setUp() {
        irradiance = models.irradiance();
        pos = loc.getSolarPosition(ts, pressure, tempAir);
        airmass = loc.getAirmass(pos);
    }

    public void testExtraRadiation() {
        double dniExtra1  = irradiance.extraRadiation(ts);
        assertEquals(0, TestUtils.compareTo(1320.6414358435557, dniExtra1, 6));

        double dniExtra2  = irradiance.extraRadiation(ts, 1366.1);
        assertEquals(0, TestUtils.compareTo(1320.6414358435557, dniExtra2, 6));

        double dniExtra3  = irradiance.extraRadiation(ts, 1365.0);
        assertEquals(0, TestUtils.compareTo(1319.5780396211503, dniExtra3, 6));
    }

    public void testProjection() {
        double projection = Irradiance.projection(surfaceTilt, surfaceAzimuth,
                                                  pos.getApparentZenith(), pos.getAzimuth());
        assertEquals(0, TestUtils.compareTo(0.9373781864763406, projection, 6));
    }

    public void testAoi() {
        double aoi = Irradiance.aoi(surfaceTilt, surfaceAzimuth, pos.getApparentZenith(), pos.getAzimuth());
        assertEquals(0, TestUtils.compareTo(20.384181956421706, aoi, 6));
    }

    public void testGetIrradiance() {
        Irradiance.Variable irrad = irradiance.getIrradiance(pos, ghi);
        assertEquals(0, TestUtils.compareTo(5.124277371496872, irrad.dni, 6));
        assertEquals(0, TestUtils.compareTo(266.9170713454702, irrad.dhi, 6));
        assertEquals(0, TestUtils.compareTo(0.207635974449745, irrad.kt, 6));
    }

    public void testBeamComponent() {
        Irradiance.Variable irrad = irradiance.getIrradiance(pos, ghi);
        double beamComponent1 = Irradiance.beamComponent(surfaceTilt, surfaceAzimuth, pos, irrad);
        assertEquals(0, TestUtils.compareTo(4.803385829495487, beamComponent1, 6));

        double beamComponent2 = Irradiance.beamComponent(surfaceTilt, surfaceAzimuth, pos, irrad.dni);
        assertEquals(0, TestUtils.compareTo(4.803385829495487, beamComponent2, 6));
    }

    public void testGetPoaIrradiance() {
        Irradiance.Variable irrad = irradiance.getIrradiance(pos, ghi);
        Irradiance.PoaVariable poa;

        Irradiance.SurfaceType surfaceType;
        surfaceType = Irradiance.SurfaceType.GRASS;
        poa = irradiance.getPoaIrradiance(surfaceTilt, surfaceAzimuth, pos, irrad, airmass, surfaceType);
        assertEquals(0, TestUtils.compareTo(259.71594538504115, poa.global, 6));
        assertEquals(0, TestUtils.compareTo(4.803385829495487, poa.direct, 6));
        assertEquals(0, TestUtils.compareTo(254.91255955554567, poa.diffuse, 6));
        assertEquals(0, TestUtils.compareTo(251.8392542179933, poa.skyDiffuse, 6));
        assertEquals(0, TestUtils.compareTo(3.0733053375523696, poa.groundDiffuse, 6));

        surfaceType = Irradiance.SurfaceType.SNOW;
        poa = irradiance.getPoaIrradiance(surfaceTilt, surfaceAzimuth, pos, irrad, airmass, surfaceType);
        assertEquals(0, TestUtils.compareTo(266.63088239453396, poa.global, 6));
        assertEquals(0, TestUtils.compareTo(4.803385829495487, poa.direct, 6));
        assertEquals(0, TestUtils.compareTo(261.8274965650385, poa.diffuse, 6));
        assertEquals(0, TestUtils.compareTo(251.8392542179933, poa.skyDiffuse, 6));
        assertEquals(0, TestUtils.compareTo(9.988242347045201, poa.groundDiffuse, 6));

        surfaceType = Irradiance.SurfaceType.ASPHALT;
        poa = irradiance.getPoaIrradiance(surfaceTilt, surfaceAzimuth, pos, irrad, airmass, surfaceType);
        assertEquals(0, TestUtils.compareTo(258.4866232500202, poa.global, 6));
        assertEquals(0, TestUtils.compareTo(4.803385829495487, poa.direct, 6));
        assertEquals(0, TestUtils.compareTo(253.68323742052473, poa.diffuse, 6));
        assertEquals(0, TestUtils.compareTo(251.8392542179933, poa.skyDiffuse, 6));
        assertEquals(0, TestUtils.compareTo(1.8439832025314216, poa.groundDiffuse, 6));
    }
}