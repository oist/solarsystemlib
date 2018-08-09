package jp.oist.unit.ios.solarsystemlib.pvsystem.cell.temperature;

import jp.oist.unit.ios.solarsystemlib.Location;
import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.atomsphere.airmass.Airmass;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.collection.ModelCollection;
import jp.oist.unit.ios.solarsystemlib.irradiance.Irradiance;
import jp.oist.unit.ios.solarsystemlib.pvsystem.PvSystem;
import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPosition;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class SapmCellTemperatureTest extends TestCase {

    public ModelCollection models = new DefaultModelCollection() {
        public CellTemperatureModel getCellTemperatureModel() {
            return new SapmCellTemperature(this);
        }
    };

    public ZonedDateTime ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
    public Location loc = new Location(26.462, 127.831, 42.962);
    public double ghi = 272.0;
    public double pressure = 100720.0;
    public double tempAir = 30.7;
    public double windSpeed = 0.0;

    public double surfaceTilt = 27.5;
    public double surfaceAzimuth = 225.0;

    private SolarPosition.Variable pos;
    private Airmass airmass;

    Irradiance irradiance;

    public void setUp() {
        irradiance = models.irradiance();
        pos = loc.getSolarPosition(ts, pressure, tempAir);
        airmass = loc.getAirmass(pos);
    }

    public void testEstimate() {
        Irradiance.Variable irrad = irradiance.getIrradiance(pos, ghi);

        Irradiance.SurfaceType surfaceType;
        surfaceType = Irradiance.SurfaceType.GRASS;
        Irradiance.PoaVariable poa = irradiance.getPoaIrradiance(surfaceTilt, surfaceAzimuth, pos,
                                                                 irrad, airmass, surfaceType);

        CellTemperatureModel model = models.getCellTemperatureModel();
        PvSystem.RackingModel rackingModel;
        CellTemperature.Variable temps;

        rackingModel = PvSystem.RackingModel.OPEN_RACK_CELL_GLASSBACK;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(39.56073687186786, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(38.781589035712734, temps.module, 6));

        rackingModel = PvSystem.RackingModel.ROOF_MOUNT_CELL_GLASSBACK;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(44.15142481139249, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(43.89170886600745, temps.module, 6));

        rackingModel = PvSystem.RackingModel.OPEN_RACK_CELL_POLYMERBACK;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(38.86516408243903, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(38.086016246283904, temps.module, 6));

        rackingModel = PvSystem.RackingModel.INSULATED_RACK_CELL_POLYMERBACK;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(46.336196516084506, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(46.336196516084506, temps.module, 6));

        rackingModel = PvSystem.RackingModel.OPEN_RACK_POLYMER_THINFILM_STEEL;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(38.718911161784746, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(37.93976332562962, temps.module, 6));

        rackingModel = PvSystem.RackingModel.X22_CONCENTRATOR_TRACKER;
        temps = model.estimate(poa, tempAir, windSpeed, rackingModel);
        assertEquals(0, TestUtils.compareTo(44.35002048490213, temps.cell, 6));
        assertEquals(0, TestUtils.compareTo(40.9737131948966, temps.module, 6));
    }
}