package jade.mini.test.testImpl;

import jade.mini.spring.MapperFactoryBean;
import jade.mini.test.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 下午11:18
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String args[]) {
        Date loadStart = new Date();
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        MapperFactoryBean mapperFactoryBean1 = (MapperFactoryBean) context.getBean("userDao");
        UserDao test1 = (UserDao) mapperFactoryBean1.getMapper();
        Date loadend = new Date();
        System.out.println("load:" + (loadend.getSeconds()-loadStart.getSeconds()));
        System.out.println(test1.select(1).get(3).getUserName());
        Date sqlend = new Date();
        System.out.println("sql:" + (sqlend.getSeconds()-loadend.getSeconds()));
    }
}
