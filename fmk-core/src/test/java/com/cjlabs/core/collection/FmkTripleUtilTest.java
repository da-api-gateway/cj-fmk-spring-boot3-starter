package com.cjlabs.core.collection;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkTripleUtil Tests")
class FmkTripleUtilTest {

    @Nested
    @DisplayName("Triple Creation Tests")
    class TripleCreationTests {
        @Test
        @DisplayName("of should create immutable triple")
        void ofShouldCreateImmutableTriple() {
            Triple<String, Integer, Boolean> triple = FmkTripleUtil.of("key", 1, true);
            
            assertNotNull(triple);
            assertEquals("key", triple.getLeft());
            assertEquals(1, triple.getMiddle());
            assertEquals(true, triple.getRight());
        }

        @Test
        @DisplayName("ofMutable should create mutable triple")
        void ofMutableShouldCreateMutableTriple() {
            MutableTriple<String, Integer, Boolean> triple = FmkTripleUtil.ofMutable("key", 1, true);
            
            assertNotNull(triple);
            assertEquals("key", triple.getLeft());
            assertEquals(1, triple.getMiddle());
            assertEquals(true, triple.getRight());
            
            // Test mutability
            triple.setLeft("newKey");
            triple.setMiddle(2);
            triple.setRight(false);
            
            assertEquals("newKey", triple.getLeft());
            assertEquals(2, triple.getMiddle());
            assertEquals(false, triple.getRight());
        }

        @Test
        @DisplayName("ofNullable should return null when all values are null")
        void ofNullableShouldReturnNullWhenAllValuesAreNull() {
            Triple<String, Integer, Boolean> triple = FmkTripleUtil.ofNullable(null, null, null);
            assertNull(triple);
        }

        @Test
        @DisplayName("ofNullable should create triple when at least one value is not null")
        void ofNullableShouldCreateTripleWhenAtLeastOneValueIsNotNull() {
            Triple<String, Integer, Boolean> triple1 = FmkTripleUtil.ofNullable("key", null, null);
            Triple<String, Integer, Boolean> triple2 = FmkTripleUtil.ofNullable(null, 1, null);
            Triple<String, Integer, Boolean> triple3 = FmkTripleUtil.ofNullable(null, null, true);
            
            assertNotNull(triple1);
            assertEquals("key", triple1.getLeft());
            assertNull(triple1.getMiddle());
            assertNull(triple1.getRight());
            
            assertNotNull(triple2);
            assertNull(triple2.getLeft());
            assertEquals(1, triple2.getMiddle());
            assertNull(triple2.getRight());
            
            assertNotNull(triple3);
            assertNull(triple3.getLeft());
            assertNull(triple3.getMiddle());
            assertEquals(true, triple3.getRight());
        }

        @Test
        @DisplayName("tripleOf should create triple with same value for all positions")
        void tripleOfShouldCreateTripleWithSameValueForAllPositions() {
            Triple<String, String, String> triple = FmkTripleUtil.tripleOf("value");
            
            assertNotNull(triple);
            assertEquals("value", triple.getLeft());
            assertEquals("value", triple.getMiddle());
            assertEquals("value", triple.getRight());
        }
    }

    @Nested
    @DisplayName("Triple Access Tests")
    class TripleAccessTests {
        @Test
        @DisplayName("getLeft should return left value")
        void getLeftShouldReturnLeftValue() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            assertEquals("key", FmkTripleUtil.getLeft(triple));
        }

