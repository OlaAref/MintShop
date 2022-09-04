package com.olaaref.mintshop.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {
	
	public String message() default "";
	public Class<?>[] groups() default {};
	public Class<? extends Payload>[] payload() default {};
	
	//comparable objects
	public String first();
	public String second();
	
	@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List{
		public FieldMatch[] value();
	}
	

}
