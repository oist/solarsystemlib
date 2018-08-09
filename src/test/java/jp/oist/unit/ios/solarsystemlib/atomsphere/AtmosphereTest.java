/* vim : set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.atomsphere;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.atomsphere.airmass.Airmass;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AtmosphereTest extends TestCase {

    private static ZoneId TZ = ZoneId.of("Asia/Tokyo");

    public Location loc = new Location(26.462, 127.831, 42.962);
    public double pressure = 100720;
    public double tempAir = 30.7;

    public void setUp() {
    }

    public void testGetAirmass() {
        int scale = 6;
        ZonedDateTime ts = ZonedDateTime.of(2017,7, 10, 13, 0, 1, 0, TZ);
        SolarPosition.Variable sp = loc.getSolarPosition(ts, pressure, tempAir);

        Atmosphere atmosphere = new Atmosphere(new DefaultModelCollection());
        Airmass am = atmosphere.getAirmass(sp, loc.altitude);

        assertEquals(0, TestUtils.compareTo(am.getRelativeAirmass(), 1.007802, scale));
        assertEquals(0, TestUtils. compareTo(am.getAbsoluteAirmass(),1.002679, scale));
    }
}