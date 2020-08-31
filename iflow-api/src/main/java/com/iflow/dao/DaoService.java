package com.iflow.dao;

import com.iflow.json.Page;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DaoService {

    void setDataSource(DataSource ds);

    DataSource getDataSource();

    Map<String, Object> findUnique(String paramString, Object... paramVarArgs) throws SQLException;

    <T> T findUnique(Class<T> paramClass, String paramString, Object... paramVarArgs) throws Exception;

    List<Map<String, Object>> find(String paramString, Object... paramVarArgs) throws SQLException;

    <T> List<T> find(Class<T> paramClass, String paramString, Object... paramVarArgs) throws Exception;

    List<LinkedHashMap<String, Object>> findWithFieldSort(String paramString, Object... paramVarArgs) throws SQLException;

    <T> Page findByPage(Class<T> paramClass, Page paramPage, String paramString, Object... paramVarArgs) throws Exception;

    Page findByPage(Page paramPage, String paramString, Object... paramVarArgs) throws SQLException;

    long count(String paramString, Object... paramVarArgs) throws SQLException;

    int executeUpdate(String paramString, Object... paramVarArgs) throws SQLException;

    <T> T insertAndGetClass(Class<T> paramClass, String paramString1, String paramString2, Object... paramVarArgs) throws Exception;

    int[] batchUpdate(List<String> paramList) throws SQLException;

    void call(String paramString, Object... paramVarArgs) throws SQLException;

    int insert(Object paramObject) throws Exception;

    int update(Object paramObject) throws Exception;

    int saveOrUpdate(Object paramObject) throws Exception;

    String getDatabaseType();
}

