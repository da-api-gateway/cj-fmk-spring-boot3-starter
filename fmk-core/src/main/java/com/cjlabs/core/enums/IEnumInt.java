package com.cjlabs.core.enums;

import java.util.Optional;

public interface IEnumInt {

    /**
     * 获取枚举的 code
     *
     * @return 枚举值
     */
    int getCode();

    /**
     * 获取枚举的描述信息
     *
     * @return 描述信息
     */
    String getMsg();

    /**
     * 根据 code 获取枚举对象
     *
     * @param code      枚举值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 对应的枚举对象，若不存在返回 Optional.empty()
     */
    static <T extends Enum<T> & IEnumInt> Optional<T> getEnumByCode(int code, Class<T> enumClass) {
        for (T flag : enumClass.getEnumConstants()) {
            if (flag.getCode() == code) {
                return Optional.of(flag);
            }
        }
        return Optional.empty();
    }
}
