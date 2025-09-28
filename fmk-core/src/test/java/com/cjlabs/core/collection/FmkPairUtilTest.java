package com.cjlabs.core.collection;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkPairUtil Tests")
class FmkPairUtilTest {

    @Nested
    @DisplayName("Pair Creation Tests")
    class PairCreationTests {
        @Test
        @DisplayName("of should create immutable pair")
        void ofShouldCreateImmutablePair() {
            Pair<String, Integer> pair = FmkPairUtil.of("key", 1);
            
            assertNotNull(pair);
            assertEquals("key", pair.getLeft());
            assertEquals(1, pair.getRight());
        }

        @Test
        @DisplayName("ofMutable should create mutable pair")
        void ofMutableShouldCreateMutablePair() {
            MutablePair<String, Integer> pair = FmkPairUtil.ofMutable("key", 1);
            
            assertNotNull(pair);
            assertEquals("key", pair.getLeft());
            assertEquals(1, pair.getRight());
            
            // Test mutability
            pair.setLeft("newKey");
            pair.setRight(2);
            
            assertEquals("newKey", pair.getLeft());
            assertEquals(2, pair.getRight());
        }

        @Test
        @DisplayName("ofNullable should return null when both values are null")
        void ofNullableShouldReturnNullWhenBothValuesAreNull() {
            Pair<String, Integer> pair = FmkPairUtil.ofNullable(null, null);
            assertNull(pair);
        }

        @Test
        @DisplayName("ofNullable should create pair when at least one value is not null")
        void ofNullableShouldCreatePairWhenAtLeastOneValueIsNotNull() {
            Pair<String, Integer> pair1 = FmkPairUtil.ofNullable("key", null);
            Pair<String, Integer> pair2 = FmkPairUtil.ofNullable(null, 1);
            
            assertNotNull(pair1);
            assertEquals("key", pair1.getLeft());
            assertNull(pair1.getRight());
            
            assertNotNull(pair2);
            assertNull(pair2.getLeft());
            assertEquals(1, pair2.getRight());
        }

        @Test
        @DisplayName("pairOf should create pair with same value for both sides")
        void pairOfShouldCreatePairWithSameValueForBothSides() {
            Pair<String, String> pair = FmkPairUtil.pairOf("value");
            
            assertNotNull(pair);
            assertEquals("value", pair.getLeft());
            assertEquals("value", pair.getRight());
        }
    }

    @Nested
    @DisplayName("Pair Access Tests")
    class PairAccessTests {
        @Test
        @DisplayName("getLeft should return left value")
        void getLeftShouldReturnLeftValue() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            assertEquals("key", FmkPairUtil.getLeft(pair));
        }

        @Test
        @DisplayName("getLeft should return null for null pair")
        void getLeftShouldReturnNullForNullPair() {
            assertNull(FmkPairUtil.getLeft(null));
        }

