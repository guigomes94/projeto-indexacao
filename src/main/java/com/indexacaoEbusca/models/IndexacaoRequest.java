package com.indexacaoEbusca.models;

public class IndexacaoRequest {
	
	private Integer idDocumento;
	
	private Integer idArquivo;
	
	private String texto;
	
	public IndexacaoRequest() {

	}

	public IndexacaoRequest(Integer idDocumento, Integer idArquivo, String texto) {
		this.idDocumento = idDocumento;
		this.idArquivo = idArquivo;
		this.texto = texto;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Integer getIdArquivo() {
		return idArquivo;
	}

	public void setIdArquivo(Integer idArquivo) {
		this.idArquivo = idArquivo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}


}
