package com.petclinic.spring;

import org.springframework.context.ApplicationContext;
import java.util.Objects;

public final class ApplicationContextHolder {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextHolder.applicationContext = Objects.requireNonNull(applicationContext);
    }

    /**
     * Get spring bean by type
     *
     * @deprecated prefer proper dependency injection, use only for intermediate steps
     */
    public static <T> T getBean(Class<T> beanClass) {
        return getApplicationContext().getBean(beanClass);
    }

    /**
     * Get spring bean by type and name
     *
     * @deprecated prefer proper dependency injection, use only for intermediate steps
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return getApplicationContext().getBean(name, beanClass);
    }

    private static ApplicationContext getApplicationContext() {
        return Objects.requireNonNull(ApplicationContextHolder.applicationContext,
                "applicationContext is not initialized");
    }

    private ApplicationContextHolder() {
    }
}
