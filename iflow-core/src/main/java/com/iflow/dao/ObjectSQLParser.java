package com.iflow.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.iflow.json.EntitySql;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectSQLParser {

    private static Logger logger = LoggerFactory.getLogger(ObjectSQLParser.class);

    private static final String IGNORE_PATTERN = "__";

    private Object entity;

    private String tableName;

    private List<Field> idFields = new ArrayList<>();

    private List<Field> allFields = new ArrayList<>();

    public ObjectSQLParser(Object entity) {
        this.entity = entity;
        Table table = entity.getClass().<Table>getAnnotation(Table.class);
        if (table == null) {
            this.tableName = parseTableNameByClass(entity.getClass());
        } else {
            this.tableName = table.name();
        }
        String objName = "java.lang.Object";
        Class<?> model = entity.getClass();
        while (model != null && !objName.equals(model.getName())) {
            this.allFields.addAll(Arrays.asList(model.getDeclaredFields()));
            model = model.getSuperclass();
        }
        for (Field field : this.allFields) {
            field.setAccessible(true);
            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null)
                this.idFields.add(field);
        }
    }

    public EntitySql parse4Count() throws Exception {
        if (this.idFields.isEmpty())
            return null;
        StringBuilder sql = new StringBuilder("select count(*) from " + this.tableName);
        LinkedList<Object> params = new LinkedList();
        sql.append(" where 1=1 ");
        for (Field field : this.idFields) {
            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            Object value = field.get(this.entity);
            if (value == null) {
                sql.append(" and " + name + " is null");
                continue;
            }
            String safeStr = getSafeValue4Sql(field);
            sql.append(" and " + name + " = " + safeStr);
        }
        EntitySql es = new EntitySql();
        es.setSql(sql.toString());
        es.setParams(params.toArray());
        return es;
    }

    public EntitySql parse4Insert() throws Exception {
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(this.tableName + "(");
        StringBuilder values = new StringBuilder();
        boolean isEmpty = true;
        for (Field field : this.allFields) {
            Object value = field.get(this.entity);

            if (Modifier.isFinal(field.getModifiers()) || field.getName().startsWith("__"))
                continue;
            field.setAccessible(true);
            Transient ignoreAnno = field.<Transient>getAnnotation(Transient.class);
            if (ignoreAnno != null)
                continue;

            // 如果有自动生成UUID，这里生成
            GeneratedValue genValueAnno = field.getAnnotation(GeneratedValue.class);
            if((value == null || StringUtils.isBlank(value.toString())
                    && genValueAnno != null
                    && genValueAnno.generator().equalsIgnoreCase("system-uuid"))){
                String uuid = UUID.randomUUID().toString().replace("-", "");

                // 设置该user1的name属性
                field.setAccessible(true);
                field.set(this.entity, uuid);
                value = uuid;
            }

            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            if (value != null) {
                isEmpty = false;
                sql.append(name + ",");
                String safeStr = getSafeValue4Sql(field);
                values.append(safeStr + ",");
            }
        }
        if (isEmpty) {
            logger.error("empty Object is not allowed " + this.entity.toString());
            throw new Exception("empty Object is not allowed " + this.entity.toString());
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ") values(" + values.substring(0, values.length() - 1) + ")");
        EntitySql es = new EntitySql();
        es.setSql(sql.toString());
        return es;
    }

    public String parse4InsertSql() throws Exception {
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(this.tableName + "(");
        StringBuilder values = new StringBuilder();
        boolean isEmpty = true;
        for (Field field : this.allFields) {
            Object value = field.get(this.entity);

            if (Modifier.isFinal(field.getModifiers()) || field.getName().startsWith("__"))
                continue;
            field.setAccessible(true);
            Transient ignoreAnno = field.<Transient>getAnnotation(Transient.class);
            if (ignoreAnno != null)
                continue;

            // 如果有自动生成UUID，这里生成
            GeneratedValue genValueAnno = field.getAnnotation(GeneratedValue.class);
            if((value == null || StringUtils.isBlank(value.toString())
                    && genValueAnno != null
                    && genValueAnno.generator().equalsIgnoreCase("system-uuid"))){
                String uuid = UUID.randomUUID().toString().replace("-", "");

                // 设置该user1的name属性
                field.setAccessible(true);
                field.set(this.entity, uuid);
                value = uuid;
            }

            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();

            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null && (value == null || StringUtils.isBlank(value.toString())))
                throw new Exception("" + JSON.toJSONString(this.entity));
            if (value != null) {
                isEmpty = false;
                sql.append(name + ",");
                String safeStr = getSafeValue4Sql(field);
                values.append(safeStr + ",");
            }
        }
        if (isEmpty) {
            logger.error("empty Object is not allowed " + this.entity.toString());
            throw new Exception("empty Object is not allowed " + this.entity.toString());
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")");
        return sql.toString();
    }

    public EntitySql parse4Update() throws Exception {
        if (this.idFields.isEmpty()) {
            logger.error("update operate @Id can not not null");
            throw new Exception("update operate @Id can not not null");
        }
        StringBuilder sql = new StringBuilder("update ");
        sql.append(this.tableName + " set ");
        boolean isEmpty = true;
        for (Field field : this.allFields) {
            field.setAccessible(true);
            Transient ignoreAnno = field.<Transient>getAnnotation(Transient.class);
            if (ignoreAnno != null)
                continue;
            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null)
                continue;
            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            Object value = field.get(this.entity);
            if (value != null) {
                isEmpty = false;
                String safeStr = getSafeValue4Sql(field);
                sql.append(name + " = " + safeStr + ",");
            }
        }
        if (isEmpty) {
            logger.error("empty Object is not allowed " + this.entity.toString());
            throw new Exception("empty Object is not allowed " + this.entity.toString());
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + " where 1=1 ");
        for (Field field : this.idFields) {
            Object value = field.get(this.entity);
            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null && (value == null || StringUtils.isBlank(value.toString())))
                throw new Exception("" + JSON.toJSONString(this.entity));
            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            String safeStr = getSafeValue4Sql(field);
            sql.append(" and " + name + " = " + safeStr);
        }
        EntitySql es = new EntitySql();
        es.setSql(sql.toString());
        return es;
    }

    public String parse4UpdateSql() throws Exception {
        if (this.idFields.isEmpty()) {
            logger.error("update operate @Id can not not null");
            throw new Exception("update operate @Id can not not null");
        }
        StringBuilder sql = new StringBuilder("update ");
        sql.append(this.tableName + " set ");
        boolean isEmpty = true;
        for (Field field : this.allFields) {
            field.setAccessible(true);
            Transient ignoreAnno = field.<Transient>getAnnotation(Transient.class);
            if (ignoreAnno != null)
                continue;
            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null)
                continue;
            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            Object value = field.get(this.entity);
            if (value != null) {
                isEmpty = false;
                String safeStr = getSafeValue4Sql(field);
                sql.append(name + " = " + safeStr + ",");
            }
        }
        if (isEmpty) {
            logger.error("empty Object is not allowed " + this.entity.toString());
            throw new Exception("empty Object is not allowed " + this.entity.toString());
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + " where 1=1 ");
        for (Field field : this.idFields) {
            Object value = field.get(this.entity);
            Id id = field.<Id>getAnnotation(Id.class);
            if (id != null && (value == null || StringUtils.isBlank(value.toString())))
                throw new Exception("" + JSON.toJSONString(this.entity));
            String name = field.getName();
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                name = columnAnno.name();
            }
            JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
            if (jsonField != null)
                name = jsonField.name();
            String safeStr = getSafeValue4Sql(field);
            sql.append(" and " + name + " = " + safeStr);
        }
        return sql.toString();
    }

    private String getSafeValue4Sql(Field field) throws Exception {
        Object value = field.get(this.entity);
        JSONField jsonField = field.<JSONField>getAnnotation(JSONField.class);
        String safeStr = "null";
        if (value instanceof Date || value instanceof java.sql.Date || value instanceof java.sql.Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            safeStr = "'" + sdf.format(value) + "'";
        } else if (value instanceof Number) {
            safeStr = value + "";
        } else if (jsonField != null || value instanceof java.util.Map || value instanceof List || value

                .getClass().isArray()) {
            safeStr = "'" + replaceSpecialChar(JSON.toJSONString(value)) + "'";
        } else {
            safeStr = "'" + replaceSpecialChar(value.toString()) + "'";
        }
        return safeStr;
    }

    public static String replaceSpecialChar(String source) {
        if (StringUtils.isBlank(source))
            return "";
        String cleanStr = source.trim();
        if (cleanStr.contains("'"))
            cleanStr = cleanStr.replace("'", "''");
        return cleanStr;
    }

    public static String parseTableNameByClass(Class clazz) {
        if (clazz == null)
            return null;
        String className = clazz.getSimpleName();
        if (className.length() == 1)
            return className.toLowerCase();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (i > 0 && Character.isUpperCase(ch))
                name.append("_");
            name.append(ch);
        }
        return name.toString().toLowerCase();
    }

    public String getDataKey() throws Exception {
        if (this.idFields == null || this.idFields.isEmpty())
            return null;
        StringBuilder content = new StringBuilder();
        int size = this.idFields.size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < size; i++) {
            Field field = this.idFields.get(i);
            Object value = field.get(this.entity);
            if (value != null && field.getType() == Date.class)
                value = sdf.format(value);
            value = replaceSpecialChar(value + "");
            content.append(value);
            if (i != size - 1)
                content.append("_");
        }
        return content.toString();
    }

    public Object getEntity() {
        return this.entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Field> getIdFields() {
        return this.idFields;
    }

    public void setIdFields(List<Field> idFields) {
        this.idFields = idFields;
    }

    public List<Field> getAllFields() {
        return this.allFields;
    }

    public void setAllFields(List<Field> allFields) {
        this.allFields = allFields;
    }
}
