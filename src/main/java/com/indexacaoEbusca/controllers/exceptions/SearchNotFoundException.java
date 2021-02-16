package com.indexacaoEbusca.controllers.exceptions;

public class SearchNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SearchNotFoundException(String message) {
		super(message);
	}

}
