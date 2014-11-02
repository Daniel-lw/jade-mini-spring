package jade.mini.core;

import jade.mini.annotation.*;
import jade.mini.exception.JadeMiniException;
import jade.mini.exception.ParamMissingException;
import jade.mini.exception.ParamOutOfBoundException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-28
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class ProxyHelper {

    public static String CATALOG = "catalog";
    public static String TABLE = "table";
    public static String TABLE_PRE = "$";

    public static String SQLMethodHelper(Method method) throws ParamMissingException {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations.length == 0) throw new ParamMissingException("Method has no annotation with name SQL");
        else {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(SQL.class)) {
                    return ((SQL) annotation).value();
                }
            }
        }
        throw new ParamMissingException("Method has no annotation with name SQL");
    }

    public static ReturnType SQLReturnHelper(Method method) throws ParamMissingException {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations.length == 0) throw new ParamMissingException("Method has no annotation with name SQL");
        else {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(SQL.class)) {
                    return ((SQL) annotation).returnType();
                }
            }
        }
        throw new ParamMissingException("Method has no annotation with name SQL");
    }

    public static Map<String, Object> DaoTargetHelper(Object target) throws ParamMissingException, ParamOutOfBoundException {
        Class clazz = target.getClass();
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) throw new ParamMissingException("target has no interface ");
        if (interfaces.length >= 2)
            throw new ParamOutOfBoundException("target has more interface out of our exception");
        DAO annotationDAO = (DAO) interfaces[0].getAnnotation(DAO.class);
        if (annotationDAO == null) throw new ParamMissingException("target has no interface with name called DAO");
        HashMap<String, Object> annotationResult = new HashMap<String, Object>(2);
        annotationResult.put(ProxyHelper.CATALOG, annotationDAO.catalog());
        annotationResult.put(ProxyHelper.TABLE, annotationDAO.table());
        return annotationResult;

    }

    public static String sqlConvert(String sql, String source, String[] tables) {
        int index = 0;
        while ((index = sql.indexOf(ProxyHelper.TABLE_PRE)) > 0) {
            int table_index = Integer.valueOf(String.valueOf(sql.charAt(index + 1)));
            String tableName = source + "." + tables[table_index];
            sql = sql.substring(0, index) + " " + tableName + " " + sql.substring(index + 2, sql.length());
        }
        return sql;
    }

    public static Object SQLSend(String sql, Object[] args, Method method) throws ParamMissingException {
        ReturnType returnType = method.getAnnotation(SQL.class).returnType();
        int methodReturnType = returnTypeint(method.getReturnType());
        String methodType = method.getAnnotation(SQL.class).value().substring(0, method.getAnnotation(SQL.class).value().indexOf(" "));
        Map<String, Object> paramValue = new HashMap<String, Object>(4);
        Annotation currentAnnotation = null;
        Annotation firstAnnotation = null;
        Annotation[][] annotations = method.getParameterAnnotations();
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = null;
        SqlParameterSource sqlParameterSource = null;
        if (args != null && args.length != 0) {
            firstAnnotation = annotations[0][0];
            if (firstAnnotation.annotationType().equals(SQLParamBase.class)) {
                for (int index = 0; index < args.length; index++) {
                    currentAnnotation = annotations[index][0];
                    if (currentAnnotation.annotationType().equals(firstAnnotation.annotationType())) {
                        paramValue.put(((SQLParamBase) currentAnnotation).value(), args[index]);
                    } else throw new JadeMiniException("jada-mini not support Method annotation type over 2");
                }
                sqlParameterSource = new MapSqlParameterSource(paramValue);
            }

            if (firstAnnotation.annotationType().equals(SQLParamObject.class)) {
                if (args.length >= 2)
                    throw new JadeMiniException("jada-mini not support Method annotation count >= 2 with type SQLParamObject ");
                else {
                    beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(args[0]);
                }
            }

        }
        if (methodTypeInt(methodType) == 3) throw new JadeMiniException("SQL method analyze error");
        else if (methodTypeInt(methodType) == 1) {
            if (returnType.equals(ReturnType.ROWS)) {
                if (currentAnnotation.annotationType().equals(SQLParamBase.class))
                    return doUpateToRowsByBase(SQLTemplateHolder.namedParameterJdbcTemplate, sql, sqlParameterSource);
                else
                    return doUpateToRowsByObject(SQLTemplateHolder.namedParameterJdbcTemplate, sql, beanPropertySqlParameterSource);
            } else {
                if (currentAnnotation.annotationType().equals(SQLParamBase.class))
                    return doUpateToIdsByBase(SQLTemplateHolder.namedParameterJdbcTemplate, sql, sqlParameterSource);
                else
                    return doUpateToIdsByObject(SQLTemplateHolder.namedParameterJdbcTemplate, sql, beanPropertySqlParameterSource);
            }
        } else {

            if (methodReturnType == 3)
                throw new JadeMiniException("jade mini method not support returnType" + method.getReturnType());
            if (methodReturnType == 1) {
                if (currentAnnotation.annotationType().equals(SQLParamBase.class))
                    return doSelectToSingleByBase(SQLTemplateHolder.namedParameterJdbcTemplate, sql, sqlParameterSource, virtualReturnType(method));
                else
                    return doSelectToSingleByObject(SQLTemplateHolder.namedParameterJdbcTemplate, sql, beanPropertySqlParameterSource, virtualReturnType(method));

            } else {
                if (currentAnnotation.annotationType().equals(SQLParamBase.class))
                    return doSelectToListByBase(SQLTemplateHolder.namedParameterJdbcTemplate, sql, sqlParameterSource, virtualReturnType(method));
                else
                    return doSelectToListByObject(SQLTemplateHolder.namedParameterJdbcTemplate, sql, beanPropertySqlParameterSource, virtualReturnType(method));
            }

        }
    }

    private static Class virtualReturnType(Method method) {
            Class clazz = method.getReturnType();
        if (clazz.getSimpleName().equalsIgnoreCase("long")) return Long.class;

        if (clazz.getSimpleName().equalsIgnoreCase("int") || clazz.getSimpleName().equalsIgnoreCase("integer"))
            return Integer.class;

        if (clazz.getSimpleName().equalsIgnoreCase("string")) return String.class;

        if (clazz.getSimpleName().equalsIgnoreCase("double")) return Double.class;

        if (clazz.getSimpleName().equalsIgnoreCase("void")) return Void.class;

        if (clazz.getSimpleName().contains("list") || clazz.getSimpleName().contains("List")) {
          return (Class) ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];  //获取list<T> T的类型
        }

        return clazz;
    }

    private static int returnTypeint(Class clazz) {
        if (clazz.getSimpleName().equalsIgnoreCase("long") || clazz.getSimpleName().equalsIgnoreCase("int") || clazz.getSimpleName().equalsIgnoreCase("integer") || clazz.getSimpleName().equalsIgnoreCase("string") || clazz.getSimpleName().equalsIgnoreCase("double") || clazz.getSimpleName().equalsIgnoreCase("void"))
            return 1;
        if (clazz.getSimpleName().contains("list") || clazz.getSimpleName().contains("List")) return 2;
        return 1;
    }

    private static int methodTypeInt(String methodType) {
        if (resembleString(methodType, "select")) return 2;
        else if (resembleString(methodType, "update") || resembleString(methodType, "delete") || resembleString(methodType, "insert"))
            return 1;
        else return 3;

    }

    private static boolean resembleString(String a, String b) {
        if (a.contains(b.toLowerCase()) || a.contains(b.toUpperCase()) || a.equalsIgnoreCase(b)) return true;
        else return false;
    }

    private static long doUpateToRowsByBase(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, SqlParameterSource sqlParameterSource) {
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    private static long doUpateToIdsByBase(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, SqlParameterSource sqlParameterSource) {
        KeyHolder keyholder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyholder);
        return keyholder.getKey().longValue();
    }

    private static long doUpateToRowsByObject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
        KeyHolder keyholder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, beanPropertySqlParameterSource, keyholder);
        return keyholder.getKey().longValue();
    }

    private static long doUpateToIdsByObject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
        return namedParameterJdbcTemplate.update(sql, beanPropertySqlParameterSource);
    }

    private static Object doSelectToSingleByBase(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, SqlParameterSource sqlParameterSource, Class objectType) {
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new BeanPropertyRowMapper(objectType));
    }

    private static Object doSelectToSingleByObject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, BeanPropertySqlParameterSource beanPropertySqlParameterSource, Class objectType) {
        return namedParameterJdbcTemplate.queryForObject(sql, beanPropertySqlParameterSource, new BeanPropertyRowMapper(objectType));
    }

    private static List doSelectToListByBase(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, SqlParameterSource sqlParameterSource, Class objectType) {
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new BeanPropertyRowMapper(objectType));
    }

    private static List doSelectToListByObject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, BeanPropertySqlParameterSource beanPropertySqlParameterSource, Class objectType) {
        return namedParameterJdbcTemplate.query(sql, beanPropertySqlParameterSource, new BeanPropertyRowMapper(objectType));
    }
}
