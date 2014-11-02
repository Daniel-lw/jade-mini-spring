package jade.mini.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 下午8:08
 * To change this template use File | Settings | File Templates.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface SQL {
    java.lang.String value();

    ReturnType returnType() default ReturnType.ROWS;
}