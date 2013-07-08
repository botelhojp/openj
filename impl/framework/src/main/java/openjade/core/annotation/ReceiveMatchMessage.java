package openjade.core.annotation;

import jade.content.lang.leap.LEAPCodec;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@SuppressWarnings("rawtypes")
public @interface ReceiveMatchMessage {

	Class action();

	Class ontology();

	Class codec() default LEAPCodec.class;

	int[] performative() default {-1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,29,20,21};
}
