/* vim: set ts=4 sw=4 et fenc=utf-8 ff=unix cc=100 : */
package jp.oist.unit.ios.solarsystemlib.solarposition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jp.oist.unit.ios.solarsystemlib.TestUtils;
import jp.oist.unit.ios.solarsystemlib.collection.DefaultModelCollection;
import jp.oist.unit.ios.solarsystemlib.collection.ModelCollection;
import junit.framework.TestCase;

public class SolarPositionSpaModelTest extends TestCase {
    private static Class[] ONE_ARGUMENT  = new Class[] {double.class};
    private static Class[] TWO_ARGUMENT  = new Class[] {double.class, double.class};
    private static Class[] THREE_ARGUMENT  = new Class[] {double.class, double.class, double.class};
    private static Class[] FOUR_ARGUMENT  = new Class[] {double.class, double.class, double.class, double.class};
    private static Class[] SIX_ARGUMENT  = new Class[] {double.class, double.class, double.class,
                                                        double.class, double.class, double.class};
    private ZonedDateTime curTime = ZonedDateTime.parse("2018-07-26T15:52:40+09:00");
    private SolarPositionSpaModel solarPositionSpaModel = new SolarPositionSpaModel(new DefaultModelCollection());

    public void testSolarPosition() {

        int scale = 6;

        double latitude = 26.462;
        double longitude = 127.831;
        double elevation = 0;
        double pressure = 1013.25;
        double temp = 12;
        double atmosRefract = 0.5667;

        double deltaT = 67.0;
        Method m;
        try {
            /* test julian_day */
            m = SolarPositionSpaModel.class.getDeclaredMethod("julian_day", ONE_ARGUMENT);
            m.setAccessible(true);
            double jd = (double)m.invoke(solarPositionSpaModel, curTime.toEpochSecond());
            assertEquals(0, TestUtils.compareTo(2458325.786574, jd, scale));

            /* test julian_ephemeris_day */
            m = SolarPositionSpaModel.class.getDeclaredMethod("julian_ephemeris_day", TWO_ARGUMENT);

            m.setAccessible(true);
            double jde = (double)m.invoke(solarPositionSpaModel, jd, deltaT);
            assertEquals(0, TestUtils.compareTo(2458325.787350, jde, scale));

            /* test julian_century */
            m = SolarPositionSpaModel.class.getDeclaredMethod("julian_century", ONE_ARGUMENT);

            m.setAccessible(true);
            double jc = (double)m.invoke(solarPositionSpaModel, jd);
            assertEquals(0, TestUtils.compareTo(0.185648, jc, scale));

            /* test julian_ephemeris_century */
            m = SolarPositionSpaModel.class.getDeclaredMethod("julian_ephemeris_century", ONE_ARGUMENT);

            m.setAccessible(true);
            double jce = (double)m.invoke(solarPositionSpaModel, jde);
            assertEquals(0, TestUtils.compareTo(0.185648, jce, scale));

            /* test julian_ephemeris_millennium */
            m = SolarPositionSpaModel.class.getDeclaredMethod("julian_ephemeris_millennium", ONE_ARGUMENT);


            m.setAccessible(true);
            double jme = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(0.018565, jme, scale));

            /* test heliocentric_radius_vector */
            m = SolarPositionSpaModel.class.getDeclaredMethod("heliocentric_radius_vector", ONE_ARGUMENT);
            m.setAccessible(true);
            double r = (double)m.invoke(solarPositionSpaModel, jme);
            assertEquals(0, TestUtils.compareTo(1.01562825652975874391, r, scale));

            /* test heliocentric_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("heliocentric_longitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double l = (double)m.invoke(solarPositionSpaModel, jme);
            assertEquals(0, TestUtils.compareTo(303.26632331867403991055, l, scale));

            /* test heliocentric_latitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("heliocentric_latitude", ONE_ARGUMENT);

            m.setAccessible(true);
            double b = (double)m.invoke(solarPositionSpaModel, jme);
            assertEquals(0, TestUtils.compareTo(-6.148166454818104e-6, b, scale));

            /* test geocentric_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("geocentric_longitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double Theta = (double)m.invoke(solarPositionSpaModel, l);
            assertEquals(0, TestUtils.compareTo(123.26632331867403991055, Theta, scale));

            /* test geocentric_latitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("geocentric_latitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double beta = (double)m.invoke(solarPositionSpaModel, b);
            assertEquals(0, TestUtils.compareTo(6.148166454818104e-6, beta, scale));

            /* test mean_elongation */
            m = SolarPositionSpaModel.class.getDeclaredMethod("mean_elongation", ONE_ARGUMENT);
            m.setAccessible(true);
            double x0 = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(82960.727684573212172836065292, x0, scale));

