package com.iflow.util;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;


/**
 * 数据源
 *
 * 工作流数据源设计与业务库耦合在一起，增加便利性
 *
 * 需要业务库使用时注入业务库数据源： java.sql.DataSource
 *
 */
@Component
public class DataSourceAdapter {

    /**
     * 数据源
     */
    private DataSource dataSource;


    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
