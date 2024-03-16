package com.ucoruh.communication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldEncoding {
  EncodingType value();
  int minLength() default 0;
  int maxLength() default Integer.MAX_VALUE;
}
