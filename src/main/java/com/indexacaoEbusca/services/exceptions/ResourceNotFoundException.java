package com.indexacaoEbusca.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(Object busca) {
		super("Nada encontrado para: " + busca);
	}

}
