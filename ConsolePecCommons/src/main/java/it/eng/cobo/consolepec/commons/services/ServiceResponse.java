package it.eng.cobo.consolepec.commons.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 * @param <T> Response object
 */
@NoArgsConstructor
public class ServiceResponse<T> {
	
	@Getter
	@Setter
	private T response;
	
	@Getter
	@Setter
	private ServiceError serviceError;
	
	@Getter
	@Setter
	private boolean error;
	
	public ServiceResponse(T response) {
		this.response = response;
		this.error = false;
	}
	
	public ServiceResponse(ServiceError error) {
		this.serviceError = error;
		this.error = true;
	}
}
