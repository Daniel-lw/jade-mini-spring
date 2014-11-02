package jade.mini.core;

import jade.mini.exception.EmptyClassException;
import jade.mini.spring.MapperFactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public class SqlUtils {
    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SqlUtils.applicationContext = applicationContext;
    }

    public Object getServiceObject(String name) throws EmptyClassException {
        if (applicationContext != null) {
            return ((MapperFactoryBean) applicationContext.getBean(name)).getMapper();
        } else throw new EmptyClassException("applicationContext has no Object with name called " + name);

    }
}
