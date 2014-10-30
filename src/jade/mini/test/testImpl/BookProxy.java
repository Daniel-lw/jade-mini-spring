package jade.mini.test.testImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-27
 * Time: 上午9:15
 * To change this template use File | Settings | File Templates.
 */
public class BookProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("haha");
        return null;
    }
}
