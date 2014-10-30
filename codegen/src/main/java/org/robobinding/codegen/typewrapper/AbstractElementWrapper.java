package org.robobinding.codegen.typewrapper;

import java.lang.annotation.Annotation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;

/**
 * @since 1.0
 * @author Cheng Wei
 *
 */
public abstract class AbstractElementWrapper {
	protected final ProcessingContext context;
	protected final Types types;
	private final Element element;
	
	public AbstractElementWrapper(ProcessingContext context, Types types, Element element) {
		this.context = context;
		this.types = types;
		this.element = element;
	}

	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return element.getAnnotation(annotationType) != null;
	}
	
	public <A extends Annotation> AnnotationMirrorWrapper getAnnotation(Class<A> annotationType) {
		AnnotationMirror annotationMirror = findAnnotationMirror(annotationType);
		if(annotationMirror != null) {
			return new AnnotationMirrorWrapper(context, annotationMirror);
		} else {
			throw new RuntimeException("Method '"+toString()+"' is not annotated with @"+annotationType.getName());
		}
	}
	
	private  <A extends Annotation> AnnotationMirror findAnnotationMirror(Class<A> annotationType) {
		for(AnnotationMirror m : element.getAnnotationMirrors()) {
			if(types.isSameType(m.getAnnotationType(), context.typeMirrorOf(annotationType))) {
				return m;
			}
		}
		return null;
	}
}