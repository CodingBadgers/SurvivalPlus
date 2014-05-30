package uk.codingbadgers.SurvivalPlus.commands.annotations;

import uk.codingbadgers.SurvivalPlus.commands.utils.SenderType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sender {

    SenderType[] value();

    String message() default "You cannot use this command as a ${type}.";

}
