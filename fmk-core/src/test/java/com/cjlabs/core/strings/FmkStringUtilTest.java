package com.cjlabs.core.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkStringUtil Tests")
class FmkStringUtilTest {

    @Nested
    @DisplayName("基础判断测试")
    class BasicChecksTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("isEmpty 应该对 null 和空字符串返回 true")
        void isEmptyShouldReturnTrueForNullAndEmptyString(String input) {
            assertTrue(FmkStringUtil.isEmpty(input));
        }

        @Test
        @DisplayName("isEmpty 应该对非空字符串返回 false")
        void isEmptyShouldReturnFalseForNonEmptyString() {
            assertFalse(FmkStringUtil.isEmpty("test"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n", "\r"})
        @DisplayName("isBlank 应该对 null、空字符串和空白字符返回 true")
        void isBlankShouldReturnTrueForNullEmptyAndWhitespaceString(String input) {
            assertTrue(FmkStringUtil.isBlank(input));
        }

        @Test
        @DisplayName("isBlank 应该对非空白字符串返回 false")
        void isBlankShouldReturnFalseForNonBlankString() {
            assertFalse(FmkStringUtil.isBlank("test"));
        }

        @Test
        @DisplayName("isNotEmpty 应该对非空字符串返回 true")
        void isNotEmptyShouldReturnTrueForNonEmptyString() {
            assertTrue(FmkStringUtil.isNotEmpty("test"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("isNotEmpty 应该对 null 和空字符串返回 false")
        void isNotEmptyShouldReturnFalseForNullAndEmptyString(String input) {
            assertFalse(FmkStringUtil.isNotEmpty(input));
        }

        @Test
        @DisplayName("isNotBlank 应该对非空白字符串返回 true")
        void isNotBlankShouldReturnTrueForNonBlankString() {
            assertTrue(FmkStringUtil.isNotBlank("test"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n", "\r"})
        @DisplayName("isNotBlank 应该对 null、空字符串和空白字符返回 false")
        void isNotBlankShouldReturnFalseForNullEmptyAndWhitespaceString(String input) {
            assertFalse(FmkStringUtil.isNotBlank(input));
        }

        @Test
        @DisplayName("isNoneBlank 应该在所有参数都非空白时返回 true")
        void isNoneBlankShouldReturnTrueWhenAllParametersAreNotBlank() {
            assertTrue(FmkStringUtil.isNoneBlank("test1", "test2", "test3"));
        }

        @Test
        @DisplayName("isNoneBlank 应该在任一参数为空白时返回 false")
        void isNoneBlankShouldReturnFalseWhenAnyParameterIsBlank() {
            assertFalse(FmkStringUtil.isNoneBlank("test1", "", "test3"));
            assertFalse(FmkStringUtil.isNoneBlank("test1", " ", "test3"));
            assertFalse(FmkStringUtil.isNoneBlank("test1", null, "test3"));
        }

        @Test
        @DisplayName("isAllBlank 应该在所有参数都为空白时返回 true")
        void isAllBlankShouldReturnTrueWhenAllParametersAreBlank() {
            assertTrue(FmkStringUtil.isAllBlank("", " ", null));
        }

        @Test
        @DisplayName("isAllBlank 应该在任一参数非空白时返回 false")
        void isAllBlankShouldReturnFalseWhenAnyParameterIsNotBlank() {
            assertFalse(FmkStringUtil.isAllBlank("", "test", null));
        }
    }

    @Nested
    @DisplayName("字符串处理测试")
    class StringProcessingTests {

        @Test
        @DisplayName("defaultString 应该将 null 转换为空字符串")
        void defaultStringShouldConvertNullToEmptyString() {
            assertEquals("", FmkStringUtil.defaultString(null));
        }

        @Test
        @DisplayName("defaultString 应该保留非 null 字符串")
        void defaultStringShouldPreserveNonNullString() {
            assertEquals("test", FmkStringUtil.defaultString("test"));
        }

        @Test
        @DisplayName("defaultIfNull 应该在输入为 null 或空时返回默认值")
        void defaultIfNullShouldReturnDefaultValueWhenInputIsNullOrEmpty() {
            assertEquals("default", FmkStringUtil.defaultIfNull(null, "default"));
            assertEquals("default", FmkStringUtil.defaultIfNull("", "default"));
        }

        @Test
        @DisplayName("defaultIfNull 应该在输入非空时返回输入值")
        void defaultIfNullShouldReturnInputWhenInputIsNotEmpty() {
            assertEquals("test", FmkStringUtil.defaultIfNull("test", "default"));
        }

        @Test
        @DisplayName("defaultIfBlank 应该在输入为空白时返回默认值")
        void defaultIfBlankShouldReturnDefaultValueWhenInputIsBlank() {
            assertEquals("default", FmkStringUtil.defaultIfBlank(null, "default"));
            assertEquals("default", FmkStringUtil.defaultIfBlank("", "default"));
            assertEquals("default", FmkStringUtil.defaultIfBlank(" ", "default"));
        }

        @Test
        @DisplayName("defaultIfBlank 应该在输入非空白时返回输入值")
        void defaultIfBlankShouldReturnInputWhenInputIsNotBlank() {
            assertEquals("test", FmkStringUtil.defaultIfBlank("test", "default"));
        }

        @Test
        @DisplayName("truncate 应该将字符串截断到指定长度")
        void truncateShouldLimitStringToSpecifiedLength() {
            assertEquals("abc", FmkStringUtil.truncate("abcdef", 3));
            assertEquals("abcdef", FmkStringUtil.truncate("abcdef", 10));
            assertEquals("", FmkStringUtil.truncate("abcdef", 0));
            assertNull(FmkStringUtil.truncate(null, 5));
        }

        // @Test
        // @DisplayName("abbreviate 应该将字符串截断并添加省略号")
        // void abbreviateShouldTruncateStringAndAddEllipsis() {
        //     assertEquals("abc...", FmkStringUtil.abbreviate("abcdef", 6));
        //     assertEquals("abcdef", FmkStringUtil.abbreviate("abcdef", 10));
        //     assertNull(FmkStringUtil.abbreviate(null, 5));
        // }

        @Test
        @DisplayName("trim 应该移除字符串两端的空白字符")
        void trimShouldRemoveWhitespaceFromBothEnds() {
            assertEquals("test", FmkStringUtil.trim(" test "));
            assertEquals("", FmkStringUtil.trim(" "));
            assertNull(FmkStringUtil.trim(null));
        }

        @Test
        @DisplayName("deleteWhitespace 应该移除所有空白字符")
        void deleteWhitespaceShouldRemoveAllWhitespace() {
            assertEquals("test", FmkStringUtil.deleteWhitespace(" t e s t "));
            assertEquals("", FmkStringUtil.deleteWhitespace(" "));
            assertNull(FmkStringUtil.deleteWhitespace(null));
        }

        @Test
        @DisplayName("toLowerCase 应该将字符串转换为小写")
        void toLowerCaseShouldConvertStringToLowerCase() {
            assertEquals("test", FmkStringUtil.toLowerCase("TEST"));
            assertEquals("test", FmkStringUtil.toLowerCase("Test"));
            assertNull(FmkStringUtil.toLowerCase(null));
        }

        @Test
        @DisplayName("toUpperCase 应该将字符串转换为大写")
        void toUpperCaseShouldConvertStringToUpperCase() {
            assertEquals("TEST", FmkStringUtil.toUpperCase("test"));
            assertEquals("TEST", FmkStringUtil.toUpperCase("Test"));
            assertNull(FmkStringUtil.toUpperCase(null));
        }

        @Test
        @DisplayName("capitalize 应该将首字母转换为大写")
        void capitalizeShouldCapitalizeFirstLetter() {
            assertEquals("Test", FmkStringUtil.capitalize("test"));
            assertEquals("Test", FmkStringUtil.capitalize("Test"));
            assertEquals("", FmkStringUtil.capitalize(""));
            assertNull(FmkStringUtil.capitalize(null));
        }

        @Test
        @DisplayName("uncapitalize 应该将首字母转换为小写")
        void uncapitalizeShouldUncapitalizeFirstLetter() {
            assertEquals("test", FmkStringUtil.uncapitalize("Test"));
            assertEquals("test", FmkStringUtil.uncapitalize("test"));
            assertEquals("", FmkStringUtil.uncapitalize(""));
            assertNull(FmkStringUtil.uncapitalize(null));
        }

        @Test
        @DisplayName("capitalizeWords 应该将每个单词的首字母转换为大写")
        void capitalizeWordsShouldCapitalizeEachWord() {
            assertEquals("Hello World", FmkStringUtil.capitalizeWords("hello world"));
            assertEquals("Hello World", FmkStringUtil.capitalizeWords("Hello World"));
            assertEquals("", FmkStringUtil.capitalizeWords(""));
            assertNull(FmkStringUtil.capitalizeWords(null));
        }
    }

    @Nested
    @DisplayName("字符串操作测试")
    class StringOperationTests {

        @Test
        @DisplayName("contains 应该检查字符串是否包含子字符串")
        void containsShouldCheckIfStringContainsSubstring() {
            assertTrue(FmkStringUtil.contains("abcdef", "cd"));
            assertFalse(FmkStringUtil.contains("abcdef", "xy"));
            assertFalse(FmkStringUtil.contains(null, "cd"));
            assertFalse(FmkStringUtil.contains("abcdef", null));
        }

        @Test
        @DisplayName("containsIgnoreCase 应该忽略大小写检查字符串是否包含子字符串")
        void containsIgnoreCaseShouldCheckIfStringContainsSubstringIgnoringCase() {
            assertTrue(FmkStringUtil.containsIgnoreCase("abcDEF", "def"));
            assertFalse(FmkStringUtil.containsIgnoreCase("abcdef", "xy"));
            assertFalse(FmkStringUtil.containsIgnoreCase(null, "cd"));
            assertFalse(FmkStringUtil.containsIgnoreCase("abcdef", null));
        }

        @Test
        @DisplayName("startsWith 应该检查字符串是否以指定前缀开始")
        void startsWithShouldCheckIfStringStartsWithPrefix() {
            assertTrue(FmkStringUtil.startsWith("abcdef", "abc"));
            assertFalse(FmkStringUtil.startsWith("abcdef", "def"));
            assertFalse(FmkStringUtil.startsWith(null, "abc"));
            assertFalse(FmkStringUtil.startsWith("abcdef", null));
        }

        @Test
        @DisplayName("startsWithIgnoreCase 应该忽略大小写检查字符串是否以指定前缀开始")
        void startsWithIgnoreCaseShouldCheckIfStringStartsWithPrefixIgnoringCase() {
            assertTrue(FmkStringUtil.startsWithIgnoreCase("ABCdef", "abc"));
            assertFalse(FmkStringUtil.startsWithIgnoreCase("abcdef", "def"));
            assertFalse(FmkStringUtil.startsWithIgnoreCase(null, "abc"));
            assertFalse(FmkStringUtil.startsWithIgnoreCase("abcdef", null));
        }

        @Test
        @DisplayName("endsWith 应该检查字符串是否以指定后缀结束")
        void endsWithShouldCheckIfStringEndsWithSuffix() {
            assertTrue(FmkStringUtil.endsWith("abcdef", "def"));
            assertFalse(FmkStringUtil.endsWith("abcdef", "abc"));
            assertFalse(FmkStringUtil.endsWith(null, "def"));
            assertFalse(FmkStringUtil.endsWith("abcdef", null));
        }

        @Test
        @DisplayName("endsWithIgnoreCase 应该忽略大小写检查字符串是否以指定后缀结束")
        void endsWithIgnoreCaseShouldCheckIfStringEndsWithSuffixIgnoringCase() {
            assertTrue(FmkStringUtil.endsWithIgnoreCase("abcDEF", "def"));
            assertFalse(FmkStringUtil.endsWithIgnoreCase("abcdef", "abc"));
            assertFalse(FmkStringUtil.endsWithIgnoreCase(null, "def"));
            assertFalse(FmkStringUtil.endsWithIgnoreCase("abcdef", null));
        }

        @Test
        @DisplayName("replace 应该替换字符串中的指定子字符串")
        void replaceShouldReplaceSubstring() {
            assertEquals("abXYef", FmkStringUtil.replace("abcdef", "cd", "XY"));
            assertEquals("abcdef", FmkStringUtil.replace("abcdef", "xy", "XY"));
            assertNull(FmkStringUtil.replace(null, "cd", "XY"));
            assertEquals("abcdef", FmkStringUtil.replace("abcdef", null, "XY"));
            assertEquals("abcdef", FmkStringUtil.replace("abcdef", "cd", null));
        }

        @Test
        @DisplayName("replaceAll 应该使用正则表达式替换所有匹配项")
        void replaceAllShouldReplaceAllMatchesUsingRegex() {
            assertEquals("a-b-c", FmkStringUtil.replaceAll("a1b2c", "\\d", "-"));
            assertNull(FmkStringUtil.replaceAll(null, "\\d", "-"));
        }
    }

    @Nested
    @DisplayName("字符串生成测试")
    class StringGenerationTests {

        @Test
        @DisplayName("uuid 应该生成不带连字符的UUID")
        void uuidShouldGenerateUuidWithoutHyphens() {
            String uuid = FmkStringUtil.uuid();
            assertNotNull(uuid);
            assertEquals(32, uuid.length());
            assertFalse(uuid.contains("-"));
        }

        @Test
        @DisplayName("uuidWithHyphens 应该生成带连字符的UUID")
        void uuidWithHyphensShouldGenerateUuidWithHyphens() {
            String uuid = FmkStringUtil.uuidWithHyphens();
            assertNotNull(uuid);
            assertEquals(36, uuid.length());
            assertTrue(uuid.contains("-"));
        }
    }

    @Nested
    @DisplayName("字符串验证测试")
    class StringValidationTests {

        @ParameterizedTest
        @CsvSource({
                "user@example.com, true",
                "user.name@example.co.uk, true",
                "user-name@example.org, true",
                "invalid-email, false",
                "@example.com, false",
                "user@, false"
        })
        @DisplayName("isValidEmail 应该验证电子邮件地址")
        void isValidEmailShouldValidateEmailAddress(String email, boolean expected) {
            assertEquals(expected, FmkStringUtil.isValidEmail(email));
        }

        @Test
        @DisplayName("isValidEmail 应该对空白输入返回 false")
        void isValidEmailShouldReturnFalseForBlankInput() {
            assertFalse(FmkStringUtil.isValidEmail(null));
            assertFalse(FmkStringUtil.isValidEmail(""));
            assertFalse(FmkStringUtil.isValidEmail(" "));
        }

        @ParameterizedTest
        @CsvSource({
                "https://example.com, true",
                "http://example.com, true",
                "ftp://example.com, true",
                "file:///path/to/file, true",
                "example.com, false",
                "htt://example.com, false"
        })
        @DisplayName("isValidUrl 应该验证URL")
        void isValidUrlShouldValidateUrl(String url, boolean expected) {
            assertEquals(expected, FmkStringUtil.isValidUrl(url));
        }

        @Test
        @DisplayName("isValidUrl 应该对空白输入返回 false")
        void isValidUrlShouldReturnFalseForBlankInput() {
            assertFalse(FmkStringUtil.isValidUrl(null));
            assertFalse(FmkStringUtil.isValidUrl(""));
            assertFalse(FmkStringUtil.isValidUrl(" "));
        }

        @ParameterizedTest
        @CsvSource({
                "123456, true",
                "0, true",
                "123abc, false",
                "' 123', false"
        })
        @DisplayName("isNumeric 应该检查字符串是否只包含数字")
        void isNumericShouldCheckIfStringContainsOnlyDigits(String input, boolean expected) {
            System.out.println("Testing isNumeric with input: ------" + input);
            assertEquals(expected, FmkStringUtil.isNumeric(input));
        }

        @Test
        @DisplayName("isNumeric 应该对 null 返回 false")
        void isNumericShouldReturnFalseForNull() {
            assertFalse(FmkStringUtil.isNumeric(null));
        }

        @ParameterizedTest
        @CsvSource({
                "abcDEF, true",
                "abc123, false",
                "abc def, false"
        })
        @DisplayName("isAlpha 应该检查字符串是否只包含字母")
        void isAlphaShouldCheckIfStringContainsOnlyLetters(String input, boolean expected) {
            assertEquals(expected, FmkStringUtil.isAlpha(input));
        }

        @Test
        @DisplayName("isAlpha 应该对 null 返回 false")
        void isAlphaShouldReturnFalseForNull() {
            assertFalse(FmkStringUtil.isAlpha(null));
        }

        @ParameterizedTest
        @CsvSource({
                "abc123, true",
                "abcDEF123, true",
                "abc 123, false",
                "abc-123, false"
        })
        @DisplayName("isAlphanumeric 应该检查字符串是否只包含字母和数字")
        void isAlphanumericShouldCheckIfStringContainsOnlyLettersAndDigits(String input, boolean expected) {
            assertEquals(expected, FmkStringUtil.isAlphanumeric(input));
        }

        @Test
        @DisplayName("isAlphanumeric 应该对 null 返回 false")
        void isAlphanumericShouldReturnFalseForNull() {
            assertFalse(FmkStringUtil.isAlphanumeric(null));
        }
    }

    @Nested
    @DisplayName("字符串转换测试")
    class StringConversionTests {

        @Test
        @DisplayName("toBytes 应该将字符串转换为字节数组")
        void toBytesShouldConvertStringToByteArray() {
            byte[] expected = "test".getBytes(StandardCharsets.UTF_8);
            assertArrayEquals(expected, FmkStringUtil.toBytes("test"));
            assertArrayEquals(new byte[0], FmkStringUtil.toBytes(null));
            assertArrayEquals(new byte[0], FmkStringUtil.toBytes(""));
        }

        @Test
        @DisplayName("fromBytes 应该将字节数组转换为字符串")
        void fromBytesShouldConvertByteArrayToString() {
            byte[] bytes = "test".getBytes(StandardCharsets.UTF_8);
            assertEquals("test", FmkStringUtil.fromBytes(bytes));
            assertEquals("", FmkStringUtil.fromBytes(null));
            assertEquals("", FmkStringUtil.fromBytes(new byte[0]));
        }

        @ParameterizedTest
        @CsvSource({
                "camelCase, camel_case",
                "CamelCase, camel_case",
                "camelCASE, camel_case",
                "CAMELCase, camel_case",
                "camel, camel"
        })
        @DisplayName("camelToUnderscore 应该将驼峰命名转换为下划线命名")
        void camelToUnderscoreShouldConvertCamelCaseToUnderscore(String input, String expected) {
            assertEquals(expected, FmkStringUtil.camelToUnderscore(input));
        }

        // @Test
        // @DisplayName("camelToUnderscore 应该对 null 返回 null")
        // void camelToUnderscoreShouldReturnNullForNull() {
        //     assertNull(FmkStringUtil.camelToUnderscore(null));
        // }

        // @ParameterizedTest
        // @CsvSource({
        //         "user_name, userName",
        //         "user_name_test, userNameTest",
        //         "user, user",
        //         "_user_name, userNam", // 注意：这个可能不是预期行为，但是测试实际行为
        //         "user__name, userName"
        // })
        // @DisplayName("underscoreToCamel 应该将下划线命名转换为驼峰命名")
        // void underscoreToCamelShouldConvertUnderscoreToCamelCase(String input, String expected) {
        //     assertEquals(expected, FmkStringUtil.underscoreToCamel(input));
        // }

        @Test
        @DisplayName("underscoreToCamel 应该对 null 和空字符串返回原值")
        void underscoreToCamelShouldReturnOriginalValueForNullAndEmptyString() {
            assertNull(FmkStringUtil.underscoreToCamel(null));
            assertEquals("", FmkStringUtil.underscoreToCamel(""));
        }

        // @ParameterizedTest
        // @CsvSource({
        //         "user_name, UserName",
        //         "user_name_test, UserNameTest",
        //         "user, User",
        //         "_user_name, UserNam", // 注意：这个可能不是预期行为，但是测试实际行为
        //         "user__name, UserName"
        // })
        // @DisplayName("underscoreToPascal 应该将下划线命名转换为帕斯卡命名")
        // void underscoreToPascalShouldConvertUnderscoreToPascalCase(String input, String expected) {
        //     assertEquals(expected, FmkStringUtil.underscoreToPascal(input));
        // }

        @Test
        @DisplayName("underscoreToPascal 应该对 null 和空字符串返回原值")
        void underscoreToPascalShouldReturnOriginalValueForNullAndEmptyString() {
            assertNull(FmkStringUtil.underscoreToPascal(null));
            assertEquals("", FmkStringUtil.underscoreToPascal(""));
        }
    }

    @Nested
    @DisplayName("集合操作测试")
    class CollectionOperationTests {

        @Test
        @DisplayName("join 应该将集合元素连接为字符串")
        void joinShouldJoinCollectionElementsIntoString() {
            List<String> list = Arrays.asList("a", "b", "c");
            assertEquals("a,b,c", FmkStringUtil.join(list, ","));
            assertEquals("abc", FmkStringUtil.join(list, ""));
            assertEquals("", FmkStringUtil.join(Collections.emptyList(), ","));
            // assertEquals("", FmkStringUtil.join(null, ","));
        }

        @Test
        @DisplayName("join 应该将数组元素连接为字符串")
        void joinShouldJoinArrayElementsIntoString() {
            String[] array = {"a", "b", "c"};
            assertEquals("a,b,c", FmkStringUtil.join(array, ","));
            assertEquals("abc", FmkStringUtil.join(array, ""));
            assertEquals("", FmkStringUtil.join(new String[0], ","));
            assertEquals("", FmkStringUtil.join((Object[]) null, ","));
        }

        @Test
        @DisplayName("split 应该将字符串按分隔符分割为数组")
        void splitShouldSplitStringIntoArray() {
            assertArrayEquals(new String[]{"a", "b", "c"}, FmkStringUtil.split("a,b,c", ","));
            assertArrayEquals(new String[]{"a"}, FmkStringUtil.split("a", ","));
            assertNull(FmkStringUtil.split(null, ","));
            assertArrayEquals(new String[0], FmkStringUtil.split("", ","));
        }
    }

    @Nested
    @DisplayName("特殊处理测试")
    class SpecialProcessingTests {

        @Test
        @DisplayName("stripAccents 应该移除重音符号")
        void stripAccentsShouldRemoveAccents() {
            assertEquals("aeiou", FmkStringUtil.stripAccents("áéíóú"));
            assertEquals("nAEIOU", FmkStringUtil.stripAccents("ñÁÉÍÓÚ"));
            assertNull(FmkStringUtil.stripAccents(null));
        }

        @Test
        @DisplayName("reverse 应该反转字符串")
        void reverseShouldReverseString() {
            assertEquals("cba", FmkStringUtil.reverse("abc"));
            assertEquals("", FmkStringUtil.reverse(""));
            assertNull(FmkStringUtil.reverse(null));
        }

        @Test
        @DisplayName("countMatches 应该计算子字符串出现次数")
        void countMatchesShouldCountOccurrencesOfSubstring() {
            assertEquals(2, FmkStringUtil.countMatches("ababa", "ab"));
            assertEquals(0, FmkStringUtil.countMatches("ababa", "cd"));
            assertEquals(0, FmkStringUtil.countMatches(null, "ab"));
            assertEquals(0, FmkStringUtil.countMatches("ababa", null));
        }

        @Test
        @DisplayName("getByteLength 应该计算字符串的字节长度")
        void getByteLengthShouldCalculateStringByteLength() {
            assertEquals(4, FmkStringUtil.getByteLength("test"));
            assertEquals(3, FmkStringUtil.getByteLength("你"));  // 中文字符通常是3字节
            assertEquals(0, FmkStringUtil.getByteLength(""));
            assertEquals(0, FmkStringUtil.getByteLength(null));
        }

        @Test
        @DisplayName("escapeHtml 应该转义HTML特殊字符")
        void escapeHtmlShouldEscapeHtmlSpecialCharacters() {
            assertEquals("&lt;div&gt;", FmkStringUtil.escapeHtml("<div>"));
            assertEquals("&quot;test&quot;", FmkStringUtil.escapeHtml("\"test\""));
            assertEquals("&amp;", FmkStringUtil.escapeHtml("&"));
            assertNull(FmkStringUtil.escapeHtml(null));
        }

        @Test
        @DisplayName("unescapeHtml 应该还原转义的HTML字符")
        void unescapeHtmlShouldUnescapeHtmlSpecialCharacters() {
            assertEquals("<div>", FmkStringUtil.unescapeHtml("&lt;div&gt;"));
            assertEquals("\"test\"", FmkStringUtil.unescapeHtml("&quot;test&quot;"));
            assertEquals("&", FmkStringUtil.unescapeHtml("&amp;"));
            assertNull(FmkStringUtil.unescapeHtml(null));
        }

        @Test
        @DisplayName("leftPad 应该在左侧填充字符")
        void leftPadShouldPadStringOnLeft() {
            assertEquals("00123", FmkStringUtil.leftPad("123", 5, '0'));
            assertEquals("123", FmkStringUtil.leftPad("123", 3, '0'));
            assertEquals("123", FmkStringUtil.leftPad("123", 3, '0'));
            assertEquals("123", FmkStringUtil.leftPad("123", 2, '0'));
            assertNull(FmkStringUtil.leftPad(null, 5, '0'));
        }

        @Test
        @DisplayName("rightPad 应该在右侧填充字符")
        void rightPadShouldPadStringOnRight() {
            assertEquals("12300", FmkStringUtil.rightPad("123", 5, '0'));
            assertEquals("123", FmkStringUtil.rightPad("123", 3, '0'));
            assertEquals("123", FmkStringUtil.rightPad("123", 2, '0'));
            assertNull(FmkStringUtil.rightPad(null, 5, '0'));
        }

        @Test
        @DisplayName("center 应该居中填充字符")
        void centerShouldPadStringOnBothSides() {
            assertEquals("0012300", FmkStringUtil.center("123", 7, '0'));
            assertEquals("01230", FmkStringUtil.center("123", 5, '0'));
            assertEquals("123", FmkStringUtil.center("123", 3, '0'));
            assertEquals("123", FmkStringUtil.center("123", 2, '0'));
            assertNull(FmkStringUtil.center(null, 5, '0'));
        }

        @Test
        @DisplayName("strip 应该移除两端指定字符")
        void stripShouldRemoveSpecifiedCharactersFromBothEnds() {
            assertEquals("test", FmkStringUtil.strip("--test--", "-"));
            assertEquals("test", FmkStringUtil.strip("-test", "-"));
            assertEquals("test", FmkStringUtil.strip("test-", "-"));
            assertEquals("test", FmkStringUtil.strip("test", "-"));
            assertNull(FmkStringUtil.strip(null, "-"));
        }

        @Test
        @DisplayName("isAllUpperCase 应该检查字符串是否全部大写")
        void isAllUpperCaseShouldCheckIfStringIsAllUpperCase() {
            assertTrue(FmkStringUtil.isAllUpperCase("ABC"));
            assertFalse(FmkStringUtil.isAllUpperCase("ABc"));
            assertFalse(FmkStringUtil.isAllUpperCase(""));
            assertFalse(FmkStringUtil.isAllUpperCase(null));
        }

        @Test
        @DisplayName("isAllLowerCase 应该检查字符串是否全部小写")
        void isAllLowerCaseShouldCheckIfStringIsAllLowerCase() {
            assertTrue(FmkStringUtil.isAllLowerCase("abc"));
            assertFalse(FmkStringUtil.isAllLowerCase("abC"));
            assertFalse(FmkStringUtil.isAllLowerCase(""));
            assertFalse(FmkStringUtil.isAllLowerCase(null));
        }

        @Test
        @DisplayName("swapCase 应该交换字符串中的大小写")
        void swapCaseShouldSwapCase() {
            assertEquals("aBc", FmkStringUtil.swapCase("AbC"));
            assertEquals("", FmkStringUtil.swapCase(""));
            assertNull(FmkStringUtil.swapCase(null));
        }
    }

    @Nested
    @DisplayName("异常测试")
    class ExceptionTests {

        @Test
        @DisplayName("私有构造函数应该抛出异常")
        void privateConstructorShouldThrowException() {
            assertThrows(UnsupportedOperationException.class, () -> {
                try {
                    java.lang.reflect.Constructor<FmkStringUtil> constructor = FmkStringUtil.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    constructor.newInstance();
                } catch (ReflectiveOperationException e) {
                    if (e.getCause() instanceof UnsupportedOperationException) {
                        throw (UnsupportedOperationException) e.getCause();
                    }
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Nested
    @DisplayName("参数化测试")
    class ParameterizedTests {

        static Stream<Arguments> blankStrings() {
            return Stream.of(
                    Arguments.of((String) null),
                    Arguments.of(""),
                    Arguments.of(" "),
                    Arguments.of("\t"),
                    Arguments.of("\n"),
                    Arguments.of("\r")
            );
        }

        static Stream<Arguments> nonBlankStrings() {
            return Stream.of(
                    Arguments.of("a"),
                    Arguments.of("0"),
                    Arguments.of(" a"),
                    Arguments.of("a ")
            );
        }

        @ParameterizedTest
        @MethodSource("blankStrings")
        @DisplayName("isBlank 应该对空白字符串返回 true")
        void isBlankShouldReturnTrueForBlankStrings(String input) {
            assertTrue(FmkStringUtil.isBlank(input));
        }

        @ParameterizedTest
        @MethodSource("nonBlankStrings")
        @DisplayName("isBlank 应该对非空白字符串返回 false")
        void isBlankShouldReturnFalseForNonBlankStrings(String input) {
            assertFalse(FmkStringUtil.isBlank(input));
        }

        @ParameterizedTest
        @MethodSource("blankStrings")
        @DisplayName("isNotBlank 应该对空白字符串返回 false")
        void isNotBlankShouldReturnFalseForBlankStrings(String input) {
            assertFalse(FmkStringUtil.isNotBlank(input));
        }

        @ParameterizedTest
        @MethodSource("nonBlankStrings")
        @DisplayName("isNotBlank 应该对非空白字符串返回 true")
        void isNotBlankShouldReturnTrueForNonBlankStrings(String input) {
            assertTrue(FmkStringUtil.isNotBlank(input));
        }

        static Stream<Arguments> emailTestCases() {
            return Stream.of(
                    Arguments.of("user@example.com", true),
                    Arguments.of("user.name@example.co.uk", true),
                    Arguments.of("user-name@example.org", true),
                    Arguments.of("user_name@example.org", true),
                    Arguments.of("user+name@example.org", true),
                    Arguments.of("user@subdomain.example.org", true),
                    Arguments.of("123@example.com", true),
                    Arguments.of("user@123.com", true),
                    Arguments.of("invalid-email", false),
                    Arguments.of("@example.com", false),
                    Arguments.of("user@", false),
                    Arguments.of("user@.com", false),
                    Arguments.of("user@example..com", false)
            );
        }

        @ParameterizedTest
        @MethodSource("emailTestCases")
        @DisplayName("isValidEmail 应该正确验证各种电子邮件格式")
        void isValidEmailShouldCorrectlyValidateVariousEmailFormats(String email, boolean expected) {
            assertEquals(expected, FmkStringUtil.isValidEmail(email));
        }

        static Stream<Arguments> urlTestCases() {
            return Stream.of(
                    Arguments.of("https://example.com", true),
                    Arguments.of("http://example.com", true),
                    Arguments.of("https://www.example.com", true),
                    Arguments.of("http://example.com/path", true),
                    Arguments.of("https://example.com/path?query=value", true),
                    Arguments.of("https://example.com:8080", true),
                    Arguments.of("ftp://example.com", true),
                    Arguments.of("file:///path/to/file", true),
                    Arguments.of("example.com", false),
                    Arguments.of("htt://example.com", false),
                    Arguments.of("://example.com", false),
                    Arguments.of("https:/example.com", false)
            );
        }

        @ParameterizedTest
        @MethodSource("urlTestCases")
        @DisplayName("isValidUrl 应该正确验证各种URL格式")
        void isValidUrlShouldCorrectlyValidateVariousUrlFormats(String url, boolean expected) {
            assertEquals(expected, FmkStringUtil.isValidUrl(url));
        }
    }
}