            /* test mean_anomaly_sun */
            m = SolarPositionSpaModel.class.getDeclaredMethod("mean_anomaly_sun", ONE_ARGUMENT);
            m.setAccessible(true);
            double x1 = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(7040.673645756259247718844563, x1, scale));

            /* test mean_anomaly_moon */
            m = SolarPositionSpaModel.class.getDeclaredMethod("mean_anomaly_moon", ONE_ARGUMENT);
            m.setAccessible(true);
            double x2 = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(88725.902178310047020204365253, x2, scale));

            /* test moon_argument_latitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("moon_argument_latitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double x3 = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(89798.682537172469892539083958, x3, scale));

            /* test moon_ascending_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("moon_ascending_longitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double x4 = (double)m.invoke(solarPositionSpaModel, jce);
            assertEquals(0, TestUtils.compareTo(-234.023627393496866488931119, x4, scale));

            /* test longitude_nutation */
            m = SolarPositionSpaModel.class.getDeclaredMethod("longitude_nutation", SIX_ARGUMENT);
            m.setAccessible(true);
            double deltaPsi = (double)m.invoke(solarPositionSpaModel, jce, x0, x1, x2, x3, x4);
            assertEquals(0, TestUtils.compareTo(-0.00357515291588244221, deltaPsi, scale));

            /* test obliquity_nutation */
            m = SolarPositionSpaModel.class.getDeclaredMethod("obliquity_nutation", SIX_ARGUMENT);

            m.setAccessible(true);
            double deltaEpsilon = (double)m.invoke(solarPositionSpaModel, jce, x0, x1, x2, x3, x4);
            assertEquals(0, TestUtils.compareTo(-0.00157595396760760922, deltaEpsilon, scale));

            /* test mean_ecliptic_obliquity */
            m = SolarPositionSpaModel.class.getDeclaredMethod("mean_ecliptic_obliquity", ONE_ARGUMENT);
            m.setAccessible(true);
            double epsilon0 = (double)m.invoke(solarPositionSpaModel, jme);
            assertEquals(0, TestUtils.compareTo(84372.757962020623381249606609, epsilon0, scale));

            /* test true_ecliptic_obliquity */
            m = SolarPositionSpaModel.class.getDeclaredMethod("true_ecliptic_obliquity", TWO_ARGUMENT);
            m.setAccessible(true);
            double epsilon = (double)m.invoke(solarPositionSpaModel, epsilon0, deltaEpsilon);
            assertEquals(23.43530125770478989011, epsilon);

            /* test aberration_correction */
            m = SolarPositionSpaModel.class.getDeclaredMethod("aberration_correction", ONE_ARGUMENT);
            m.setAccessible(true);
            double deltaTau = (double)m.invoke(solarPositionSpaModel, r);
            assertEquals(-0.00560402989432220652, deltaTau);

            /* test apparent_sun_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("apparent_sun_longitude", THREE_ARGUMENT);
            m.setAccessible(true);
            double lamd = (double)m.invoke(solarPositionSpaModel, Theta, deltaPsi, deltaTau);
            assertEquals(0, TestUtils.compareTo(123.25714413586382534049, lamd, scale));

            /* test apparent_sun_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("mean_sidereal_time", TWO_ARGUMENT);
            m.setAccessible(true);
            double v0 = (double)m.invoke(solarPositionSpaModel, jd, jc);
            assertEquals(0, TestUtils.compareTo(47.09172651497647166252, v0, scale));

            /* test apparent_sidereal_time */
            m = SolarPositionSpaModel.class.getDeclaredMethod("apparent_sidereal_time", THREE_ARGUMENT);
            m.setAccessible(true);
            double v = (double)m.invoke(solarPositionSpaModel, v0, deltaPsi, epsilon);
            assertEquals(0, TestUtils.compareTo(47.08844627728449694359, v, scale));

            /* test geocentric_sun_right_ascension */
            m = SolarPositionSpaModel.class.getDeclaredMethod("geocentric_sun_right_ascension", THREE_ARGUMENT);
            m.setAccessible(true);
            double alpha = (double)m.invoke(solarPositionSpaModel, lamd, epsilon, beta);
            assertEquals(0, TestUtils.compareTo(125.55598971832477, alpha, scale));

            /* test geocentric_sun_declination */
            m = SolarPositionSpaModel.class.getDeclaredMethod("geocentric_sun_declination", THREE_ARGUMENT);
            m.setAccessible(true);
            double delta = (double)m.invoke(solarPositionSpaModel, lamd, epsilon, beta);
            assertEquals(0, TestUtils.compareTo(19.42514244727276917502, delta, scale));

            /* test sun_mean_longitude */
            m = SolarPositionSpaModel.class.getDeclaredMethod("sun_mean_longitude", ONE_ARGUMENT);
            m.setAccessible(true);
            double ml = (double)m.invoke(solarPositionSpaModel, jme);
            assertEquals(0, TestUtils.compareTo(6963.93161764620253961766, ml, scale));

            /* test equation_of_time */
            m = SolarPositionSpaModel.class.getDeclaredMethod("equation_of_time", FOUR_ARGUMENT);
            m.setAccessible(true);
            double eot = (double)m.invoke(solarPositionSpaModel, ml, alpha, deltaPsi, epsilon);
            assertEquals(0, TestUtils.compareTo(-6.53348243925574934110, eot, scale));

            /* test local_hour_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("local_hour_angle", THREE_ARGUMENT);
            m.setAccessible(true);
            double h = (double)m.invoke(solarPositionSpaModel, v, longitude, alpha);
            assertEquals(0, TestUtils.compareTo(49.36345655895971162863, h, scale));

            /* test equatorial_horizontal_parallax */
            m = SolarPositionSpaModel.class.getDeclaredMethod("equatorial_horizontal_parallax", ONE_ARGUMENT);
            m.setAccessible(true);
            double xi = (double)m.invoke(solarPositionSpaModel, r);
            assertEquals(0, TestUtils.compareTo(0.00240518886912851693, xi, scale));

            /* test uterm */
            m = SolarPositionSpaModel.class.getDeclaredMethod("uterm", ONE_ARGUMENT);
            m.setAccessible(true);
            double u = (double)m.invoke(solarPositionSpaModel, latitude);
            assertEquals(0, TestUtils.compareTo(0.46051063928951713, u, scale));

            /* test xterm */
            m = SolarPositionSpaModel.class.getDeclaredMethod("xterm", THREE_ARGUMENT);
            m.setAccessible(true);
            double x = (double)m.invoke(solarPositionSpaModel, u, latitude, elevation);
            assertEquals(0, TestUtils.compareTo(0.89582568336549917021, x, scale));

            /* test yterm */
            m = SolarPositionSpaModel.class.getDeclaredMethod("yterm", THREE_ARGUMENT);
            m.setAccessible(true);
            double y = (double)m.invoke(solarPositionSpaModel, u, latitude, elevation);
            assertEquals(0, TestUtils.compareTo(0.44291560110723154908, y, scale));

            /* test parallax_sun_right_ascension */
            m = SolarPositionSpaModel.class.getDeclaredMethod("parallax_sun_right_ascension", FOUR_ARGUMENT);
            m.setAccessible(true);
            double delta_alpha = (double)m.invoke(solarPositionSpaModel, x, xi, h, delta);
            assertEquals(0, TestUtils.compareTo(-0.00173378934556125248, delta_alpha, scale));

            /* test topocentric_sun_declination */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_sun_declination", SIX_ARGUMENT);
            m.setAccessible(true);
            double delta_prime = (double)m.invoke(solarPositionSpaModel, delta, x, y, xi, delta_alpha, h);
            assertEquals(0, TestUtils.compareTo(19.42460444379609896259, delta_prime, scale));

            /* test topocentric_local_hour_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_local_hour_angle", TWO_ARGUMENT);
            m.setAccessible(true);
            double h_prime = (double)m.invoke(solarPositionSpaModel, h, delta_alpha);
            assertEquals(0, TestUtils.compareTo(49.36519034830527630220, h_prime, scale));

            /* test topocentric_elevation_angle_without_atmosphere */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_elevation_angle_without_atmosphere",
                                                              THREE_ARGUMENT);
            m.setAccessible(true);
            double e0 = (double)m.invoke(solarPositionSpaModel, latitude, delta_prime, h_prime);
            assertEquals(0, TestUtils.compareTo(44.26785660428057411764, e0, scale));

            /* test  */
            m = SolarPositionSpaModel.class.getDeclaredMethod("atmospheric_refraction_correction",
                                                              FOUR_ARGUMENT);
            m.setAccessible(true);
            double delta_e = (double)m.invoke(solarPositionSpaModel, pressure, temp, e0, atmosRefract);
            assertEquals(0, TestUtils.compareTo(0.01724737577094804727, delta_e, scale));

            /* test  topocentric_elevation_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_elevation_angle", TWO_ARGUMENT);
            m.setAccessible(true);
            double  e = (double)m.invoke(solarPositionSpaModel, e0, delta_e);
            assertEquals(0, TestUtils.compareTo(44.2851039800515, e, scale));

            /* test topocentric_zenith_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_zenith_angle", ONE_ARGUMENT);
            m.setAccessible(true);
            double theta = (double)m.invoke(solarPositionSpaModel, e);
        
            assertEquals(0, TestUtils.compareTo(45.7148960199485, theta, scale));

            /* test topocentric_zenith_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_zenith_angle", ONE_ARGUMENT);
            m.setAccessible(true);
            double theta0 = (double)m.invoke(solarPositionSpaModel, e0);
        
            assertEquals(0, TestUtils.compareTo(45.73214339571945, theta0, scale));

            /* test topocentric_astronomers_azimuth */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_astronomers_azimuth", THREE_ARGUMENT);
            m.setAccessible(true);
            double gamma = (double)m.invoke(solarPositionSpaModel, h_prime, delta_prime, latitude);
            assertEquals(0, TestUtils.compareTo(91.9244917665748, gamma, scale));

            /* test topocentric_azimuth_angle */
            m = SolarPositionSpaModel.class.getDeclaredMethod("topocentric_azimuth_angle", ONE_ARGUMENT);
            m.setAccessible(true);
            double phi = (double)m.invoke(solarPositionSpaModel, gamma);
        
            assertEquals(0, TestUtils.compareTo(271.9244917665748, phi, scale));
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException ex) {
            fail(ex.getMessage());
        }
    }
}
