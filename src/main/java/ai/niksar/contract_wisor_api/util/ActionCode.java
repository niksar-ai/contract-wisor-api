package ai.niksar.contract_wisor_api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
This represents the operation performed for each API request. It must be unique for each API. When this value is changed, it must be updated in all places where it is used.
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionCode {
    String value();
}
