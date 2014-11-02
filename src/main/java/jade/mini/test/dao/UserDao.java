package jade.mini.test.dao;


import jade.mini.annotation.DAO;
import jade.mini.annotation.SQL;
import jade.mini.annotation.SQLParamBase;
import jade.mini.annotation.SQLParamObject;
import jade.mini.test.testImpl.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 下午8:03
* To change this template use File | Settings | File Templates.
        */
@DAO(catalog = "life_trunk", table = {"user", "book"})
public interface UserDao {
    @SQL(value = "select * from $0")
    public List<User> select(@SQLParamBase("id") long id);
}
