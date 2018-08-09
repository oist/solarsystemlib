/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.irradiance.aoi;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.irradiance.Irradiance;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class AoiLossAshraeiamModelTest extends TestCase {

    public void testEstimate() {
        int scale = 6;
        Location loc = new Location(26.462, 127.831, 42.962);
        double pressure = 100720;
        double tempAir = 30.7;
        double surfaceTilt = 27.5;
        double surfaceAzimuth = 225.0;

        SolarPosition.Variable sp;
        ZonedDateTime ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");

        sp = loc.getSolarPosition(ts, pressure, tempAir);
        Double aoi = Irradiance.aoi(surfaceTilt, surfaceAzimuth, sp.getApparentZenith(), sp.getAzimuth());
        AoiLossAshraeiamModel model = new AoiLossAshraeiamModel(null);
        Double aoiLoss = model.estimate(aoi);
        assertEquals(0, TestUtils.compareTo(0.996660, aoiLoss, scale));
    }
}