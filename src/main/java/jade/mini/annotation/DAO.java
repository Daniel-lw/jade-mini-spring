package jade.mini.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-26
 * Time: 下午8:05
 * To change this template use File | Settings | File Templates.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Inherited
@java.lang.annotation.Documented
public @interface DAO {
    java.lang.String catalog() default "";

    java.lang.String[] table() default {""};
}
