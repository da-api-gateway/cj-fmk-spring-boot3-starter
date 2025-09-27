package com.cjlabs.db.mybatis.handler;

import com.cjlabs.core.types.base.BaseLongType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用Long类型处理器基类
 */
public abstract class LongTypeHandler<T extends BaseLongType<T>> extends BaseTypeHandler<T> {
    
    private final LongConverter<T> converter;
    
    public LongTypeHandler(LongConverter<T> converter) {
        this.converter = converter;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getValue());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : converter.fromLong(value);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long value = rs.getLong(columnIndex);
        return rs.wasNull() ? null : converter.fromLong(value);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long value = cs.getLong(columnIndex);
        return cs.wasNull() ? null : converter.fromLong(value);
    }
    
    /**
     * Long转换器接口
     */
    public interface LongConverter<T> {
        T fromLong(Long value);
    }
}