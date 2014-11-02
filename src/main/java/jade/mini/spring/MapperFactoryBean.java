package jade.mini.spring;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-24
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class MapperFactoryBean {
    private Object mapper;

    public Object getMapper() {
        return mapper;
    }

    public void setMapper(Object mapper) {
        this.mapper = mapper;
    }
}
