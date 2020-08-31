package com.iflow.json;

import java.io.Serializable;

public class EntitySql implements Serializable {

    private String sql;

    private Object[] params;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}
