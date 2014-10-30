package jade.mini.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 下午8:15
 * To change this template use File | Settings | File Templates.
 */
public enum ReturnType {
    KEYS("keys"), ROWS("rows");
    private String type;

    private ReturnType(String type) {
        this.type = type;

    }


}
