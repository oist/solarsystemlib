package jp.oist.unit.ios.solarsystemlib;

import jp.oist.unit.ios.solarsystemlib.solarposition.SolarPositionSpaModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TestUtils {

    public static int compareTo(double v1, double v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1).setScale(scale, RoundingMode.HALF_UP);
        BigDecimal b2 = new BigDecimal(v2).setScale(scale, RoundingMode.HALF_UP);
        return b1.compareTo(b2);
    }


    public static Object execPrivateMethod(Object instance, String name,
                                           Class<?>[] parameterType,
                                           Object... args)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method m = instance.getClass().getDeclaredMethod(name, parameterType);
        m.setAccessible(true);
        return m.invoke(instance, args);
    }
}
