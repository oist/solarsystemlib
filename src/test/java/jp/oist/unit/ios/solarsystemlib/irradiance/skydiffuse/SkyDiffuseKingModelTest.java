/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.irradiance.skydiffuse;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.atomsphere.airmass.Airmass;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.collection.ModelCollection;
import jp.oist.unit.ios.solarsystemlib.irradiance.Irradiance;
import jp.oist.unit.ios.solarsystemlib.irradiance.dni.DniErbsModel;
import jp.oist.unit.ios.solarsystemlib.irradiance.dni.DniModel;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class SkyDiffuseKingModelTest extends TestCase {

    private ModelCollection models = new DefaultModelCollection() {
        @Override
        public DniErbsModel getDniModel() {
            return new DniErbsModel(this);
        }
        @Override
        public SkyDiffuseModel getSkyDiffuseModel() {
            return new SkyDiffuseKingModel(this);
        }
    };

    public void testEstimate() {
        ZonedDateTime ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
        Location loc = new Location(26.462, 127.831, 42.962);
        double ghi = 272.0;
        double pressure = 100720.0;
        double tempAir = 30.7;

        double surfaceTilt = 27.5;
        double surfaceAzimuth = 225.0;

        DniModel erbs = models.getDniModel();
        SolarPosition.Variable sp = loc.getSolarPosition(ts, pressure, tempAir);
        Irradiance.Variable irrad = erbs.estimate(ts, ghi, sp.getApparentZenith());
        Airmass am = loc.getAirmass(sp);

        SkyDiffuseModel king = models.getSkyDiffuseModel();
        double skyDiffuse = king.estimate(surfaceTilt, surfaceAzimuth, sp, irrad, am);

        assertEquals(0, TestUtils.compareTo(252.56612115422996, skyDiffuse, 6));
    }
}