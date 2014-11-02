package jade.mini;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionUtil {

    public static Exception unwrapThrowable(Throwable throwable) {
        return new RuntimeException();
    }
}
