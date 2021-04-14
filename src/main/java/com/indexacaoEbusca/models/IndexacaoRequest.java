package com.indexacaoEbusca.models;

public class IndexacaoRequest {
	
	private Integer id;
	
	private String nomeArquivo;
	
	private String texto;
	
	public IndexacaoRequest() {

	}

	public IndexacaoRequest(Integer id, String nomeArquivo, String texto) {
		this.id = id;
		this.nomeArquivo = nomeArquivo;
		this.texto = texto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
