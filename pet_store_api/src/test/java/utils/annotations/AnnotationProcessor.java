package utils.annotations;

import exceptions.AnnotationException;
import utils.annotations.helper.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationProcessor {

    /**
     * Method, which check the annotation for method in interface
     *
     * @param methodName      name of method
     * @param annotationClass annotation for this method
     * @return {@link Annotation}
     */
    public static <T extends Annotation> T validateMethod(final String methodName, final Class<?> feature,
                                                          final Class<T> annotationClass) {
        Method thisMethod = Arrays.stream(feature.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow();
        if (thisMethod.isAnnotationPresent(annotationClass)) {
            return thisMethod.getAnnotation(annotationClass);
        } else throw new AnnotationException(
                "No annotation @" + annotationClass.getSimpleName() + "for method" + methodName + "!");
    }

    /**
     * The method returns the required url depending on the url value variables in the annotation {@link Service}:<br>
     * $ - for environment variables<br>
     * # - for system properties
     *
     * @param service service class
     * @return String
     */
    public static String getServiceBaseUrl(Class<?> service) {
        String url;

        if (service.isAnnotationPresent(Service.class)) {
            url = service.getAnnotation(Service.class).url();
        } else throw new AnnotationException("No annotation @Service for " + service.getSimpleName() + ".class");

        if (url.contains("${")) {
            int beginIndex = url.indexOf("{") + 1;
            int endIndex = url.indexOf("}");
            String variableName = url.substring(beginIndex, endIndex);
            String variableValue = System.getenv(variableName);
            return url.replace("${" + variableName + "}", variableValue);
        } else if (url.contains("#{")) {
            int beginIndex = url.indexOf("{") + 1;
            int endIndex = url.indexOf("}");
            String variableName = url.substring(beginIndex, endIndex);
            String variableValue = System.getProperty(variableName);
            return url.replace("#{" + variableName + "}", variableValue);
        }
        return url;
    }
}
