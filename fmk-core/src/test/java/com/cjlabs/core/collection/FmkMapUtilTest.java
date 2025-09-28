package com.cjlabs.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkMapUtil Tests")
class FmkMapUtilTest {

    @Nested
    @DisplayName("isEmpty and isNotEmpty Tests")
    class EmptyChecks {
        @Test
        @DisplayName("isEmpty should return true for null map")
        void isEmptyShouldReturnTrueForNullMap() {
            assertTrue(FmkMapUtil.isEmpty(null));
        }

        @Test
        @DisplayName("isEmpty should return true for empty map")
        void isEmptyShouldReturnTrueForEmptyMap() {
            assertTrue(FmkMapUtil.isEmpty(Collections.emptyMap()));
        }

        @Test
        @DisplayName("isEmpty should return false for non-empty map")
        void isEmptyShouldReturnFalseForNonEmptyMap() {
            assertFalse(FmkMapUtil.isEmpty(Map.of("key", "value")));
        }

        @Test
        @DisplayName("isNotEmpty should return false for null map")
        void isNotEmptyShouldReturnFalseForNullMap() {
            assertFalse(FmkMapUtil.isNotEmpty(null));
        }

        @Test
        @DisplayName("isNotEmpty should return false for empty map")
        void isNotEmptyShouldReturnFalseForEmptyMap() {
            assertFalse(FmkMapUtil.isNotEmpty(Collections.emptyMap()));
        }

        @Test
        @DisplayName("isNotEmpty should return true for non-empty map")
        void isNotEmptyShouldReturnTrueForNonEmptyMap() {
            assertTrue(FmkMapUtil.isNotEmpty(Map.of("key", "value")));
        }
    }

