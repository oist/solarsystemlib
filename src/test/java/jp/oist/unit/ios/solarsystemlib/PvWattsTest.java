/* vim : set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.ModelChain;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.collection.ModelCollection;
import jp.oist.unit.ios.solarsystemlib.irradiance.Irradiance;
import jp.oist.unit.ios.solarsystemlib.pvsystem.PvSystem;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class PvWattsTest extends TestCase {
    public ModelCollection models = new DefaultModelCollection();

    public Location loc = new Location(26.462, 127.831, 42.962);

    public double surfaceTilt = 27.5;
    public double surfaceAzimuth = 225.0;

    private PvSystem system;

    Irradiance irradiance;

    public void setUp() {
        irradiance = models.irradiance();
        system = new PvSystem(surfaceTilt, surfaceAzimuth, 6, Irradiance.SurfaceType.GRASS);
    }

    public void testEstimate() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("pvWattsDc", new Object[] {233.0 * 6.0, -0.003});

        ModelChain modelChain = new ModelChain(system, loc, opts);
        modelChain.setModelCollection(models);

        ZonedDateTime ts;
        SolarPosition.Variable pos;
        Irradiance.Variable irrad;
        double ghi;
        double pressure;
        double tempAir;
        double windSpeed;

        ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
        ghi = 272.0;
        pressure = 100720.0;
        tempAir = 30.7;
        windSpeed = 0.0;
        pos = loc.getSolarPosition(ts, pressure, tempAir);
        irrad = irradiance.getIrradiance(pos, ghi);
        ModelChain.Result result = modelChain.pvWatts(ts, irrad,  pressure, tempAir, windSpeed);

        assertEquals(0, TestUtils.compareTo(result.dcPower, 347.20117099969667, 4));
        assertEquals(0, TestUtils.compareTo(result.acPower, 331.34872543576114, 4));
    }
}