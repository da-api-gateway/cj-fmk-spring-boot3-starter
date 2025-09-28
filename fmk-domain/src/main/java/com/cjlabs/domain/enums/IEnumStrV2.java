package com.cjlabs.domain.enums;

import java.util.Optional;

public interface IEnumStrV2 {
    /**
     * 获取枚举的 code
     *
     * @return 枚举值
     */
    String getType();

    /**
     * 获取枚举的 code
     *
     * @return 枚举值
     */
    String getKey();

    /**
     * 获取枚举的描述信息
     *
     * @return 描述信息
     */
    String getMsgZh();

    /**
     * 获取枚举的描述信息
     *
     * @return 描述信息
     */
    String getMsgEn();

    /**
     * 根据 code 获取枚举对象
     *
     * @param type      枚举值
     * @param key       枚举值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 对应的枚举对象，若不存在返回 Optional.empty()
     */
    static <T extends Enum<T> & IEnumStrV2> Optional<T> getEnumByCode(String type, String key, Class<T> enumClass) {
        for (T flag : enumClass.getEnumConstants()) {
            if (flag.getType().equals(type) && flag.getKey().equals(key)) {
                return Optional.of(flag);
            }
        }
        return Optional.empty();
    }

}