    @Nested
    @DisplayName("emptyIfNull Tests")
    class EmptyIfNullTests {
        @Test
        @DisplayName("emptyIfNull should return empty map for null input")
        void emptyIfNullShouldReturnEmptyMapForNullInput() {
            Map<String, String> result = FmkMapUtil.emptyIfNull(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("emptyIfNull should return original map for non-null input")
        void emptyIfNullShouldReturnOriginalMapForNonNullInput() {
            Map<String, String> original = Map.of("key", "value");
            Map<String, String> result = FmkMapUtil.emptyIfNull(original);
            assertSame(original, result);
        }
    }

    @Nested
    @DisplayName("Immutable Map Creation Tests")
    class ImmutableMapTests {
        @Test
        @DisplayName("of(K,V) should create map with one entry")
        void ofShouldCreateMapWithOneEntry() {
            Map<String, Integer> map = FmkMapUtil.of("key", 1);
            assertEquals(1, map.size());
            assertEquals(1, map.get("key"));
        }

        @Test
        @DisplayName("of(K,V,K,V) should create map with two entries")
        void ofShouldCreateMapWithTwoEntries() {
            Map<String, Integer> map = FmkMapUtil.of("key1", 1, "key2", 2);
            assertEquals(2, map.size());
            assertEquals(1, map.get("key1"));
            assertEquals(2, map.get("key2"));
        }

        @Test
        @DisplayName("of(K,V,K,V,K,V) should create map with three entries")
        void ofShouldCreateMapWithThreeEntries() {
            Map<String, Integer> map = FmkMapUtil.of("key1", 1, "key2", 2, "key3", 3);
            assertEquals(3, map.size());
            assertEquals(1, map.get("key1"));
            assertEquals(2, map.get("key2"));
            assertEquals(3, map.get("key3"));
        }

        @Test
        @DisplayName("of(K,V,K,V,K,V,K,V) should create map with four entries")
        void ofShouldCreateMapWithFourEntries() {
            Map<String, Integer> map = FmkMapUtil.of("key1", 1, "key2", 2, "key3", 3, "key4", 4);
            assertEquals(4, map.size());
            assertEquals(1, map.get("key1"));
            assertEquals(2, map.get("key2"));
            assertEquals(3, map.get("key3"));
            assertEquals(4, map.get("key4"));
        }

        @Test
        @DisplayName("of(K,V,K,V,K,V,K,V,K,V) should create map with five entries")
        void ofShouldCreateMapWithFiveEntries() {
            Map<String, Integer> map = FmkMapUtil.of("key1", 1, "key2", 2, "key3", 3, "key4", 4, "key5", 5);
            assertEquals(5, map.size());
            assertEquals(1, map.get("key1"));
            assertEquals(2, map.get("key2"));
            assertEquals(3, map.get("key3"));
            assertEquals(4, map.get("key4"));
            assertEquals(5, map.get("key5"));
        }
    }

    @Nested
    @DisplayName("HashMap Creation Tests")
    class HashMapTests {
        @Test
        @DisplayName("newHashMap should create empty HashMap")
        void newHashMapShouldCreateEmptyHashMap() {
            Map<String, Integer> map = FmkMapUtil.newHashMap();
            assertTrue(map instanceof HashMap);
            assertTrue(map.isEmpty());
        }

        @Test
        @DisplayName("newHashMap(K,V) should create HashMap with one entry")
        void newHashMapShouldCreateHashMapWithOneEntry() {
            Map<String, Integer> map = FmkMapUtil.newHashMap("key", 1);
            assertTrue(map instanceof HashMap);
            assertEquals(1, map.size());
            assertEquals(1, map.get("key"));
        }

        @Test
        @DisplayName("newHashMapWithCapacity should create HashMap with specified capacity")
        void newHashMapWithCapacityShouldCreateHashMapWithSpecifiedCapacity() {
            Map<String, Integer> map = FmkMapUtil.newHashMapWithCapacity(10);
            assertTrue(map instanceof HashMap);
            assertTrue(map.isEmpty());
        }
    }

    @Nested
    @DisplayName("LinkedHashMap Creation Tests")
    class LinkedHashMapTests {
        @Test
        @DisplayName("newLinkedHashMap should create LinkedHashMap with one entry")
        void newLinkedHashMapShouldCreateLinkedHashMapWithOneEntry() {
            Map<String, Integer> map = FmkMapUtil.newLinkedHashMap("key", 1);
            assertTrue(map instanceof LinkedHashMap);
            assertEquals(1, map.size());
            assertEquals(1, map.get("key"));
        }
    }

    @Nested
    @DisplayName("Map Merge Tests")
    class MergeTests {
        @Test
        @DisplayName("merge should return second map when first map is null")
        void mergeShouldReturnSecondMapWhenFirstMapIsNull() {
            Map<String, Integer> map2 = Map.of("key", 1);
            Map<String, Integer> result = FmkMapUtil.merge(null, map2);
            assertEquals(map2, result);
        }

        @Test
        @DisplayName("merge should return first map when second map is null")
        void mergeShouldReturnFirstMapWhenSecondMapIsNull() {
            Map<String, Integer> map1 = Map.of("key", 1);
            Map<String, Integer> result = FmkMapUtil.merge(map1, null);
            assertEquals(map1, result);
        }

        @Test
        @DisplayName("merge should combine maps with second map values overriding first")
        void mergeShouldCombineMapsWithSecondMapValuesOverridingFirst() {
            Map<String, Integer> map1 = new HashMap<>();
            map1.put("key1", 1);
            map1.put("key2", 2);

            Map<String, Integer> map2 = new HashMap<>();
            map2.put("key2", 20);
            map2.put("key3", 3);

            Map<String, Integer> result = FmkMapUtil.merge(map1, map2);
            
            assertEquals(3, result.size());
            assertEquals(1, result.get("key1"));
            assertEquals(20, result.get("key2"));
            assertEquals(3, result.get("key3"));
        }
    }

    @Nested
    @DisplayName("Value Retrieval Tests")
    class ValueRetrievalTests {
        @Test
        @DisplayName("getOrDefault should return default value for null map")
        void getOrDefaultShouldReturnDefaultValueForNullMap() {
            assertEquals("default", FmkMapUtil.getOrDefault(null, "key", "default"));
        }

        @Test
        @DisplayName("getOrDefault should return default value for missing key")
        void getOrDefaultShouldReturnDefaultValueForMissingKey() {
            Map<String, String> map = Map.of("key1", "value1");
            assertEquals("default", FmkMapUtil.getOrDefault(map, "key2", "default"));
        }

        @Test
        @DisplayName("getOrDefault should return value for existing key")
        void getOrDefaultShouldReturnValueForExistingKey() {
            Map<String, String> map = Map.of("key", "value");
            assertEquals("value", FmkMapUtil.getOrDefault(map, "key", "default"));
        }

        @Test
        @DisplayName("getString should return empty string for null map")
        void getStringShouldReturnEmptyStringForNullMap() {
            assertEquals("", FmkMapUtil.getString(null, "key"));
        }

        @Test
        @DisplayName("getString should return empty string for missing key")
        void getStringShouldReturnEmptyStringForMissingKey() {
            Map<String, String> map = Map.of("key1", "value1");
            assertEquals("", FmkMapUtil.getString(map, "key2"));
        }

        @Test
        @DisplayName("getString should return string value for existing key")
        void getStringShouldReturnStringValueForExistingKey() {
            Map<String, String> map = Map.of("key", "value");
            assertEquals("value", FmkMapUtil.getString(map, "key"));
        }

        @Test
        @DisplayName("getInt should return default value for null map")
        void getIntShouldReturnDefaultValueForNullMap() {
            assertEquals(42, FmkMapUtil.getInt(null, "key", 42));
        }

        @Test
        @DisplayName("getInt should return int value for existing key")
        void getIntShouldReturnIntValueForExistingKey() {
            Map<String, Integer> map = Map.of("key", 123);
            assertEquals(123, FmkMapUtil.getInt(map, "key", 42));
        }

        @Test
        @DisplayName("getLong should return default value for null map")
        void getLongShouldReturnDefaultValueForNullMap() {
            assertEquals(42L, FmkMapUtil.getLong(null, "key", 42L));
        }

        @Test
        @DisplayName("getLong should return long value for existing key")
        void getLongShouldReturnLongValueForExistingKey() {
            Map<String, Long> map = Map.of("key", 123L);
            assertEquals(123L, FmkMapUtil.getLong(map, "key", 42L));
        }

        @Test
        @DisplayName("getBoolean should return default value for null map")
        void getBooleanShouldReturnDefaultValueForNullMap() {
            assertTrue(FmkMapUtil.getBoolean(null, "key", true));
        }

        @Test
        @DisplayName("getBoolean should return boolean value for existing key")
        void getBooleanShouldReturnBooleanValueForExistingKey() {
            Map<String, Boolean> map = Map.of("key", true);
            assertTrue(FmkMapUtil.getBoolean(map, "key", false));
        }
    }

    @Nested
    @DisplayName("Filter Tests")
    class FilterTests {
        @Test
        @DisplayName("filterByValue should return empty map for null input")
        void filterByValueShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.filterByValue(null, v -> true).isEmpty());
        }

