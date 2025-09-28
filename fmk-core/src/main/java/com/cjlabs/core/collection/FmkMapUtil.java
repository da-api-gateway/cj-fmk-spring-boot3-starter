package com.cjlabs.core.collection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Map工具类，提供常用的Map操作方法
 */
public class FmkMapUtil {

    /**
     * 检查Map是否为空（null或没有元素）
     *
     * @param map 要检查的Map
     * @return 如果Map为null或空，则返回true
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }

    /**
     * 检查Map是否非空（不为null且有元素）
     *
     * @param map 要检查的Map
     * @return 如果Map不为null且包含至少一个元素，则返回true
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtils.isNotEmpty(map);
    }

    /**
     * 如果Map为null，则返回空Map，否则返回原Map
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param map 要检查的Map
     * @return 原Map或空Map（如果原Map为null）
     */
    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return MapUtils.emptyIfNull(map);
    }

    /**
     * 创建一个不可变Map
     *
     * @param <K>   Map键的类型
     * @param <V>   Map值的类型
     * @param key   键
     * @param value 值
     * @return 包含单个键值对的不可变Map
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        return ImmutableMap.of(key, value);
    }

    /**
     * 创建一个不可变Map，包含两个键值对
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param k1  第一个键
     * @param v1  第一个值
     * @param k2  第二个键
     * @param v2  第二个值
     * @return 包含两个键值对的不可变Map
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return ImmutableMap.of(k1, v1, k2, v2);
    }

    /**
     * 创建一个不可变Map，包含三个键值对
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param k1  第一个键
     * @param v1  第一个值
     * @param k2  第二个键
     * @param v2  第二个值
     * @param k3  第三个键
     * @param v3  第三个值
     * @return 包含三个键值对的不可变Map
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3);
    }

    /**
     * 创建一个不可变Map，包含四个键值对
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param k1  第一个键
     * @param v1  第一个值
     * @param k2  第二个键
     * @param v2  第二个值
     * @param k3  第三个键
     * @param v3  第三个值
     * @param k4  第四个键
     * @param v4  第四个值
     * @return 包含四个键值对的不可变Map
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * 创建一个不可变Map，包含五个键值对
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param k1  第一个键
     * @param v1  第一个值
     * @param k2  第二个键
     * @param v2  第二个值
     * @param k3  第三个键
     * @param v3  第三个值
     * @param k4  第四个键
     * @param v4  第四个值
     * @param k5  第五个键
     * @param v5  第五个值
     * @return 包含五个键值对的不可变Map
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /**
     * 创建一个HashMap并添加键值对
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @return 包含指定键值对的HashMap
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 创建一个HashMap并添加键值对
     *
     * @param <K>   Map键的类型
     * @param <V>   Map值的类型
     * @param key   键
     * @param value 值
     * @return 包含指定键值对的HashMap
     */
    public static <K, V> Map<K, V> newHashMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 创建一个具有指定初始容量的HashMap
     *
     * @param <K>             Map键的类型
     * @param <V>             Map值的类型
     * @param initialCapacity 初始容量
     * @return 具有指定初始容量的空HashMap
     */
    public static <K, V> Map<K, V> newHashMapWithCapacity(int initialCapacity) {
        return Maps.newHashMapWithExpectedSize(initialCapacity);
    }

    /**
     * 创建一个LinkedHashMap并添加键值对
     *
     * @param <K>   Map键的类型
     * @param <V>   Map值的类型
     * @param key   键
     * @param value 值
     * @return 包含指定键值对的LinkedHashMap
     */
    public static <K, V> Map<K, V> newLinkedHashMap(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 合并两个Map，如果有重复键，使用第二个Map的值
     *
     * @param <K>  Map键的类型
     * @param <V>  Map值的类型
     * @param map1 第一个Map
     * @param map2 第二个Map
     * @return 合并后的新Map
     */
    public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2) {
        if (isEmpty(map1)) {
            return emptyIfNull(map2);
        }
        if (isEmpty(map2)) {
            return emptyIfNull(map1);
        }

        Map<K, V> result = new HashMap<>(map1);
        result.putAll(map2);
        return result;
    }

    /**
     * 获取Map中指定键的值，如果键不存在则返回默认值
     *
     * @param <K>          Map键的类型
     * @param <V>          Map值的类型
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 键对应的值，如果键不存在则返回默认值
     */
    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (isEmpty(map)) {
            return defaultValue;
        }
        return map.getOrDefault(key, defaultValue);
    }

    /**
     * 获取Map中指定键的字符串值，如果键不存在或值为null则返回空字符串
     *
     * @param <K> Map键的类型
     * @param map Map
     * @param key 键
     * @return 键对应的字符串值，如果键不存在或值为null则返回空字符串
     */
    public static <K> String getString(Map<K, ?> map, K key) {
        if (isEmpty(map)) {
            return StringUtils.EMPTY;
        }
        Object value = map.get(key);
        return value == null ? StringUtils.EMPTY : value.toString();
    }

    /**
     * 获取Map中指定键的整数值，如果键不存在或值无法转换为整数则返回默认值
     *
     * @param <K>          Map键的类型
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 键对应的整数值，如果键不存在或值无法转换为整数则返回默认值
     */
    public static <K> int getInt(Map<K, ?> map, K key, int defaultValue) {
        return MapUtils.getInteger(map, key, defaultValue);
    }

    /**
     * 获取Map中指定键的长整数值，如果键不存在或值无法转换为长整数则返回默认值
     *
     * @param <K>          Map键的类型
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 键对应的长整数值，如果键不存在或值无法转换为长整数则返回默认值
     */
    public static <K> long getLong(Map<K, ?> map, K key, long defaultValue) {
        return MapUtils.getLong(map, key, defaultValue);
    }

    /**
     * 获取Map中指定键的布尔值，如果键不存在或值无法转换为布尔值则返回默认值
     *
     * @param <K>          Map键的类型
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 键对应的布尔值，如果键不存在或值无法转换为布尔值则返回默认值
     */
    public static <K> boolean getBoolean(Map<K, ?> map, K key, boolean defaultValue) {
        return MapUtils.getBoolean(map, key, defaultValue);
    }

    /**
     * 根据值过滤Map
     *
     * @param <K>       Map键的类型
     * @param <V>       Map值的类型
     * @param map       要过滤的Map
     * @param predicate 过滤条件
     * @return 过滤后的新Map
     */
    public static <K, V> Map<K, V> filterByValue(Map<K, V> map, java.util.function.Predicate<V> predicate) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 根据键过滤Map
     *
     * @param <K>       Map键的类型
     * @param <V>       Map值的类型
     * @param map       要过滤的Map
     * @param predicate 过滤条件
     * @return 过滤后的新Map
     */
    public static <K, V> Map<K, V> filterByKey(Map<K, V> map, java.util.function.Predicate<K> predicate) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 转换Map的值
     *
     * @param <K>         Map键的类型
     * @param <V>         原Map值的类型
     * @param <R>         新Map值的类型
     * @param map         要转换的Map
     * @param valueMapper 值转换函数
     * @return 转换后的新Map
     */
    public static <K, V, R> Map<K, R> transformValues(Map<K, V> map, Function<V, R> valueMapper) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> valueMapper.apply(entry.getValue())
                ));
    }

    /**
     * 转换Map的键
     *
     * @param <K>       原Map键的类型
     * @param <V>       Map值的类型
     * @param <R>       新Map键的类型
     * @param map       要转换的Map
     * @param keyMapper 键转换函数
     * @return 转换后的新Map
     */
    public static <K, V, R> Map<R, V> transformKeys(Map<K, V> map, Function<K, R> keyMapper) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> keyMapper.apply(entry.getKey()),
                        Map.Entry::getValue,
                        (v1, v2) -> v1 // 如果有重复键，保留第一个值
                ));
    }

    /**
     * 反转Map的键和值
     *
     * @param <K> 原Map键的类型
     * @param <V> 原Map值的类型
     * @param map 要反转的Map
     * @return 反转后的新Map，原Map的值作为键，原Map的键作为值
     */
    public static <K, V> Map<V, K> inverse(Map<K, V> map) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey,
                        (k1, k2) -> k1 // 如果有重复值，保留第一个键
                ));
    }

    /**
     * 对Map按值排序
     *
     * @param <K>       Map键的类型
     * @param <V>       Map值的类型，必须实现Comparable接口
     * @param map       要排序的Map
     * @param ascending 是否升序排序
     * @return 排序后的LinkedHashMap
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean ascending) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }

        return map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int result = Objects.compare(e1.getValue(), e2.getValue(), Comparable::compareTo);
                    return ascending ? result : -result;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    /**
     * 对Map按键排序
     *
     * @param <K>       Map键的类型，必须实现Comparable接口
     * @param <V>       Map值的类型
     * @param map       要排序的Map
     * @param ascending 是否升序排序
     * @return 排序后的LinkedHashMap
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean ascending) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }

        return map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int result = e1.getKey().compareTo(e2.getKey());
                    return ascending ? result : -result;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    /**
     * 从Map中移除值为null的条目
     *
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @param map 要处理的Map
     * @return 移除null值后的新Map
     */
    public static <K, V> Map<K, V> removeNullValues(Map<K, V> map) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 从Map中获取子集
     *
     * @param <K>  Map键的类型
     * @param <V>  Map值的类型
     * @param map  原Map
     * @param keys 要提取的键集合
     * @return 包含指定键的子Map
     */
    public static <K, V> Map<K, V> subMap(Map<K, V> map, Iterable<K> keys) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }

        Map<K, V> result = new HashMap<>();
        for (K key : keys) {
            if (map.containsKey(key)) {
                result.put(key, map.get(key));
            }
        }
        return result;
    }
}