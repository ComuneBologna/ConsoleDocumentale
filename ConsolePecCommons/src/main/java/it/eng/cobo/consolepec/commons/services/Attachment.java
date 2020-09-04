package it.eng.cobo.consolepec.commons.services;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Da utilizzare per la gestione durante la serializzazione. Consente di escludere dalla serializzazione in json l'oggetto e aggiungerlo come allegato
 * alla response in modo da poter trasferire un {@link java.io.InputStream} tra il transactional e lo spagic client
 */
@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Attachment {
	//
}