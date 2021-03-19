package com.indexacaoEbusca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.indexacaoEbusca.models.DBFile;
import com.indexacaoEbusca.models.UploadFileResponse;

@Service
public class UploadService {
	
	private static String BASE_URL = "/api/documentos";
	
	@Autowired
	private DBFileStorageService dbFileStorageService;
	
	@Autowired
	private FileLocalStorageService fileLocalStorageService;
	
	@Autowired
	private OCRService extrator;
	
	@Autowired
	private ProcessadorService processador;
	
	@Autowired
	private IndexadorService indexador;
	
	public UploadFileResponse uploadFile(MultipartFile file) {
		String localStorage = fileLocalStorageService.storeFile(file); // armazenar o arquivo localmente

		String extracao = extrator.extrairTexto(localStorage); // extrair texto do pdf
		
		String keywords = processador.getRadicais(extracao); // processar o texto pra gerar índices
		
		fileLocalStorageService.storeKeywords(keywords); // salvar índices localmente
		
		DBFile dbFile = dbFileStorageService.storeFile(file); // salvar arquivo no banco de dados
		
		indexador.criarOuAtualizarIndices(dbFile.getFileName(), dbFile.getId().toString()); // criar ou atualizar os índices
		
		String fileDownloadUri = ServletUriComponentsBuilder.
				fromCurrentContextPath()
				.path(BASE_URL)
				.path("/downloadFile/")
				.path(dbFile.getId().toString())
				.toUriString();
		
		UploadFileResponse response = new UploadFileResponse(dbFile.getFileName(),
				fileDownloadUri, 
				file.getContentType(), 
				file.getSize());
		
		return response;

	}

}
