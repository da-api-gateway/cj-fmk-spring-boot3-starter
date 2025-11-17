// package com.cjlabs.core.collection;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.Arguments;
// import org.junit.jupiter.params.provider.MethodSource;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import java.util.stream.Stream;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// @DisplayName("FmkCollectionUtil Tests")
// class FmkCollectionUtilTest {
//
//     @Nested
//     @DisplayName("isEmpty and isNotEmpty Tests")
//     class EmptyChecks {
//         @Test
//         @DisplayName("isEmpty should return true for null collection")
//         void isEmptyShouldReturnTrueForNullCollection() {
//             assertTrue(FmkCollectionUtil.isEmpty(null));
//         }
//
//         @Test
//         @DisplayName("isEmpty should return true for empty collection")
//         void isEmptyShouldReturnTrueForEmptyCollection() {
//             assertTrue(FmkCollectionUtil.isEmpty(Collections.emptyList()));
//         }
//
//         @Test
//         @DisplayName("isEmpty should return false for non-empty collection")
//         void isEmptyShouldReturnFalseForNonEmptyCollection() {
//             assertFalse(FmkCollectionUtil.isEmpty(List.of("item")));
//         }
//
//         @Test
//         @DisplayName("isNotEmpty should return false for null collection")
//         void isNotEmptyShouldReturnFalseForNullCollection() {
//             assertFalse(FmkCollectionUtil.isNotEmpty(null));
//         }
//
//         @Test
//         @DisplayName("isNotEmpty should return false for empty collection")
//         void isNotEmptyShouldReturnFalseForEmptyCollection() {
//             assertFalse(FmkCollectionUtil.isNotEmpty(Collections.emptyList()));
//         }
//
//         @Test
//         @DisplayName("isNotEmpty should return true for non-empty collection")
//         void isNotEmptyShouldReturnTrueForNonEmptyCollection() {
//             assertTrue(FmkCollectionUtil.isNotEmpty(List.of("item")));
//         }
//     }
//
//     @Nested
//     @DisplayName("emptyIfNull Tests")
//     class EmptyIfNullTests {
//         @Test
//         @DisplayName("emptyIfNull should return empty list for null input")
//         void emptyIfNullShouldReturnEmptyListForNullInput() {
//             List<String> result = FmkCollectionUtil.emptyIfNull(null);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("emptyIfNull should return original list for non-null input")
//         void emptyIfNullShouldReturnOriginalListForNonNullInput() {
//             List<String> original = List.of("item1", "item2");
//             List<String> result = FmkCollectionUtil.emptyIfNull(original);
//             assertSame(original, result);
//         }
//     }
//
//     @Nested
//     @DisplayName("immutableList Tests")
//     class ImmutableListTests {
//         @Test
//         @DisplayName("immutableList should create list with given elements")
//         void immutableListShouldCreateListWithGivenElements() {
//             List<String> result = FmkCollectionUtil.immutableList("a", "b", "c");
//             assertEquals(List.of("a", "b", "c"), result);
//         }
//
//         @Test
//         @DisplayName("immutableList should create empty list when no elements provided")
//         void immutableListShouldCreateEmptyListWhenNoElementsProvided() {
//             List<String> result = FmkCollectionUtil.immutableList();
//             assertTrue(result.isEmpty());
//         }
//     }
//
//     @Nested
//     @DisplayName("partition Tests")
//     class PartitionTests {
//         @Test
//         @DisplayName("partition should return empty list for null input")
//         void partitionShouldReturnEmptyListForNullInput() {
//             List<List<String>> result = FmkCollectionUtil.partition(null, 2);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("partition should return empty list for empty input")
//         void partitionShouldReturnEmptyListForEmptyInput() {
//             List<List<String>> result = FmkCollectionUtil.partition(Collections.emptyList(), 2);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("partition should split list into chunks of specified size")
//         void partitionShouldSplitListIntoChunksOfSpecifiedSize() {
//             List<Integer> input = List.of(1, 2, 3, 4, 5);
//             List<List<Integer>> result = FmkCollectionUtil.partition(input, 2);
//
//             assertEquals(3, result.size());
//             assertEquals(List.of(1, 2), result.get(0));
//             assertEquals(List.of(3, 4), result.get(1));
//             assertEquals(List.of(5), result.get(2));
//         }
//     }
//
//     @Nested
//     @DisplayName("filter Tests")
//     class FilterTests {
//         @Test
//         @DisplayName("filter should return empty list for null input")
//         void filterShouldReturnEmptyListForNullInput() {
//             Collection<Integer> result = FmkCollectionUtil.filter(null, i -> i > 0);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("filter should return elements that satisfy predicate")
//         void filterShouldReturnElementsThatSatisfyPredicate() {
//             Collection<Integer> input = List.of(-2, -1, 0, 1, 2);
//             Collection<Integer> result = FmkCollectionUtil.filter(input, i -> i > 0);
//             assertEquals(List.of(1, 2), result);
//         }
//     }
//
//     @Nested
//     @DisplayName("transform Tests")
//     class TransformTests {
//         @Test
//         @DisplayName("transform should return empty list for null input")
//         void transformShouldReturnEmptyListForNullInput() {
//             Collection<String> result = FmkCollectionUtil.transform(null, Object::toString);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("transform should apply function to each element")
//         void transformShouldApplyFunctionToEachElement() {
//             Collection<Integer> input = List.of(1, 2, 3);
//             Collection<String> result = FmkCollectionUtil.transform(input, i -> "Number: " + i);
//             assertEquals(List.of("Number: 1", "Number: 2", "Number: 3"), result);
//         }
//     }
//
//     @Nested
//     @DisplayName("toList Tests")
//     class ToListTests {
//         @Test
//         @DisplayName("toList should return empty list for null input")
//         void toListShouldReturnEmptyListForNullInput() {
//             List<String> result = FmkCollectionUtil.toList(null);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("toList should convert collection to ArrayList")
//         void toListShouldConvertCollectionToArrayList() {
//             Set<String> input = Set.of("a", "b", "c");
//             List<String> result = FmkCollectionUtil.toList(input);
//
//             assertTrue(result instanceof ArrayList);
//             assertEquals(3, result.size());
//             assertTrue(result.containsAll(input));
//         }
//     }
//
//     @Nested
//     @DisplayName("toSet Tests")
//     class ToSetTests {
//         @Test
//         @DisplayName("toSet should return empty set for null input")
//         void toSetShouldReturnEmptySetForNullInput() {
//             Set<String> result = FmkCollectionUtil.toSet(null);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("toSet should convert collection to HashSet")
//         void toSetShouldConvertCollectionToHashSet() {
//             List<String> input = List.of("a", "b", "c", "a");
//             Set<String> result = FmkCollectionUtil.toSet(input);
//
//             assertTrue(result instanceof HashSet);
//             assertEquals(3, result.size());
//             assertTrue(result.contains("a"));
//             assertTrue(result.contains("b"));
//             assertTrue(result.contains("c"));
//         }
//     }
//
//     @Nested
//     @DisplayName("Set Operation Tests")
//     class SetOperationTests {
//         @Test
//         @DisplayName("intersection should return common elements")
//         void intersectionShouldReturnCommonElements() {
//             Collection<String> a = List.of("a", "b", "c");
//             Collection<String> b = List.of("b", "c", "d");
//
//             Collection<String> result = FmkCollectionUtil.intersection(a, b);
//             assertEquals(Set.of("b", "c"), new HashSet<>(result));
//         }
//
//         @Test
//         @DisplayName("union should return combined elements without duplicates")
//         void unionShouldReturnCombinedElementsWithoutDuplicates() {
//             Collection<String> a = List.of("a", "b", "c");
//             Collection<String> b = List.of("b", "c", "d");
//
//             Collection<String> result = FmkCollectionUtil.union(a, b);
//             assertEquals(Set.of("a", "b", "c", "d"), new HashSet<>(result));
//         }
//
//         @Test
//         @DisplayName("subtract should return elements in first collection not in second")
//         void subtractShouldReturnElementsInFirstCollectionNotInSecond() {
//             Collection<String> a = List.of("a", "b", "c");
//             Collection<String> b = List.of("b", "c", "d");
//
//             Collection<String> result = FmkCollectionUtil.subtract(a, b);
//             assertEquals(List.of("a"), result);
//         }
//     }
//
//     @Nested
//     @DisplayName("Array Conversion Tests")
//     class ArrayConversionTests {
//         @Test
//         @DisplayName("arrayToList should return empty list for null input")
//         void arrayToListShouldReturnEmptyListForNullInput() {
//             List<String> result = FmkCollectionUtil.arrayToList(null);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("arrayToList should convert array to ArrayList")
//         void arrayToListShouldConvertArrayToArrayList() {
//             String[] input = {"a", "b", "c"};
//             List<String> result = FmkCollectionUtil.arrayToList(input);
//
//             assertTrue(result instanceof ArrayList);
//             assertEquals(3, result.size());
//             assertEquals(Arrays.asList(input), result);
//         }
//
//         @Test
//         @DisplayName("listToArray should return empty array for null input")
//         void listToArrayShouldReturnEmptyArrayForNullInput() {
//             String[] result = FmkCollectionUtil.listToArray(null, String.class);
//             assertNotNull(result);
//             assertEquals(0, result.length);
//         }
//
//         @Test
//         @DisplayName("listToArray should convert list to array of specified type")
//         void listToArrayShouldConvertListToArrayOfSpecifiedType() {
//             List<String> input = List.of("a", "b", "c");
//             String[] result = FmkCollectionUtil.listToArray(input, String.class);
//
//             assertEquals(3, result.length);
//             assertArrayEquals(new String[]{"a", "b", "c"}, result);
//         }
//     }
//
//     @Nested
//     @DisplayName("toMap Tests")
//     class ToMapTests {
//         @Test
//         @DisplayName("toMap should return empty map for null input")
//         void toMapShouldReturnEmptyMapForNullInput() {
//             Map<String, Integer> result = FmkCollectionUtil.toMap(null, Object::toString);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("toMap should convert collection to map using key mapper")
//         void toMapShouldConvertCollectionToMapUsingKeyMapper() {
//             List<String> input = List.of("apple", "banana", "cherry1");
//             Map<Integer, String> result = FmkCollectionUtil.toMap(input, String::length);
//
//             assertEquals(3, result.size());
//             assertEquals("apple", result.get(5));
//             assertEquals("banana", result.get(6));
//             assertEquals("cherry1", result.get(7)); // Note: This will be overridden by the first value
//         }
//     }
//
//     @Nested
//     @DisplayName("containsAny and containsAll Tests")
//     class ContainsTests {
//         @Test
//         @DisplayName("containsAny should return true if collections have common elements")
//         void containsAnyShouldReturnTrueIfCollectionsHaveCommonElements() {
//             Collection<String> a = List.of("a", "b", "c");
//             Collection<String> b = List.of("c", "d", "e");
//
//             assertTrue(FmkCollectionUtil.containsAny(a, b));
//         }
//
//         @Test
//         @DisplayName("containsAny should return false if collections have no common elements")
//         void containsAnyShouldReturnFalseIfCollectionsHaveNoCommonElements() {
//             Collection<String> a = List.of("a", "b", "c");
//             Collection<String> b = List.of("d", "e", "f");
//
//             assertFalse(FmkCollectionUtil.containsAny(a, b));
//         }
//
//         @Test
//         @DisplayName("containsAll should return true if collection contains all elements")
//         void containsAllShouldReturnTrueIfCollectionContainsAllElements() {
//             Collection<String> collection = List.of("a", "b", "c", "d");
//
//             assertTrue(FmkCollectionUtil.containsAll(collection, "a", "c"));
//         }
//
//         @Test
//         @DisplayName("containsAll should return false if collection doesn't contain all elements")
//         void containsAllShouldReturnFalseIfCollectionDoesntContainAllElements() {
//             Collection<String> collection = List.of("a", "b", "c");
//
//             assertFalse(FmkCollectionUtil.containsAll(collection, "a", "d"));
//         }
//     }
//
//     @Nested
//     @DisplayName("removeNulls Tests")
//     class RemoveNullsTests {
//         @Test
//         @DisplayName("removeNulls should return empty collection for null input")
//         void removeNullsShouldReturnEmptyCollectionForNullInput() {
//             Collection<String> result = FmkCollectionUtil.removeNulls(null);
//             assertNotNull(result);
//             assertTrue(result.isEmpty());
//         }
//
//         @Test
//         @DisplayName("removeNulls should remove null elements from collection")
//         void removeNullsShouldRemoveNullElementsFromCollection() {
//             List<String> input = new ArrayList<>();
//             input.add("a");
//             input.add(null);
//             input.add("b");
//             input.add(null);
//
//             Collection<String> result = FmkCollectionUtil.removeNulls(input);
//             assertEquals(List.of("a", "b"), result);
//         }
//     }
//
//     @Nested
//     @DisplayName("getFirst Tests")
//     class GetFirstTests {
//         @Test
//         @DisplayName("getFirst should return null for null input")
//         void getFirstShouldReturnNullForNullInput() {
//             assertNull(FmkCollectionUtil.getFirst(null));
//         }
//
//         @Test
//         @DisplayName("getFirst should return null for empty collection")
//         void getFirstShouldReturnNullForEmptyCollection() {
//             assertNull(FmkCollectionUtil.getFirst(Collections.emptyList()));
//         }
//
//         @Test
//         @DisplayName("getFirst should return first element of collection")
//         void getFirstShouldReturnFirstElementOfCollection() {
//             List<String> input = List.of("first", "second", "third");
//             assertEquals("first", FmkCollectionUtil.getFirst(input));
//         }
//     }
//
//     @Nested
//     @DisplayName("Parameterized Tests")
//     class ParameterizedTests {
//
//         static Stream<Arguments> emptyCollectionTestCases() {
//             return Stream.of(
//                 Arguments.of(null, true),
//                 Arguments.of(Collections.emptyList(), true),
//                 Arguments.of(List.of("item"), false)
//             );
//         }
//
//         @ParameterizedTest(name = "isEmpty({0}) should return {1}")
//         @MethodSource("emptyCollectionTestCases")
//         void isEmptyParameterizedTest(Collection<?> collection, boolean expected) {
//             assertEquals(expected, FmkCollectionUtil.isEmpty(collection));
//         }
//
//         static Stream<Arguments> notEmptyCollectionTestCases() {
//             return Stream.of(
//                 Arguments.of(null, false),
//                 Arguments.of(Collections.emptyList(), false),
//                 Arguments.of(List.of("item"), true)
//             );
//         }
//
//         @ParameterizedTest(name = "isNotEmpty({0}) should return {1}")
//         @MethodSource("notEmptyCollectionTestCases")
//         void isNotEmptyParameterizedTest(Collection<?> collection, boolean expected) {
//             assertEquals(expected, FmkCollectionUtil.isNotEmpty(collection));
//         }
//     }
// }