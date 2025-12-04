package com.cjlabs.web.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class FmkJacksonUtil {
    private FmkJacksonUtil() {
        // 私有构造函数，防止实例化
    }

    // 使用AtomicReference来支持线程安全的ObjectMapper替换
    private static final AtomicReference<ObjectMapper> MAPPER_REF = new AtomicReference<>();

    static {
        // 初始化默认ObjectMapper
        MAPPER_REF.set(createDefaultMapper());
    }

    /**
     * 创建默认配置的ObjectMapper
     */
    public static ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // ============== 模块注册 ==============
        // 支持参数名称
        mapper.registerModule(new ParameterNamesModule());
        // 支持Java8时间API
        mapper.registerModule(new JavaTimeModule());

        // ============== 序列化配置 ==============

        // FAIL_ON_EMPTY_BEANS: 序列化空对象时是否抛出异常
        // 默认true会抛异常，设为false允许序列化空对象为{}
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // WRAP_ROOT_VALUE: 是否包装根值
        // true: {"RootBean":{"value":"test"}}
        // false: {"value":"test"}
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // INDENT_OUTPUT: 是否格式化输出
        // true: 美化输出（多行，缩进）
        // false: 紧凑输出（单行）
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        // FAIL_ON_SELF_REFERENCES: 遇到自引用时是否抛异常
        // true: 抛异常
        // false: 忽略自引用，输出时跳过
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        // CLOSE_CLOSEABLE: 序列化后是否关闭Closeable对象
        // true: 自动关闭
        // false: 不关闭
        mapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
        // FLUSH_AFTER_WRITE_VALUE: 写入后是否刷新输出流
        // true: 立即刷新
        // false: 不刷新
        mapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, false);
        // WRITE_DATES_AS_TIMESTAMPS: 日期是否序列化为时间戳
        // true: 序列化为时间戳数字
        // false: 序列化为ISO字符串格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // WRITE_DATE_KEYS_AS_TIMESTAMPS: Map的日期键是否序列化为时间戳
        // true: 日期键序列化为时间戳
        // false: 日期键序列化为字符串
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // WRITE_DATES_WITH_ZONE_ID: 序列化日期时是否包含时区ID
        // true: 包含时区ID，如 "2023-01-01T12:00:00+08:00[Asia/Shanghai]"
        // false: 不包含时区ID，如 "2023-01-01T12:00:00+08:00"
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        // WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS: 字符数组是否序列化为JSON数组
        // true: ['a','b','c']
        // false: "abc"
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
        // WRITE_DURATIONS_AS_TIMESTAMPS: Duration是否序列化为时间戳
        // true: 序列化为数字（秒）
        // false: 序列化为ISO字符串，如"PT2H"
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        // WRITE_ENUMS_USING_INDEX: 枚举是否使用索引序列化
        // true: 使用索引值（0,1,2...）
        // false: 使用枚举名称
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        // WRITE_ENUM_KEYS_USING_INDEX: Map的枚举键是否使用索引
        // true: 使用索引
        // false: 使用枚举名称
        mapper.configure(SerializationFeature.WRITE_ENUM_KEYS_USING_INDEX, false);
        // WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED: 单元素数组是否解包
        // true: [1] -> 1
        // false: [1] -> [1]
        mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);

        // ORDER_MAP_ENTRIES_BY_KEYS: Map是否按键排序
        // true: 按键排序
        // false: 保持原始顺序
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        // WRITE_NULL_MAP_VALUES: 是否序列化Map中的null值
        // true: 包含null值
        // false: 忽略null值
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // WRITE_EMPTY_JSON_ARRAYS: 是否序列化空数组
        // true: 序列化空数组为[]
        // false: 忽略空数组
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);

        // WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS: 日期时间戳是否使用纳秒
        // true: 使用纳秒精度
        // false: 使用毫秒精度
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);


        // ============== 反序列化配置 ==============

        // USE_BIG_DECIMAL_FOR_FLOATS: 浮点数是否使用BigDecimal
        // true: 使用BigDecimal，保持精度
        // false: 使用Double，可能丢失精度
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

        // USE_BIG_INTEGER_FOR_INTS: 整数是否使用BigInteger
        // true: 使用BigInteger
        // false: 使用Integer/Long
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);

        // USE_LONG_FOR_INTS: 整数是否使用Long
        // true: 使用Long
        // false: 使用Integer
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, false);

        // USE_JAVA_ARRAY_FOR_JSON_ARRAY: JSON数组是否使用Java数组
        // true: 使用Java数组
        // false: 使用ArrayList
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);

        // FAIL_ON_UNKNOWN_PROPERTIES: 遇到未知属性是否抛异常
        // true: 抛异常
        // false: 忽略未知属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // FAIL_ON_NULL_FOR_PRIMITIVES: 基本类型遇到null是否抛异常
        // true: 抛异常
        // false: 使用默认值（0, false等）
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        // FAIL_ON_NUMBERS_FOR_ENUMS: 枚举接收数字是否抛异常
        // true: 抛异常
        // false: 允许使用数字（索引）
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

        // FAIL_ON_INVALID_SUBTYPE: 无效子类型是否抛异常
        // true: 抛异常
        // false: 使用默认处理
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        // ACCEPT_SINGLE_VALUE_AS_ARRAY: 是否接受单值作为数组
        // true: 123 -> [123]
        // false: 不接受
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);

        // UNWRAP_SINGLE_VALUE_ARRAYS: 是否解包单值数组
        // true: [123] -> 123
        // false: 不解包
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, false);

        // UNWRAP_ROOT_VALUE: 是否解包根值
        // true: 解包根值
        // false: 不解包
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);

        // ACCEPT_EMPTY_STRING_AS_NULL_OBJECT: 空字符串是否视为null
        // true: "" -> null
        // false: "" -> ""
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        // ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT: 空数组是否视为null
        // true: [] -> null
        // false: [] -> []
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);

        // ACCEPT_FLOAT_AS_INT: 是否接受浮点数作为整数
        // true: 123.0 -> 123
        // false: 不接受
        mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);

        // READ_ENUMS_USING_TO_STRING: 是否使用toString读取枚举
        // true: 使用toString()方法
        // false: 使用name()方法
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false);

        // FAIL_ON_READING_DUP_TREE_KEY: 重复键是否抛异常
        // true: 抛异常
        // false: 使用最后一个值
        mapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);

        // FAIL_ON_IGNORED_PROPERTIES: 忽略属性是否抛异常
        // true: 抛异常
        // false: 忽略
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        // FAIL_ON_UNRESOLVED_OBJECT_IDS: 未解析对象ID是否抛异常
        // true: 抛异常
        // false: 忽略
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

        // FAIL_ON_MISSING_CREATOR_PROPERTIES: 缺少创建者属性是否抛异常
        // true: 抛异常
        // false: 使用默认值
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);

        // FAIL_ON_NULL_CREATOR_PROPERTIES: 创建者属性为null是否抛异常
        // true: 抛异常
        // false: 允许null
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

        // FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY: 缺少外部类型ID属性是否抛异常
        // true: 抛异常
        // false: 使用默认处理
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);

        // FAIL_ON_TRAILING_TOKENS: 尾随标记是否抛异常
        // true: 抛异常
        // false: 忽略
        mapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);

        // WRAP_EXCEPTIONS: 是否包装异常
        // true: 包装为JsonMappingException
        // false: 抛出原始异常
        mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);

        // READ_UNKNOWN_ENUM_VALUES_AS_NULL: 未知枚举值是否读取为null
        // true: 未知枚举值 -> null
        // false: 抛异常
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        // READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE: 未知枚举值是否使用默认值
        // true: 使用默认值
        // false: 抛异常
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, false);

        // READ_DATE_TIMESTAMPS_AS_NANOSECONDS: 日期时间戳是否按纳秒读取
        // true: 按纳秒读取
        // false: 按毫秒读取
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        // ADJUST_DATES_TO_CONTEXT_TIME_ZONE: 是否调整日期到上下文时区
        // true: 调整到上下文时区
        // false: 保持原始时区
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

        // EAGER_DESERIALIZER_FETCH: 是否提前获取反序列化器
        // true: 提前获取（性能优化）
        // false: 延迟获取
        mapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, false);

        // ============== JsonGenerator配置 ==============

        // WRITE_BIGDECIMAL_AS_PLAIN: BigDecimal是否按普通格式写入
        // true: 不使用科学计数法
        // false: 可能使用科学计数法
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

        // ============== 其他配置 ==============

        // 设置序列化包含策略：不包含null值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 设置默认时区
        mapper.setTimeZone(TimeZone.getDefault());

        return mapper;
    }

    /**
     * 获取当前ObjectMapper实例
     */
    public static ObjectMapper getMapper() {
        return MAPPER_REF.get();
    }

    /**
     * 替换ObjectMapper实例（线程安全）
     *
     * @param newMapper 新的ObjectMapper实例
     */
    public static void setMapper(ObjectMapper newMapper) {
        if (newMapper == null) {
            throw new IllegalArgumentException("ObjectMapper不能为null");
        }
        MAPPER_REF.set(newMapper);
        log.info("JacksonUtil|setMapper|ObjectMapper已替换");
    }

    /**
     * 重置为默认ObjectMapper
     */
    public static void resetToDefault() {
        MAPPER_REF.set(createDefaultMapper());
        log.info("JacksonUtil|resetToDefault|ObjectMapper已重置为默认配置");
    }

    // ================ 基础序列化方法 ================

    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object obj) {
        if (Objects.isNull(obj)) {
            log.warn("JacksonUtil|toJson|对象为null");
            return null;
        }
        try {
            return getMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JacksonUtil|toJson|序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转JSON字符串（美化输出）
     */
    public static String toJsonPretty(Object obj) {
        if (Objects.isNull(obj)) {
            log.warn("JacksonUtil|toJsonPretty|对象为null");
            return null;
        }
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JacksonUtil|toJsonPretty|序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }

    // ================ 基础反序列化方法 ================

    /**
     * JSON字符串转对象
     */
    public static <T> T parseObj(String json, Class<T> clazz) {
        if (Objects.isNull(clazz) || StringUtils.isBlank(json)) {
            log.warn("JacksonUtil|parseObj|参数为空");
            return null;
        }
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            log.error("JacksonUtil|parseObj|反序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * JSON字符串转对象（使用TypeReference）
     */
    public static <T> T parseObj(String json, TypeReference<T> typeRef) {
        if (Objects.isNull(typeRef) || StringUtils.isBlank(json)) {
            log.warn("JacksonUtil|parseObj|参数为空");
            return null;
        }
        try {
            return getMapper().readValue(json, typeRef);
        } catch (IOException e) {
            log.error("JacksonUtil|parseObj|反序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }

    // ================ 集合类型方法 ================

    /**
     * JSON字符串转List
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        return parseObj(json, new TypeReference<List<T>>() {
        });
    }

    /**
     * JSON字符串转Map
     */
    public static Map<String, Object> parseMap(String json) {
        return parseObj(json, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * JSON字符串转Map（指定值类型）
     */
    /**
     * JSON字符串转Map（指定值类型）
     */
    public static <T> Map<String, T> parseMap(String json, Class<T> valueType) {
        if (Objects.isNull(valueType) || StringUtils.isBlank(json)) {
            log.warn("JacksonUtil|parseMap|参数为空");
            return Maps.newHashMap();
        }
        try {
            var typeFactory = getMapper().getTypeFactory();
            var mapType = typeFactory.constructMapType(Map.class, String.class, valueType);
            return getMapper().readValue(json, mapType);
        } catch (IOException e) {
            log.error("JacksonUtil|parseMap|反序列化失败: {}", e.getMessage(), e);
            return Maps.newHashMap();
        }
    }

    // ================ JsonNode相关方法 ================

    /**
     * 创建空的ObjectNode
     *
     * @deprecated 因为 jsonNode 不能操作，所以谨慎使用，建议使用 createObjectNode 代替
     */
    @Deprecated
    public static JsonNode createJsonNode() {
        return getMapper().createObjectNode();
    }

    /**
     * 创建空的ArrayNode
     */
    public static ObjectNode createObjectNode() {
        return getMapper().createObjectNode();
    }

    /**
     * 创建空的ArrayNode
     */
    public static ArrayNode createArrayNode() {
        return getMapper().createArrayNode();
    }

    /**
     * 字符串转JsonNode
     */
    public static JsonNode parseJsonNode(String json) {
        if (StringUtils.isBlank(json)) {
            log.warn("JacksonUtil|parseJsonNode|JSON字符串为空");
            return null;
        }
        try {
            return getMapper().readTree(json);
        } catch (IOException e) {
            log.error("JacksonUtil|parseJsonNode|解析失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转JsonNode
     */
    public static JsonNode objToJsonNode(Object obj) {
        if (Objects.isNull(obj)) {
            log.warn("JacksonUtil|objToJsonNode|对象为null");
            return null;
        }
        return getMapper().valueToTree(obj);
    }

    /**
     * JsonNode转对象
     */
    public static <T> T jsonNodeToObj(JsonNode node, Class<T> clazz) {
        if (Objects.isNull(node) || Objects.isNull(clazz)) {
            log.warn("JacksonUtil|jsonNodeToObj|参数为空");
            return null;
        }
        try {
            return getMapper().treeToValue(node, clazz);
        } catch (Exception e) {
            log.error("JacksonUtil|jsonNodeToObj|转换失败: {}", e.getMessage(), e);
            return null;
        }
    }


    /**
     * JsonNode转对象（支持TypeReference）
     */
    public static <T> T jsonNodeToObj(JsonNode node, TypeReference<T> typeRef) {
        if (Objects.isNull(node) || Objects.isNull(typeRef)) {
            log.warn("JacksonUtil|jsonNodeToObj|参数为空");
            return null;
        }
        try {
            return getMapper().convertValue(node, typeRef);
        } catch (Exception e) {
            log.error("JacksonUtil|jsonNodeToObj|转换失败: {}", e.getMessage(), e);
            return null;
        }
    }

    // ================ 工具方法 ================

    /**
     * 判断字符串是否为有效的JSON
     */
    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            getMapper().readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 深度克隆对象
     */
    public static <T> T deepClone(T obj, Class<T> clazz) {
        if (Objects.isNull(obj)) {
            return null;
        }
        String json = toJson(obj);
        return parseObj(json, clazz);
    }

    /**
     * 对象转换（通过JSON序列化/反序列化）
     */
    public static <T> T convertValue(Object obj, Class<T> clazz) {
        if (Objects.isNull(obj) || Objects.isNull(clazz)) {
            return null;
        }
        try {
            return getMapper().convertValue(obj, clazz);
        } catch (Exception e) {
            log.error("JacksonUtil|convertValue|转换失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转换（使用TypeReference）
     */
    public static <T> T convertValue(Object obj, TypeReference<T> typeRef) {
        if (Objects.isNull(obj) || Objects.isNull(typeRef)) {
            return null;
        }
        try {
            return getMapper().convertValue(obj, typeRef);
        } catch (Exception e) {
            log.error("JacksonUtil|convertValue|转换失败: {}", e.getMessage(), e);
            return null;
        }
    }
}
