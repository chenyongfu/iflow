package com.iflow.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflow.dao.DaoService;
import com.iflow.dao.ObjectSQLParser;
import com.iflow.json.EntitySql;
import com.iflow.json.Page;
import com.iflow.util.DataSourceAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * dao工具
 */
@Component("iflowDao")
public class DaoServiceImpl implements DaoService {

    private static Logger logger = LoggerFactory.getLogger(DaoServiceImpl.class);

    private DataSource dataSource;

    @Autowired
    private DataSourceAdapter dataSourceAdapter;




    public Map<String, Object> findUnique(String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("findUnique sql=" + sql);
        logger.debug("findUnique params=" + JSON.toJSONString(params));
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String name = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(name);
                    map.put(name, value);
                }
                return map;
            }
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return null;
    }

    public <T> T findUnique(Class<T> clazz, String sql, Object... params) throws Exception {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("findUnique sql=" + sql);
        logger.debug("findUnique params=" + JSON.toJSONString(params));
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            List<T> dataList = populate(resultSet, clazz);
            if (dataList != null && !dataList.isEmpty())
                return dataList.get(0);
            return null;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
    }

    public List<Map<String, Object>> find(String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("find sql=" + sql);
        logger.debug("find params=" + JSON.toJSONString(params));
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> map = null;
            while (resultSet.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String name = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(name);
                    map.put(name, value);
                }
                resultList.add(map);
            }
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return resultList;
    }

    public <T> List<T> find(Class<T> clazz, String sql, Object... params) throws Exception {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("find sql=" + sql);
        logger.debug("find params=" + JSON.toJSONString(params));
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            List<T> dataList = populate(resultSet, clazz);
            return dataList;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
    }

    public List<LinkedHashMap<String, Object>> findWithFieldSort(String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("findWithFieldSort sql=" + sql);
        logger.debug("findWithFieldSort params=" + JSON.toJSONString(params));
        List<LinkedHashMap<String, Object>> resultList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            LinkedHashMap<String, Object> map = null;
            while (resultSet.next()) {
                map = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String name = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(name);
                    map.put(name, value);
                }
                resultList.add(map);
            }
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return resultList;
    }

    public <T> Page findByPage(Class<T> clazz, Page page, String sql, Object... params) throws Exception {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("findByPage sql=" + sql);
        logger.debug("findByPage params=" + JSON.toJSONString(params));
        if (page == null)
            page = new Page();
        String countSql = "select count(*) from (" + sql + ") t1";
        long count = count(countSql, params);
        page.setTotal(Long.valueOf(count));
        if (count == 0L)
            return page;
        int limit = page.getPageSize().intValue();
        int offset = (page.getPageNow().intValue() - 1) * page.getPageSize().intValue();
        String pageSql = "select * from (" + sql + ") t2 limit " + limit + " offset " + offset;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(pageSql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            List<T> dataList = populate(resultSet, clazz);
            page.setRows(dataList);
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return page;
    }

    public Page findByPage(Page page, String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("findByPage sql=" + sql);
        logger.debug("findByPage params=" + JSON.toJSONString(params));
        if (page == null)
            page = new Page();
        String countSql = "select count(*) from (" + sql + ") t1";
        long count = count(countSql, params);
        page.setTotal(Long.valueOf(count));
        if (count == 0L)
            return page;
        int limit = page.getPageSize().intValue();
        int offset = (page.getPageNow().intValue() - 1) * page.getPageSize().intValue();
        String pageSql = "select * from (" + sql + ") t2 limit " + limit + " offset " + offset;
        List<Map<String, Object>> resultList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(pageSql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            LinkedHashMap<String, Object> map = null;
            while (resultSet.next()) {
                map = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String name = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(name);
                    map.put(name, value);
                }
                resultList.add(map);
            }
            page.setRows(resultList);
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return page;
    }

    public long count(String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("count sql=" + sql);
        logger.debug("count params=" + JSON.toJSONString(params));
        String checkSql = sql.toLowerCase();
        if (!checkSql.contains("count"))
            throw new SQLException("sql must contains count");
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("executeUpdate sql=" + sql);
        logger.debug("executeUpdate params=" + JSON.toJSONString(params));
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = this.getDataSource().getConnection();
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            int result = ps.executeUpdate();
            return result;
        } finally {
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
    }

    public <T> T insertAndGetClass(Class<T> clazz, String sql, String tablename, Object... params) throws Exception {
        if (StringUtils.isBlank(sql))
            throw new SQLException("sql can not be null");
        logger.debug("executeUpdate sql=" + sql);
        logger.debug("executeUpdate params=" + JSON.toJSONString(params));
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = this.getDataSource().getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            setParameter(ps, params);
            ps.executeUpdate();
            String sql1 = "";
            if ("public.message_record".equals(tablename)) {
                sql1 = "SELECT * from  " + tablename + " where msgid=(select max(msgid) from " + tablename + ")";
            } else {
                sql1 = "SELECT * from  " + tablename + " where id=(select max(id) from " + tablename + ")";
            }
            ps = connection.prepareStatement(sql1);
            resultSet = ps.executeQuery();
            connection.commit();
            List<T> dataList = populate(resultSet, clazz);
            if (dataList != null && !dataList.isEmpty())
                return dataList.get(0);
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            logger.error("executeUpdate failed", e);
            throw e;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (resultSet != null)
                resultSet.close();
            if (ps != null)
                ps.close();
            if (connection != null)
                connection.close();
        }
        return null;
    }

    public int[] batchUpdate(List<String> withoutParamsSqlList) throws SQLException {
        if (withoutParamsSqlList == null || withoutParamsSqlList.isEmpty())
            return null;
        int defaultFlushSize = 1000;
        List<Integer> resultList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            for (int i = 0; i < withoutParamsSqlList.size(); i++) {
                statement.addBatch(withoutParamsSqlList.get(i));
                if ((i + 1) % defaultFlushSize == 0)
                    try {
                        int[] arrayOfInt = statement.executeBatch();
                        for (int j = 0; j < arrayOfInt.length; j++)
                            resultList.add(Integer.valueOf(arrayOfInt[j]));
                        connection.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            int[] result = statement.executeBatch();
            for (int k = 0; k < result.length; k++)
                resultList.add(Integer.valueOf(result[k]));
            connection.commit();
            int[] arr1 = resultList.stream().mapToInt(Integer::valueOf).toArray();
            return arr1;
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public void call(String funcName, Object... params) throws SQLException {
        if (StringUtils.isBlank(funcName))
            throw new SQLException("procedure or function name can not be null");
        logger.debug("call funcName=" + funcName);
        logger.debug("call params=" + JSON.toJSONString(params));
        Connection connection = null;
        CallableStatement stmt = null;
        try {
            StringBuilder finalSql = new StringBuilder("{call " + funcName + "(");
            if (params != null)
                for (int i = 0; i < params.length; i++) {
                    if (i == params.length - 1) {
                        finalSql.append("?");
                    } else {
                        finalSql.append("?,");
                    }
                }
            finalSql.append(")}");
            connection = this.getDataSource().getConnection();
            stmt = connection.prepareCall(finalSql.toString());
            setParameter(stmt, params);
            stmt.execute();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
    }

    public int insert(Object entity) throws Exception {
        ObjectSQLParser parser = new ObjectSQLParser(entity);
        EntitySql entitySql = parser.parse4Insert();
        if (entitySql == null)
            return 0;
        String sql = entitySql.getSql();
        Object[] params = entitySql.getParams();
        logger.debug("execute: " + sql);
        logger.debug("params: " + JSON.toJSONString(params));
        int count = executeUpdate(sql, params);
        return count;
    }

    public int update(Object entity) throws Exception {
        ObjectSQLParser parser = new ObjectSQLParser(entity);
        EntitySql entitySql = parser.parse4Update();
        if (entitySql == null)
            return 0;
        String sql = entitySql.getSql();
        Object[] params = entitySql.getParams();
        logger.debug("execute: " + sql);
        logger.debug("params: " + JSON.toJSONString(params));
        int count = executeUpdate(sql, params);
        return count;
    }

    public int saveOrUpdate(Object entity) throws Exception {
        ObjectSQLParser parser = new ObjectSQLParser(entity);
        EntitySql entitySql = parser.parse4Count();
        if (entitySql == null)
            return insert(entity);
        String sql = entitySql.getSql();
        Object[] params = entitySql.getParams();
        logger.debug("execute: " + sql);
        logger.debug("params: " + JSON.toJSONString(params));
        long count = count(sql, params);
        if (count > 0L)
            return update(entity);
        return insert(entity);
    }

    public String getDatabaseType() {
        Connection conn = null;
        try {
            conn = this.getDataSource().getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            String driver = meta.getDatabaseProductName().toLowerCase();
            String type = "mysql";
            if (driver.contains("mysql")) {
                type = "mysql";
            } else if (driver.contains("postgres")) {
                type = "postgres";
            } else if (driver.contains("microsoft") || driver.contains("sql server")) {
                type = "sqlserver";
            } else if (driver.contains("oracle")) {
                type = "oracle";
            }
            return type;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }
        return null;
    }

    private void setParameter(PreparedStatement st, Object[] params) throws SQLException {
        if (st == null)
            return;
        if (params != null && params.length > 0)
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    if (params[i] instanceof Date) {
                        Date date = (Date)params[i];
                        st.setTimestamp(i + 1, new Timestamp(date.getTime()));
                    } else {
                        st.setObject(i + 1, params[i]);
                    }
                } else {
                    st.setNull(i + 1, 0);
                }
            }
    }

    public <T> List<T> populate(ResultSet rs, Class<T> clazz) throws Exception {
        ResultSetMetaData rsm = rs.getMetaData();
        int colNumber = rsm.getColumnCount();
        List<T> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        while (rs.next()) {
            JSONObject json = new JSONObject();
            for (int i = 1; i <= colNumber; i++) {
                String label = rsm.getColumnLabel(i);
                Object value = rs.getObject(i);
                Field field = null;
                for (int j = 0; j < fields.length; j++) {
                    Field f = fields[j];
                    if (f.getName().equalsIgnoreCase(label)) {
                        field = f;
                        break;
                    }
                }
                value = parseSpecialValue(field, value);
                json.put(label, value);
            }
            T obj = (T)JSON.parseObject(json.toJSONString(), clazz);
            list.add(obj);
        }
        return list;
    }

    private Object parseSpecialValue(Field field, Object value) throws Exception {
        if (field == null || value == null)
            return value;
        if (field.getType().getClass().isArray() || field.getType() == List.class) {
            String str = value.toString();
            if (str != null && str.startsWith("["))
                value = JSONArray.parseArray(str);
        } else if (field.getType() == Map.class) {
            String str = value.toString();
            if (str != null && str.startsWith("{")) {
                JSONObject json = JSON.parseObject(str);
                value = json;
            }
        }
        return value;
    }




    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        if(this.dataSource == null){
            this.dataSource = dataSourceAdapter.getDataSource();
        }
        return this.dataSource;
    }


}