        @Test
        @DisplayName("filterByValue should filter entries by value predicate")
        void filterByValueShouldFilterEntriesByValuePredicate() {
            Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
            Map<String, Integer> result = FmkMapUtil.filterByValue(map, v -> v % 2 == 1);
            
            assertEquals(2, result.size());
            assertEquals(1, result.get("one"));
            assertEquals(3, result.get("three"));
        }

        @Test
        @DisplayName("filterByKey should return empty map for null input")
        void filterByKeyShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.filterByKey(null, k -> true).isEmpty());
        }

        @Test
        @DisplayName("filterByKey should filter entries by key predicate")
        void filterByKeyShouldFilterEntriesByKeyPredicate() {
            Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
            Map<String, Integer> result = FmkMapUtil.filterByKey(map, k -> k.length() > 3);
            
            assertEquals(1, result.size());
            assertEquals(3, result.get("three"));
        }
    }

    @Nested
    @DisplayName("Transform Tests")
    class TransformTests {
        @Test
        @DisplayName("transformValues should return empty map for null input")
        void transformValuesShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.transformValues(null, v -> v).isEmpty());
        }

        @Test
        @DisplayName("transformValues should transform map values")
        void transformValuesShouldTransformMapValues() {
            Map<String, Integer> map = Map.of("one", 1, "two", 2);
            Map<String, String> result = FmkMapUtil.transformValues(map, v -> "Value: " + v);
            
            assertEquals(2, result.size());
            assertEquals("Value: 1", result.get("one"));
            assertEquals("Value: 2", result.get("two"));
        }

        @Test
        @DisplayName("transformKeys should return empty map for null input")
        void transformKeysShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.transformKeys(null, k -> k).isEmpty());
        }

        @Test
        @DisplayName("transformKeys should transform map keys")
        void transformKeysShouldTransformMapKeys() {
            Map<String, Integer> map = Map.of("one", 1, "two", 2);
            Map<String, Integer> result = FmkMapUtil.transformKeys(map, k -> k.toUpperCase());
            
            assertEquals(2, result.size());
            assertEquals(1, result.get("ONE"));
            assertEquals(2, result.get("TWO"));
        }
    }

    @Nested
    @DisplayName("Inverse Tests")
    class InverseTests {
        @Test
        @DisplayName("inverse should return empty map for null input")
        void inverseShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.inverse(null).isEmpty());
        }

        @Test
        @DisplayName("inverse should swap keys and values")
        void inverseShouldSwapKeysAndValues() {
            Map<String, Integer> map = Map.of("one", 1, "two", 2);
            Map<Integer, String> result = FmkMapUtil.inverse(map);
            
            assertEquals(2, result.size());
            assertEquals("one", result.get(1));
            assertEquals("two", result.get(2));
        }

        @Test
        @DisplayName("inverse should handle null values")
        void inverseShouldHandleNullValues() {
            Map<String, Integer> map = new HashMap<>();
            map.put("one", 1);
            map.put("null", null);
            
            Map<Integer, String> result = FmkMapUtil.inverse(map);
            
            assertEquals(1, result.size());
            assertEquals("one", result.get(1));
        }
    }

    @Nested
    @DisplayName("Sort Tests")
    class SortTests {
        @Test
        @DisplayName("sortByValue should return empty map for null input")
        void sortByValueShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.sortByValue(null, true).isEmpty());
        }

        @Test
        @DisplayName("sortByValue should sort map by values in ascending order")
        void sortByValueShouldSortMapByValuesInAscendingOrder() {
            Map<String, Integer> map = new HashMap<>();
            map.put("three", 3);
            map.put("one", 1);
            map.put("two", 2);
            
            Map<String, Integer> result = FmkMapUtil.sortByValue(map, true);
            
            assertEquals(3, result.size());
            assertInstanceOf(LinkedHashMap.class, result);
            
            // Check order of entries
            String[] expectedKeys = {"one", "two", "three"};
            int i = 0;
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                assertEquals(expectedKeys[i++], entry.getKey());
            }
        }

        @Test
        @DisplayName("sortByValue should sort map by values in descending order")
        void sortByValueShouldSortMapByValuesInDescendingOrder() {
            Map<String, Integer> map = new HashMap<>();
            map.put("three", 3);
            map.put("one", 1);
            map.put("two", 2);
            
            Map<String, Integer> result = FmkMapUtil.sortByValue(map, false);
            
            assertEquals(3, result.size());
            assertInstanceOf(LinkedHashMap.class, result);
            
            // Check order of entries
            String[] expectedKeys = {"three", "two", "one"};
            int i = 0;
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                assertEquals(expectedKeys[i++], entry.getKey());
            }
        }

        @Test
        @DisplayName("sortByKey should return empty map for null input")
        void sortByKeyShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.sortByKey(null, true).isEmpty());
        }

        @Test
        @DisplayName("sortByKey should sort map by keys in ascending order")
        void sortByKeyShouldSortMapByKeysInAscendingOrder() {
            Map<String, Integer> map = new HashMap<>();
            map.put("c", 3);
            map.put("a", 1);
            map.put("b", 2);
            
            Map<String, Integer> result = FmkMapUtil.sortByKey(map, true);
            
            assertEquals(3, result.size());
            assertInstanceOf(LinkedHashMap.class, result);
            
            // Check order of entries
            String[] expectedKeys = {"a", "b", "c"};
            int i = 0;
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                assertEquals(expectedKeys[i++], entry.getKey());
            }
        }

        @Test
        @DisplayName("sortByKey should sort map by keys in descending order")
        void sortByKeyShouldSortMapByKeysInDescendingOrder() {
            Map<String, Integer> map = new HashMap<>();
            map.put("c", 3);
            map.put("a", 1);
            map.put("b", 2);
            
            Map<String, Integer> result = FmkMapUtil.sortByKey(map, false);
            
            assertEquals(3, result.size());
            assertInstanceOf(LinkedHashMap.class, result);
            
            // Check order of entries
            String[] expectedKeys = {"c", "b", "a"};
            int i = 0;
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                assertEquals(expectedKeys[i++], entry.getKey());
            }
        }
    }

    @Nested
    @DisplayName("Remove Null Values Tests")
    class RemoveNullValuesTests {
        @Test
        @DisplayName("removeNullValues should return empty map for null input")
        void removeNullValuesShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.removeNullValues(null).isEmpty());
        }

        @Test
        @DisplayName("removeNullValues should remove entries with null values")
        void removeNullValuesShouldRemoveEntriesWithNullValues() {
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", null);
            map.put("key3", "value3");
            
            Map<String, String> result = FmkMapUtil.removeNullValues(map);
            
            assertEquals(2, result.size());
            assertEquals("value1", result.get("key1"));
            assertEquals("value3", result.get("key3"));
            assertFalse(result.containsKey("key2"));
        }
    }

    @Nested
    @DisplayName("SubMap Tests")
    class SubMapTests {
        @Test
        @DisplayName("subMap should return empty map for null input")
        void subMapShouldReturnEmptyMapForNullInput() {
            assertTrue(FmkMapUtil.subMap(null, List.of("key")).isEmpty());
        }

        @Test
        @DisplayName("subMap should extract entries with specified keys")
        void subMapShouldExtractEntriesWithSpecifiedKeys() {
            Map<String, Integer> map = Map.of("key1", 1, "key2", 2, "key3", 3);
            Map<String, Integer> result = FmkMapUtil.subMap(map, Arrays.asList("key1", "key3", "key4"));
            
            assertEquals(2, result.size());
            assertEquals(1, result.get("key1"));
            assertEquals(3, result.get("key3"));
            assertFalse(result.containsKey("key2"));
            assertFalse(result.containsKey("key4"));
        }
    }

    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTests {
        
        static Stream<Arguments> emptyMapTestCases() {
            return Stream.of(
                Arguments.of(null, true),
                Arguments.of(Collections.emptyMap(), true),
                Arguments.of(Map.of("key", "value"), false)
            );
        }
        
        @ParameterizedTest(name = "isEmpty({0}) should return {1}")
        @MethodSource("emptyMapTestCases")
        void isEmptyParameterizedTest(Map<?, ?> map, boolean expected) {
            assertEquals(expected, FmkMapUtil.isEmpty(map));
        }
        
        static Stream<Arguments> notEmptyMapTestCases() {
            return Stream.of(
                Arguments.of(null, false),
                Arguments.of(Collections.emptyMap(), false),
                Arguments.of(Map.of("key", "value"), true)
            );
        }
        
        @ParameterizedTest(name = "isNotEmpty({0}) should return {1}")
        @MethodSource("notEmptyMapTestCases")
        void isNotEmptyParameterizedTest(Map<?, ?> map, boolean expected) {
            assertEquals(expected, FmkMapUtil.isNotEmpty(map));
        }
    }
}