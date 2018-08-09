/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.atomsphere.airmass;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class RelativeAirmassKastenYoung1989ModelTest extends TestCase {

    public Location loc = new Location(26.462, 127.831, 42.962);

    public void testEstimate() {
        RelativeAirmassKastenYoung1989Model model = new RelativeAirmassKastenYoung1989Model(null);
        int scale = 6;

        ZonedDateTime ts;
        SolarPosition.Variable sp;
        double pressure = 100720;
        double tempAir = 30.7;

        Double relativeAirmass;

        ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
        sp = loc.getSolarPosition(ts, pressure, tempAir);
        relativeAirmass = model.estimate(sp);
        assertEquals(0, TestUtils.compareTo(relativeAirmass, 1.007802, scale));

        ts = ZonedDateTime.parse("2017-07-10T21:00:01+09:00");
        sp = loc.getSolarPosition(ts, pressure, tempAir);
        relativeAirmass = model.estimate(sp);
        assertNull(relativeAirmass);
    }
}