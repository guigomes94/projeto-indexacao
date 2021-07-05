package com.indexacaoEbusca.models;

public class IndexacaoRequest {
	
	private String chavePrincipal;
	
	private String chaveSecundaria;
	
	private String texto;
	
	public IndexacaoRequest() {

	}

	public IndexacaoRequest(String chavePrincipal, String chaveSecundaria, String texto) {
		this.chavePrincipal = chavePrincipal;
		this.chaveSecundaria = chaveSecundaria;
		this.texto = texto;
	}

	public String getChavePrincipal() {
		return chavePrincipal;
	}

	public void setChavePrincipal(String chavePrincipal) {
		this.chavePrincipal = chavePrincipal;
	}

	public String getChaveSecundaria() {
		return chaveSecundaria;
	}

	public void setChaveSecundaria(String chaveSecundaria) {
		this.chaveSecundaria = chaveSecundaria;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
