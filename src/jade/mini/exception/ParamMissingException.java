package jade.mini.exception;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-24
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
public class ParamMissingException extends Exception {
    public ParamMissingException(String msg) {
        super(msg);
    }
}
