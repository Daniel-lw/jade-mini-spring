package jade.mini.core;

import jade.mini.annotation.SQL;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-27
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
public class MiniProxy implements InvocationHandler {
    private Object target;
    private Object currentUsed;

    /**
     * 绑定委托对象并返回一个代理类
     *
     * @param target
     * @return
     */
    public Object bind(Object target, Class interfaces) {
        this.target = target;
        //取得代理对象
        this.currentUsed = Proxy.newProxyInstance(target.getClass().getClassLoader(),
                new Class[]{interfaces}, this);
        return this.currentUsed;
    }

    @Override
    /**
     * 调用方法
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        String sql = ProxyHelper.SQLMethodHelper(method);
        Map<String, Object> daoResult = ProxyHelper.DaoTargetHelper(this.currentUsed);
        String source = (String) daoResult.get(ProxyHelper.CATALOG);
        String[] tables = (String[]) daoResult.get(ProxyHelper.TABLE);
        sql = ProxyHelper.sqlConvert(sql, source, tables);
        return ProxyHelper.SQLSend(sql, args, method);
    }


}
