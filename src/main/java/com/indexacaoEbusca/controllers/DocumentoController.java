package com.indexacaoEbusca.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.indexacaoEbusca.models.DBFile;
import com.indexacaoEbusca.models.Documento;
import com.indexacaoEbusca.models.SearchResponse;
import com.indexacaoEbusca.models.UploadFileResponse;
import com.indexacaoEbusca.services.DBFileStorageService;
import com.indexacaoEbusca.services.DocumentoService;
import com.indexacaoEbusca.services.FileStorageService;
import com.indexacaoEbusca.services.IndexadorService;
import com.indexacaoEbusca.services.KeywordsExtractorService;
import com.indexacaoEbusca.services.OCRService;
import com.indexacaoEbusca.services.ProcessadorService;
import com.indexacaoEbusca.services.SearchService;
import com.indexacaoEbusca.services.utils.Stem;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {
	
	private static String BASE_URL = "/api/documentos";

	@Autowired
	private DocumentoService service;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private DBFileStorageService dbFileStorageService;

	@Autowired
	private OCRService ocrService;
	
	@Autowired
	private ProcessadorService processador;
	
	@Autowired
	private KeywordsExtractorService keywordExtractor;
	
	@Autowired
	private IndexadorService indexador;
	
	@Autowired
	private SearchService buscador;

	@GetMapping
	public ResponseEntity<?> listAll() {
		List<Documento> result = service.listAll();
		return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<?> listBySearch(@RequestParam String text) {
		List<SearchResponse> result = new ArrayList<>();
		try {
			result = buscador.searchFiles(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = ServletUriComponentsBuilder.
				fromCurrentContextPath()
				.path(BASE_URL)
				.path("/downloadFile/")
				.toUriString();
		
		for (SearchResponse res : result) {
			res.setUrl(url + res.getId());
		}
		
		return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		String location = fileStorageService.storeFile(file);

		String extracao = ocrService.crack(location);
		
		String processada = processador.processarTexto(extracao);
		
		List<Stem> keywords = new LinkedList<>();

		try {
			keywords = keywordExtractor.getKeywordsList(processada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Documento doc = new Documento();
		String textSteam = "";
		
		if (!keywords.isEmpty()) {
			
			StringBuilder result = new StringBuilder();
			
			for (Stem keyword : keywords) {
				result.append(keyword.getStem() + " ");
			}
			
			textSteam = result.toString();
			
			int size = result.length();
			
			if (size > 255) {
				doc.setText(result.substring(0, 255));
			} else {
				doc.setText(result.substring(0, size - 1));
			}

		}
		
		fileStorageService.storeKeywords(textSteam);
		
		Documento save = service.create(doc);
		
		DBFile dbFile = dbFileStorageService.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.
				fromCurrentContextPath()
				.path(BASE_URL)
				.path("/downloadFile/")
				.path(dbFile.getId().toString())
				.toUriString();
		
		indexador.criarOuAtualizarIndices(dbFile.getFileName(), dbFile.getId().toString());

		return save != null ? 
				ResponseEntity.status(HttpStatus.CREATED)
				.body(new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize()))
				: ResponseEntity.badRequest().build();
	}

	
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		// Load file from database
		DBFile dbFile = dbFileStorageService.getFile(fileId);

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header("Content-Disposition", "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}

}
