package com.cjlabs.db.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用字符串类型处理器基类
 */
public abstract class StringTypeHandler<T> extends BaseTypeHandler<T> {
    
    private final Class<T> type;
    private final StringConverter<T> converter;
    
    public StringTypeHandler(Class<T> type, StringConverter<T> converter) {
        this.type = type;
        this.converter = converter;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : converter.fromString(value);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : converter.fromString(value);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : converter.fromString(value);
    }
    
    /**
     * 字符串转换器接口
     */
    public interface StringConverter<T> {
        T fromString(String value);
    }
}