        @Test
        @DisplayName("getLeft should return null for null triple")
        void getLeftShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.getLeft(null));
        }

        @Test
        @DisplayName("getMiddle should return middle value")
        void getMiddleShouldReturnMiddleValue() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            assertEquals(1, FmkTripleUtil.getMiddle(triple));
        }

        @Test
        @DisplayName("getMiddle should return null for null triple")
        void getMiddleShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.getMiddle(null));
        }

        @Test
        @DisplayName("getRight should return right value")
        void getRightShouldReturnRightValue() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            assertEquals(true, FmkTripleUtil.getRight(triple));
        }

        @Test
        @DisplayName("getRight should return null for null triple")
        void getRightShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.getRight(null));
        }
    }

    @Nested
    @DisplayName("Triple to Pair Conversion Tests")
    class TripleToPairConversionTests {
        @Test
        @DisplayName("toLeftMiddlePair should convert triple to pair of left and middle")
        void toLeftMiddlePairShouldConvertTripleToPairOfLeftAndMiddle() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            Pair<String, Integer> pair = FmkTripleUtil.toLeftMiddlePair(triple);
            
            assertNotNull(pair);
            assertEquals("key", pair.getLeft());
            assertEquals(1, pair.getRight());
        }

        @Test
        @DisplayName("toLeftMiddlePair should return null for null triple")
        void toLeftMiddlePairShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.toLeftMiddlePair(null));
        }

        @Test
        @DisplayName("toLeftRightPair should convert triple to pair of left and right")
        void toLeftRightPairShouldConvertTripleToPairOfLeftAndRight() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            Pair<String, Boolean> pair = FmkTripleUtil.toLeftRightPair(triple);
            
            assertNotNull(pair);
            assertEquals("key", pair.getLeft());
            assertEquals(true, pair.getRight());
        }

        @Test
        @DisplayName("toLeftRightPair should return null for null triple")
        void toLeftRightPairShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.toLeftRightPair(null));
        }

        @Test
        @DisplayName("toMiddleRightPair should convert triple to pair of middle and right")
        void toMiddleRightPairShouldConvertTripleToPairOfMiddleAndRight() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            Pair<Integer, Boolean> pair = FmkTripleUtil.toMiddleRightPair(triple);
            
            assertNotNull(pair);
            assertEquals(1, pair.getLeft());
            assertEquals(true, pair.getRight());
        }

        @Test
        @DisplayName("toMiddleRightPair should return null for null triple")
        void toMiddleRightPairShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.toMiddleRightPair(null));
        }
    }

    @Nested
    @DisplayName("Triple Operation Tests")
    class TripleOperationTests {
        @Test
        @DisplayName("equals should return true for identical triples")
        void equalsShouldReturnTrueForIdenticalTriples() {
            Triple<String, Integer, Boolean> triple1 = Triple.of("key", 1, true);
            Triple<String, Integer, Boolean> triple2 = Triple.of("key", 1, true);
            
            assertTrue(FmkTripleUtil.equals(triple1, triple2));
        }

        @Test
        @DisplayName("equals should return true for same instance")
        void equalsShouldReturnTrueForSameInstance() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            assertTrue(FmkTripleUtil.equals(triple, triple));
        }

        @Test
        @DisplayName("equals should return false for different triples")
        void equalsShouldReturnFalseForDifferentTriples() {
            Triple<String, Integer, Boolean> triple1 = Triple.of("key1", 1, true);
            Triple<String, Integer, Boolean> triple2 = Triple.of("key2", 2, false);
            
            assertFalse(FmkTripleUtil.equals(triple1, triple2));
        }

        @Test
        @DisplayName("equals should return false when one triple is null")
        void equalsShouldReturnFalseWhenOneTripleIsNull() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            
            assertFalse(FmkTripleUtil.equals(triple, null));
            assertFalse(FmkTripleUtil.equals(null, triple));
        }

        @Test
        @DisplayName("equals should return true when both triples are null")
        void equalsShouldReturnTrueWhenBothTriplesAreNull() {
            assertTrue(FmkTripleUtil.equals(null, null));
        }

        @Test
        @DisplayName("swapLeftRight should exchange left and right values")
        void swapLeftRightShouldExchangeLeftAndRightValues() {
            Triple<String, Integer, Boolean> original = Triple.of("key", 1, true);
            Triple<Boolean, Integer, String> swapped = FmkTripleUtil.swapLeftRight(original);
            
            assertNotNull(swapped);
            assertEquals(true, swapped.getLeft());
            assertEquals(1, swapped.getMiddle());
            assertEquals("key", swapped.getRight());
        }

        @Test
        @DisplayName("swapLeftRight should return null for null triple")
        void swapLeftRightShouldReturnNullForNullTriple() {
            assertNull(FmkTripleUtil.swapLeftRight(null));
        }

        @Test
        @DisplayName("toString should return string representation of triple")
        void toStringShouldReturnStringRepresentationOfTriple() {
            Triple<String, Integer, Boolean> triple = Triple.of("key", 1, true);
            assertEquals(triple.toString(), FmkTripleUtil.toString(triple));
        }

        @Test
        @DisplayName("toString should return 'null' for null triple")
        void toStringShouldReturnNullForNullTriple() {
            assertEquals("null", FmkTripleUtil.toString(null));
        }
    }

    @Nested
    @DisplayName("Pair to Triple Conversion Tests")
    class PairToTripleConversionTests {
        @Test
        @DisplayName("fromPair should convert pair to triple with given middle value")
        void fromPairShouldConvertPairToTripleWithGivenMiddleValue() {
            Pair<String, Boolean> pair = Pair.of("key", true);
            Triple<String, Integer, Boolean> triple = FmkTripleUtil.fromPair(pair, 1);
            
            assertNotNull(triple);
            assertEquals("key", triple.getLeft());
            assertEquals(1, triple.getMiddle());
            assertEquals(true, triple.getRight());
        }

        @Test
        @DisplayName("fromPair should return null for null pair")
        void fromPairShouldReturnNullForNullPair() {
            assertNull(FmkTripleUtil.fromPair(null, 1));
        }
    }
}