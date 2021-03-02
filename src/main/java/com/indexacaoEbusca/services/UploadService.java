package com.indexacaoEbusca.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.indexacaoEbusca.models.DBFile;
import com.indexacaoEbusca.models.UploadFileResponse;
import com.indexacaoEbusca.services.utils.Stem;

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
	private KeywordsExtractorService keywordsExtrator;
	
	@Autowired
	private IndexadorService indexador;
	
	public UploadFileResponse uploadFile(MultipartFile file) {
		String localStorage = fileLocalStorageService.storeFile(file);

		String extracao = extrator.crack(localStorage);
		
		String processada = processador.processarTexto(extracao);
		
		List<Stem> keywords = new LinkedList<>();

		try {
			keywords = keywordsExtrator.getKeywordsList(processada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String textSteam = "";
		
		if (!keywords.isEmpty()) {
			
			StringBuilder result = new StringBuilder();
			
			for (Stem keyword : keywords) {
				result.append(keyword.getStem() + " ");
			}
			
			textSteam = result.toString();
		}
		
		fileLocalStorageService.storeKeywords(textSteam);
		
		DBFile dbFile = dbFileStorageService.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.
				fromCurrentContextPath()
				.path(BASE_URL)
				.path("/downloadFile/")
				.path(dbFile.getId().toString())
				.toUriString();
		
		indexador.criarOuAtualizarIndices(dbFile.getFileName(), dbFile.getId().toString());
		
		
		UploadFileResponse response = new UploadFileResponse(dbFile.getFileName(),
				fileDownloadUri, 
				file.getContentType(), 
				file.getSize());
		
		return response;

	}

}
