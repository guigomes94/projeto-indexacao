package com.indexacaoEbusca.controllers;

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

import com.indexacaoEbusca.models.DBFile;
import com.indexacaoEbusca.models.SearchResponse;
import com.indexacaoEbusca.models.UploadFileResponse;
import com.indexacaoEbusca.services.BuscadorService;
import com.indexacaoEbusca.services.DBFileStorageService;
import com.indexacaoEbusca.services.UploadService;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private BuscadorService buscador;
	
	@Autowired
	private DBFileStorageService dbFileStorageService;

	@GetMapping("/search")
	public ResponseEntity<?> listBySearch(@RequestParam String text) {
		List<SearchResponse> result = buscador.searchFiles(text);
		
		return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		DBFile dbFile = dbFileStorageService.getFile(fileId);

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header("Content-Disposition", "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}
	
	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		UploadFileResponse response = uploadService.uploadFile(file);
		
		return response != null ? ResponseEntity.status(HttpStatus.CREATED).body(response): ResponseEntity.badRequest().build();
	}

}
