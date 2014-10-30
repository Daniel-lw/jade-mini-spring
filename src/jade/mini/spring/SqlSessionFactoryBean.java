package jade.mini.spring;

import jade.mini.core.SQLTemplateHolder;
import jade.mini.exception.ParamMissingException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 上午1:06
 * To change this template use File | Settings | File Templates.
 */
public class SqlSessionFactoryBean implements InitializingBean {
    private final Log logger = LogFactory.getLog(getClass());
    private DataSource dataSource;
    private String environment = "TEST";
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setEnvironment(String environment) {

        this.environment = environment;
        if (System.getProperty("JadeMini-environment") != null && System.getProperty("JadeMini-environment").trim().length() != 0) {
            this.environment = System.getProperty("JadeMini-environment");
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.

        buildSqlSessionFactory();
    }


    protected void buildSqlSessionFactory() throws Exception {
        Resource envResouce = new ClassPathResource(this.environment + ".properties");
        InputStream inputStream = null;
        if (envResouce != null) inputStream = envResouce.getInputStream();
        if (inputStream != null) {
            Properties envProperties = new Properties();
            envProperties.load(inputStream);
            this.dataSource = BasicDataSourceFactory.createDataSource(envProperties);
        }

        if (this.dataSource == null)
            throw new ParamMissingException("dataSource param init error ");
        else if (this.namedParameterJdbcTemplate == null) {
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
            SQLTemplateHolder.setNamedParameterJdbcTemplate(this.namedParameterJdbcTemplate);
        } else {
            logger.info("dataSource and namedParameterJdbcTemplate init ok!  ");
        }
    }
}
