package jp.oist.unit.ios.solarsystemlib.irradiance.extraradiation;

import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.common.Consts;
import junit.framework.TestCase;

import java.time.ZonedDateTime;

public class ExtraRadiationSpencerModelTest extends TestCase {
    public void testEstimate() {
        int scale = 6;
        ZonedDateTime ts = ZonedDateTime.parse("2017-07-10T13:00:01+09:00");
        ExtraRadiationSpencerModel model = new ExtraRadiationSpencerModel(null);
        double extrarad = model.estimate(ts.getDayOfYear(), Consts.SOLAR_CONSTANT, 67.0);

        assertEquals(0, TestUtils.compareTo(1320.6414358435557, extrarad, scale));
    }
}