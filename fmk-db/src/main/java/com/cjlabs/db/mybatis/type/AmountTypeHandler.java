// AmountTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.decimal.FmkAmount;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Amount类型处理器
 */
@MappedTypes(FmkAmount.class)
public class AmountTypeHandler extends BaseTypeHandler<FmkAmount> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FmkAmount parameter, JdbcType jdbcType) throws SQLException {
        ps.setBigDecimal(i, parameter.getValue());
    }

    @Override
    public FmkAmount getNullableResult(ResultSet rs, String columnName) throws SQLException {
        BigDecimal value = rs.getBigDecimal(columnName);
        return value == null ? null : FmkAmount.of(value);
    }

    @Override
    public FmkAmount getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal value = rs.getBigDecimal(columnIndex);
        return value == null ? null : FmkAmount.of(value);
    }

    @Override
    public FmkAmount getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        BigDecimal value = cs.getBigDecimal(columnIndex);
        return value == null ? null : FmkAmount.of(value);
    }
}