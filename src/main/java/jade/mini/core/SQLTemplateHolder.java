package jade.mini.core;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-28
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class SQLTemplateHolder {
    public static NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

    public static void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        if (SQLTemplateHolder.namedParameterJdbcTemplate != null || SQLTemplateHolder.namedParameterJdbcTemplate != namedParameterJdbcTemplate) {
            SQLTemplateHolder.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        }
    }
}
