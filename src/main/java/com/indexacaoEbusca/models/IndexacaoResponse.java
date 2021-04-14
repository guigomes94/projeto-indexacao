package com.indexacaoEbusca.models;

import java.time.LocalDate;

public class IndexacaoResponse {
	
	private LocalDate dataIndexacao;

	public IndexacaoResponse(LocalDate dataIndexacao) {
		this.dataIndexacao = dataIndexacao;
	}

	public LocalDate getDataIndexacao() {
		return dataIndexacao;
	}

	public void setDataIndexacao(LocalDate dataIndexacao) {
		this.dataIndexacao = dataIndexacao;
	}
	
}
