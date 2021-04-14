package com.indexacaoEbusca.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.IndexacaoResponse;
import com.indexacaoEbusca.models.IndexacaoRequest;

@Service
public class IndexacaoService {
	
	@Autowired
	private IndexadorService indexador;
	
	@Autowired
	private ProcessadorService processador;
	
	@Autowired
	private FileLocalStorageService fileLocalStorageService;
	
	public IndexacaoResponse indexarArquivo(IndexacaoRequest info) {
		
		String keywords = processador.getRadicais(info.getTexto());
		
		fileLocalStorageService.storeKeywords(keywords);
		
		indexador.criarOuAtualizarIndices(info.getNomeArquivo(), info.getId().toString());
		
		LocalDate dataIndexacao = LocalDate.now();
		
		IndexacaoResponse res = new IndexacaoResponse(dataIndexacao);
		
		return res;
	}

}
