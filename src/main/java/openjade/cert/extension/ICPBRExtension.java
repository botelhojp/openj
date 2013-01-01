package openjade.cert.extension;
/**

 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import openjade.cert.OIDExtension;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
@OIDExtension(loader=ICPBRExtensionLoader.class)
public @interface ICPBRExtension {
	
	public ICPBRExtensionType type();

}