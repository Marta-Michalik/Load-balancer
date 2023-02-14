package pl.edu.agh.kt;

import java.util.HashMap;
import java.util.Map;

public class MapForValues {
    private static Map<Integer, Double> map = new HashMap<>();
    static {
        map.put(1, 0.5);
        map.put(2, 0.5);
        map.put(3, 0.5);
    }
    public static void setValue(int key, double value) {
        map.put(key, value);
    }

    public static double getValue(int key) {
        return map.get(key);
    }
}