        @Test
        @DisplayName("getRight should return right value")
        void getRightShouldReturnRightValue() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            assertEquals(1, FmkPairUtil.getRight(pair));
        }

        @Test
        @DisplayName("getRight should return null for null pair")
        void getRightShouldReturnNullForNullPair() {
            assertNull(FmkPairUtil.getRight(null));
        }
    }

    @Nested
    @DisplayName("Pair Operation Tests")
    class PairOperationTests {
        @Test
        @DisplayName("swap should exchange left and right values")
        void swapShouldExchangeLeftAndRightValues() {
            Pair<String, Integer> original = Pair.of("key", 1);
            Pair<Integer, String> swapped = FmkPairUtil.swap(original);
            
            assertNotNull(swapped);
            assertEquals(1, swapped.getLeft());
            assertEquals("key", swapped.getRight());
        }

        @Test
        @DisplayName("swap should return null for null pair")
        void swapShouldReturnNullForNullPair() {
            assertNull(FmkPairUtil.swap(null));
        }

        @Test
        @DisplayName("equals should return true for identical pairs")
        void equalsShouldReturnTrueForIdenticalPairs() {
            Pair<String, Integer> pair1 = Pair.of("key", 1);
            Pair<String, Integer> pair2 = Pair.of("key", 1);
            
            assertTrue(FmkPairUtil.equals(pair1, pair2));
        }

        @Test
        @DisplayName("equals should return true for same instance")
        void equalsShouldReturnTrueForSameInstance() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            assertTrue(FmkPairUtil.equals(pair, pair));
        }

        @Test
        @DisplayName("equals should return false for different pairs")
        void equalsShouldReturnFalseForDifferentPairs() {
            Pair<String, Integer> pair1 = Pair.of("key1", 1);
            Pair<String, Integer> pair2 = Pair.of("key2", 2);
            
            assertFalse(FmkPairUtil.equals(pair1, pair2));
        }

        @Test
        @DisplayName("equals should return false when one pair is null")
        void equalsShouldReturnFalseWhenOnePairIsNull() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            
            assertFalse(FmkPairUtil.equals(pair, null));
            assertFalse(FmkPairUtil.equals(null, pair));
        }

        @Test
        @DisplayName("equals should return true when both pairs are null")
        void equalsShouldReturnTrueWhenBothPairsAreNull() {
            assertTrue(FmkPairUtil.equals(null, null));
        }

        @Test
        @DisplayName("toString should return string representation of pair")
        void toStringShouldReturnStringRepresentationOfPair() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            assertEquals(pair.toString(), FmkPairUtil.toString(pair));
        }

        @Test
        @DisplayName("toString should return 'null' for null pair")
        void toStringShouldReturnNullForNullPair() {
            assertEquals("null", FmkPairUtil.toString(null));
        }
    }

    @Nested
    @DisplayName("Map Conversion Tests")
    class MapConversionTests {
        @Test
        @DisplayName("fromEntry should convert Map.Entry to Pair")
        void fromEntryShouldConvertMapEntryToPair() {
            Map.Entry<String, Integer> entry = Map.entry("key", 1);
            Pair<String, Integer> pair = FmkPairUtil.fromEntry(entry);
            
            assertNotNull(pair);
            assertEquals("key", pair.getLeft());
            assertEquals(1, pair.getRight());
        }

        @Test
        @DisplayName("fromEntry should return null for null entry")
        void fromEntryShouldReturnNullForNullEntry() {
            assertNull(FmkPairUtil.fromEntry(null));
        }

        @Test
        @DisplayName("toEntry should convert Pair to Map.Entry")
        void toEntryShouldConvertPairToMapEntry() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            Map.Entry<String, Integer> entry = FmkPairUtil.toEntry(pair);
            
            assertNotNull(entry);
            assertEquals("key", entry.getKey());
            assertEquals(1, entry.getValue());
        }

        @Test
        @DisplayName("toEntry should return null for null pair")
        void toEntryShouldReturnNullForNullPair() {
            assertNull(FmkPairUtil.toEntry(null));
        }

        @Test
        @DisplayName("fromMap should convert Map to List of Pairs")
        void fromMapShouldConvertMapToListOfPairs() {
            Map<String, Integer> map = Map.of("key1", 1, "key2", 2);
            List<Pair<String, Integer>> pairs = FmkPairUtil.fromMap(map);
            
            assertEquals(2, pairs.size());
            
            // Check that all entries are present
            boolean foundKey1 = false;
            boolean foundKey2 = false;
            
            for (Pair<String, Integer> pair : pairs) {
                if ("key1".equals(pair.getLeft()) && 1 == pair.getRight()) {
                    foundKey1 = true;
                } else if ("key2".equals(pair.getLeft()) && 2 == pair.getRight()) {
                    foundKey2 = true;
                }
            }
            
            assertTrue(foundKey1);
            assertTrue(foundKey2);
        }

        @Test
        @DisplayName("fromMap should return empty list for null or empty map")
        void fromMapShouldReturnEmptyListForNullOrEmptyMap() {
            assertTrue(FmkPairUtil.fromMap(null).isEmpty());
            assertTrue(FmkPairUtil.fromMap(Map.of()).isEmpty());
        }

        @Test
        @DisplayName("toMap should convert List of Pairs to Map")
        void toMapShouldConvertListOfPairsToMap() {
            List<Pair<String, Integer>> pairs = List.of(
                    Pair.of("key1", 1),
                    Pair.of("key2", 2)
            );
            
            Map<String, Integer> map = FmkPairUtil.toMap(pairs);
            
            assertEquals(2, map.size());
            assertEquals(1, map.get("key1"));
            assertEquals(2, map.get("key2"));
        }

        @Test
        @DisplayName("toMap should return empty map for null or empty list")
        void toMapShouldReturnEmptyMapForNullOrEmptyList() {
            assertTrue(FmkPairUtil.toMap(null).isEmpty());
            assertTrue(FmkPairUtil.toMap(List.of()).isEmpty());
        }

        @Test
        @DisplayName("toMap should handle null pairs and null left values")
        void toMapShouldHandleNullPairsAndNullLeftValues() {
            List<Pair<String, Integer>> pairs = new ArrayList<>();
            pairs.add(Pair.of("key1", 1));
            pairs.add(null);
            pairs.add(Pair.of(null, 3));
            
            Map<String, Integer> map = FmkPairUtil.toMap(pairs);
            
            assertEquals(1, map.size());
            assertEquals(1, map.get("key1"));
        }
    }

    @Nested
    @DisplayName("Pair Transformation Tests")
    class PairTransformationTests {
        @Test
        @DisplayName("mapLeft should transform left value")
        void mapLeftShouldTransformLeftValue() {
            Pair<String, Integer> original = Pair.of("key", 1);
            Function<String, Integer> mapper = String::length;
            
            Pair<Integer, Integer> transformed = FmkPairUtil.mapLeft(original, mapper);
            
            assertNotNull(transformed);
            assertEquals(3, transformed.getLeft());
            assertEquals(1, transformed.getRight());
        }

        @Test
        @DisplayName("mapLeft should return null for null pair or mapper")
        void mapLeftShouldReturnNullForNullPairOrMapper() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            Function<String, Integer> mapper = String::length;
            
            assertNull(FmkPairUtil.mapLeft(null, mapper));
            assertNull(FmkPairUtil.mapLeft(pair, null));
        }

        @Test
        @DisplayName("mapRight should transform right value")
        void mapRightShouldTransformRightValue() {
            Pair<String, Integer> original = Pair.of("key", 5);
            Function<Integer, String> mapper = i -> "Number: " + i;
            
            Pair<String, String> transformed = FmkPairUtil.mapRight(original, mapper);
            
            assertNotNull(transformed);
            assertEquals("key", transformed.getLeft());
            assertEquals("Number: 5", transformed.getRight());
        }

        @Test
        @DisplayName("mapRight should return null for null pair or mapper")
        void mapRightShouldReturnNullForNullPairOrMapper() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            Function<Integer, String> mapper = i -> "Number: " + i;
            
            assertNull(FmkPairUtil.mapRight(null, mapper));
            assertNull(FmkPairUtil.mapRight(pair, null));
        }

        @Test
        @DisplayName("map should transform both left and right values")
        void mapShouldTransformBothLeftAndRightValues() {
            Pair<String, Integer> original = Pair.of("key", 5);
            Function<String, Integer> leftMapper = String::length;
            Function<Integer, String> rightMapper = i -> "Number: " + i;
            
            Pair<Integer, String> transformed = FmkPairUtil.map(original, leftMapper, rightMapper);
            
            assertNotNull(transformed);
            assertEquals(3, transformed.getLeft());
            assertEquals("Number: 5", transformed.getRight());
        }

        @Test
        @DisplayName("map should return null for null pair or mappers")
        void mapShouldReturnNullForNullPairOrMappers() {
            Pair<String, Integer> pair = Pair.of("key", 1);
            Function<String, Integer> leftMapper = String::length;
            Function<Integer, String> rightMapper = i -> "Number: " + i;
            
            assertNull(FmkPairUtil.map(null, leftMapper, rightMapper));
            assertNull(FmkPairUtil.map(pair, null, rightMapper));
            assertNull(FmkPairUtil.map(pair, leftMapper, null));
        }
    }

    @Nested
    @DisplayName("List Operations Tests")
    class ListOperationsTests {
        @Test
        @DisplayName("filter should filter pairs based on predicate")
        void filterShouldFilterPairsBasedOnPredicate() {
            List<Pair<String, Integer>> pairs = List.of(
                    Pair.of("one", 1),
                    Pair.of("two", 2),
                    Pair.of("three", 3)
            );
            
            List<Pair<String, Integer>> filtered = FmkPairUtil.filter(pairs, p -> p.getRight() % 2 == 1);
            
            assertEquals(2, filtered.size());
            assertEquals("one", filtered.get(0).getLeft());
            assertEquals("three", filtered.get(1).getLeft());
        }

        @Test
        @DisplayName("filter should return empty list for null inputs")
        void filterShouldReturnEmptyListForNullInputs() {
            List<Pair<String, Integer>> pairs = List.of(Pair.of("key", 1));
            
            assertTrue(FmkPairUtil.filter(null, p -> true).isEmpty());
            assertTrue(FmkPairUtil.filter(pairs, null).isEmpty());
            assertTrue(FmkPairUtil.filter(List.of(), p -> true).isEmpty());
        }

        @Test
        @DisplayName("leftList should extract left values from pairs")
        void leftListShouldExtractLeftValuesFromPairs() {
            List<Pair<String, Integer>> pairs = List.of(
                    Pair.of("one", 1),
                    Pair.of("two", 2),
                    Pair.of("three", 3)
            );
            
            List<String> leftValues = FmkPairUtil.leftList(pairs);
            
            assertEquals(3, leftValues.size());
            assertEquals(List.of("one", "two", "three"), leftValues);
        }

        @Test
        @DisplayName("leftList should return empty list for null or empty input")
        void leftListShouldReturnEmptyListForNullOrEmptyInput() {
            assertTrue(FmkPairUtil.leftList(null).isEmpty());
            assertTrue(FmkPairUtil.leftList(List.of()).isEmpty());
        }

        @Test
        @DisplayName("rightList should extract right values from pairs")
        void rightListShouldExtractRightValuesFromPairs() {
            List<Pair<String, Integer>> pairs = List.of(
                    Pair.of("one", 1),
                    Pair.of("two", 2),
                    Pair.of("three", 3)
            );
            
            List<Integer> rightValues = FmkPairUtil.rightList(pairs);
            
            assertEquals(3, rightValues.size());
            assertEquals(List.of(1, 2, 3), rightValues);
        }

        @Test
        @DisplayName("rightList should return empty list for null or empty input")
        void rightListShouldReturnEmptyListForNullOrEmptyInput() {
            assertTrue(FmkPairUtil.rightList(null).isEmpty());
            assertTrue(FmkPairUtil.rightList(List.of()).isEmpty());
        }
    }
}