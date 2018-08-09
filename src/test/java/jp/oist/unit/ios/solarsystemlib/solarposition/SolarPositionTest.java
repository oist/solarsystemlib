/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.solarposition;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

public class SolarPositionTest extends TestCase {

    private static ZoneId TZ = ZoneId.of("Asia/Tokyo");
    private static MathContext PRECISION = new MathContext(6);

    private ZonedDateTime curTime = null;

    private long timestamp = 1532587960;
    private double latitude = 26.462;
    private double longitude = 127.831;

    public void setUp() {
        Instant instant = Instant.ofEpochSecond(timestamp);
        curTime = ZonedDateTime.ofInstant(instant, TZ);
    }

    public static int compareTo(double v1, double v2, MathContext mc) {
        BigDecimal bd1 = new BigDecimal(v1, mc);
        BigDecimal bd2 = new BigDecimal(v2, mc);
        return bd1.compareTo(bd2);
    }

    public void testGetSolarPosition() {
        Location location = new Location(latitude, longitude);
        SolarPosition.Variable sp = location.getSolarPosition(curTime);
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);

        assertEquals(0, compareTo(44.285104, sp.getApparentElevation(), mc));
        assertEquals(0, compareTo(45.714896, sp.getApparentZenith(), mc));
        assertEquals(0, compareTo(271.924492, sp.getAzimuth(), mc));
        assertEquals(0, compareTo(44.267857, sp.getElevation(), mc));
        assertEquals(0, compareTo(-6.533482, sp.getEquationOfTime(), mc));
        assertEquals(0, compareTo(45.732143, sp.getZenith(), mc));
    }
}
