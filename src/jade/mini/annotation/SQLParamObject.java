package jade.mini.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-28
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.PARAMETER})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface SQLParamObject {
    java.lang.String value();
}