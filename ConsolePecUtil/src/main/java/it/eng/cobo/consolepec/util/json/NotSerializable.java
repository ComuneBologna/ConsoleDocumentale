package it.eng.cobo.consolepec.util.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks an {@link AutoValue @AutoValue}-annotated type for proper Gson serialization.
 * <p>
 * This annotation is needed because the {@linkplain Retention retention} of {@code @AutoValue} does not allow reflection at runtime.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface NotSerializable {}
