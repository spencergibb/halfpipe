package halfpipe.context;

/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * Simple Pointcut that looks for a specific Java 5 annotation
 * being present on a {@link #forClassAnnotation class} or
 * {@link #forMethodAnnotation method}.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.aop.support.annotation.AnnotationClassFilter
 * @see org.springframework.aop.support.annotation.AnnotationMethodMatcher
 */
public class NotClassAnnotationMatchingPointcut implements Pointcut {

    private final ClassFilter classFilter;

    private final MethodMatcher methodMatcher;

    class NotAnnotationClassFilter extends AnnotationClassFilter {

        public NotAnnotationClassFilter(Class<? extends Annotation> annotationType) {
            super(annotationType);
        }

        public NotAnnotationClassFilter(Class<? extends Annotation> annotationType, boolean checkInherited) {
            super(annotationType, checkInherited);
        }

        @Override
        public boolean matches(Class clazz) {
            return !super.matches(clazz);
        }
    }


    /**
     * Create a new AnnotationMatchingPointcut for the given annotation type.
     * @param classAnnotationType the annotation type to look for at the class level
     */
    public NotClassAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType) {
        this.classFilter = new NotAnnotationClassFilter(classAnnotationType);
        this.methodMatcher = MethodMatcher.TRUE;
    }

    /**
     * Create a new AnnotationMatchingPointcut for the given annotation type.
     * @param classAnnotationType the annotation type to look for at the class level
     * @param checkInherited whether to explicitly check the superclasses and
     * interfaces for the annotation type as well (even if the annotation type
     * is not marked as inherited itself)
     */
    public NotClassAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
        this.classFilter = new NotAnnotationClassFilter(classAnnotationType, checkInherited);
        this.methodMatcher = MethodMatcher.TRUE;
    }

    /**
     * Create a new AnnotationMatchingPointcut for the given annotation type.
     * @param classAnnotationType the annotation type to look for at the class level
     * (can be <code>null</code>)
     * @param methodAnnotationType the annotation type to look for at the method level
     * (can be <code>null</code>)
     */
    public NotClassAnnotationMatchingPointcut(
            Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {

        Assert.isTrue((classAnnotationType != null || methodAnnotationType != null),
                "Either Class annotation type or Method annotation type needs to be specified (or both)");

        if (classAnnotationType != null) {
            this.classFilter = new NotAnnotationClassFilter(classAnnotationType);
        }
        else {
            this.classFilter = ClassFilter.TRUE;
        }

        if (methodAnnotationType != null) {
            this.methodMatcher = new AnnotationMethodMatcher(methodAnnotationType);
        }
        else {
            this.methodMatcher = MethodMatcher.TRUE;
        }
    }


    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    public MethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }


    /**
     * Factory method for an AnnotationMatchingPointcut that matches
     * for the specified annotation at the class level.
     * @param annotationType the annotation type to look for at the class level
     * @return the corresponding AnnotationMatchingPointcut
     */
    public static NotClassAnnotationMatchingPointcut forClassAnnotation(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        return new NotClassAnnotationMatchingPointcut(annotationType);
    }

    /**
     * Factory method for an AnnotationMatchingPointcut that matches
     * for the specified annotation at the method level.
     * @param annotationType the annotation type to look for at the method level
     * @return the corresponding AnnotationMatchingPointcut
     */
    public static NotClassAnnotationMatchingPointcut forMethodAnnotation(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        return new NotClassAnnotationMatchingPointcut(null, annotationType);
    }

}
