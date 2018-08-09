/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.irradiance.dni;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;

import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.irradiance.Irradiance;
import junit.framework.TestCase;

public class ErbsTest extends TestCase {

    private ZonedDateTime curTime = ZonedDateTime.parse("2018-07-26T15:52:40+09:00");
    private double latitude = 26.462;
    private double longitude = 127.831;

    private double ghi = 616.3;

    public void setUp() {
    }

    public void testEstimate() {
        int scale = 6;

        Location location = new Location(latitude, longitude);
        SolarPosition.Variable solarpos = location.getSolarPosition(curTime);
        double zenith = solarpos.getApparentZenith();

        DniErbsModel dniErbsModel = new DniErbsModel(new DefaultModelCollection());
        Irradiance.Variable vars = dniErbsModel.estimate(curTime, ghi, zenith);

        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);
        assertEquals(0, TestUtils.compareTo(617.212613, vars.dni, scale));
        assertEquals(0, TestUtils. compareTo(185.344135, vars.dhi, scale));
        assertEquals(0 ,TestUtils. compareTo(0.666995, vars.kt, scale));
    }
}
