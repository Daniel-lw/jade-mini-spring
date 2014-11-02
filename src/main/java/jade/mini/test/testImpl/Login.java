package jade.mini.test.testImpl;

import jade.mini.spring.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-27
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class Login {
    @Qualifier("user")
    public MapperFactoryBean mapperFactoryBean;

    public static void main(String args[]){
        Login login = new Login();
        MapperFactoryBean one = login.mapperFactoryBean;
        if (one == null){System.out.println("shit");}
        else {System.out.println("ok");}
    }
}
