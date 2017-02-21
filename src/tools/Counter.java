package tools;

import java.util.HashMap;
import java.util.Map;

/**
 * 只负责统计词频，不负责显示
 * @param <T>
 */
public class Counter<T> {
    private Map<T, Integer> map = new HashMap<T, Integer>();

    public void count(T t) {
        Integer freq = map.get(t);
        map.put(t, freq == null ? 1 : freq + 1);
    }

    public Map<T, Integer> getAllKeysStatistics() {
        return map;
    }

    public int getKeyStatistics(T t) {
        return map.get(t) == null ? 0 : map.get(t);
    }
}