package jade.mini.spring;


import jade.mini.core.Empty;
import jade.mini.core.MiniProxy;
import jade.mini.core.SqlUtils;
import jade.mini.exception.ParamMissingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-10-24
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public class MapperScannerConfigurer implements BeanFactoryPostProcessor, InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private String basePackage;
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;

    public String getString() {
        return this.applicationContext.toString();
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setBasePackeage(String basePackeage) {
        this.basePackage = basePackeage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        SqlUtils.setApplicationContext(applicationContext);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Scanner scanner = new Scanner((BeanDefinitionRegistry) configurableListableBeanFactory);
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",;\t\n"));
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(this.basePackage)) throw new ParamMissingException("basePackeage param is missing ");
    }


    private final class Scanner extends ClassPathBeanDefinitionScanner {
        public Scanner(BeanDefinitionRegistry registry) {
            super(registry);
        }

        protected void registerDefaultFilters() {
            boolean acceptAllInterfaces = true;

            if (MapperScannerConfigurer.this.annotationClass != null) {
                addIncludeFilter(new AnnotationTypeFilter(MapperScannerConfigurer.this.annotationClass));
                acceptAllInterfaces = false;
            }

            if (MapperScannerConfigurer.this.markerInterface != null) {
                addIncludeFilter(new AssignableTypeFilter(MapperScannerConfigurer.this.markerInterface) {
                    protected boolean matchClassName(String className) {
                        return false;
                    }
                });
                acceptAllInterfaces = false;
            }

            if (acceptAllInterfaces) {
                addIncludeFilter(new TypeFilter() {
                    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                        return true;
                    }

                });
            }

            addExcludeFilter(new TypeFilter() {
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    Class candidateClass = null;
                    try {
                        candidateClass = getClass().getClassLoader().loadClass(classMetadata.getClassName());
                    } catch (ClassNotFoundException ex) {
                        return false;
                    }

                    if (candidateClass.getMethods().length == 0) {
                        return true;
                    }
                    return false;
                }
            });
        }

        protected Set<BeanDefinitionHolder> doScan(String[] basePackages) {
            Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
            if (beanDefinitions.isEmpty())
                this.logger.warn("No MyBatis mapper was found in '" + MapperScannerConfigurer.this.basePackage +
                        "' package. Please check your configuration.");
            else {
                for (BeanDefinitionHolder holder : beanDefinitions) {
                    GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" +
                                definition.getBeanClassName() + "' mapperInterface");
                    }

                    try {
                        definition.getPropertyValues().addPropertyValue("mapper", new MiniProxy().bind(new Empty(), definition.resolveBeanClass(this.getClass().getClassLoader())));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    definition.setBeanClass(MapperFactoryBean.class);


                }
            }

            return beanDefinitions;
        }

        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return (beanDefinition.getMetadata().isInterface()) && (beanDefinition.getMetadata().isIndependent());
        }

        protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
            if (super.checkCandidate(beanName, beanDefinition)) {
                return true;
            }
            this.logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" +
                    beanDefinition.getBeanClassName() + "' mapperInterface" +
                    ". Bean already defined with the same name!");
            return false;
        }

    }
}
