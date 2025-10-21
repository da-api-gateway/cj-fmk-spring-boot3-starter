package com.cjlabs.db;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis-Plus 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 🔥 添加动态表名拦截器（必须在分页前面）
        // interceptor.addInnerInterceptor(dynamicTableNameInterceptor());

        // 添加分页拦截器
        interceptor.addInnerInterceptor(paginationInnerInterceptor());

        log.info("MybatisPlusInterceptor配置完成：动态表名 + 分页插件");
        return interceptor;
    }

    /**
     * 分页插件配置
     */
    private PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);

        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        paginationInterceptor.setOverflow(false);

        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInterceptor.setMaxLimit(1000L);

        // 开启count的join优化，只针对部分left join
        paginationInterceptor.setOptimizeJoin(true);

        return paginationInterceptor;
    }


    // /**
    //  * 动态表名拦截器配置
    //  */
    // @Bean
    // public DynamicTableNameInnerInterceptor dynamicTableNameInterceptor() {
    //     // 🔥 正确的用法：直接传入TableNameHandler
    //     return new DynamicTableNameInnerInterceptor(createTableNameHandler());
    // }
    //
    // /**
    //  * 创建统一的表名处理器
    //  */
    // private TableNameHandler createTableNameHandler() {
    //     return (sql, tableName) -> {
    //         // 获取当前语言代码
    //         String languageCode = FmkContextUtil.getCurrentLanguageCode().getCode().toLowerCase();
    //
    //         // 🔥 处理需要动态表名的表
    //         switch (tableName.toLowerCase()) {
    //             case "sys_city":
    //                 String cityTableName = "sys_city_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, cityTableName);
    //                 return cityTableName;
    //
    //             case "sys_district":
    //                 String districtTableName = "sys_district_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, districtTableName);
    //                 return districtTableName;
    //
    //             case "sys_community":
    //                 String communityTableName = "sys_community_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, communityTableName);
    //                 return communityTableName;
    //
    //             case "building":
    //                 String buildingTableName = "building_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, buildingTableName);
    //                 return buildingTableName;
    //
    //             // 🔥 可以继续添加其他需要多语言的表
    //             case "project_info":
    //                 String projectTableName = "project_info_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, projectTableName);
    //                 return projectTableName;
    //
    //             // 🔥 可以继续添加其他需要多语言的表
    //             case "house_info":
    //                 String houseTableName = "house_info_" + languageCode;
    //                 log.info("动态表名替换: {} -> {}", tableName, houseTableName);
    //                 return houseTableName;
    //
    //             default:
    //                 // 不需要动态替换的表，直接返回原表名
    //                 return tableName;
    //         }
    //     };
    // }
}