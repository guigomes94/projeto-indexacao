package com.indexacaoEbusca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.indexacaoEbusca.models.ExtracaoResponse;
import com.indexacaoEbusca.models.IndexacaoRequest;
import com.indexacaoEbusca.models.IndexacaoResponse;
import com.indexacaoEbusca.models.SearchResponse;
import com.indexacaoEbusca.services.BuscadorService;
import com.indexacaoEbusca.services.IndexadorService;
import com.indexacaoEbusca.services.OCRService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api")
public class IndexacaoBuscaController {
	
	@Autowired
	private OCRService ocrService;
	
	@Autowired
	private IndexadorService indexacaoService;
	
	@Autowired
	private BuscadorService buscador;
	
	
	@ApiOperation("Realizará a extração do texto do arquivo.")
	@PostMapping("/extrair")
	public ResponseEntity<?> extrairTexto(@RequestParam("file") MultipartFile file) {
		ExtracaoResponse response = ocrService.extrairTexto(file);
		
		return response != null ? ResponseEntity.status(HttpStatus.CREATED).body(response) : ResponseEntity.badRequest().build();
	}
	
	@ApiOperation("Realizará a indexacão do arquivo")
	@PostMapping("/indexar")
	public ResponseEntity<?> indexarArquivo(@RequestBody IndexacaoRequest informacoes) {
		IndexacaoResponse response = indexacaoService.indexarArquivo(informacoes);
		
		return response != null ? ResponseEntity.status(HttpStatus.CREATED).body(response) : ResponseEntity.badRequest().build();
	}
	
	@ApiOperation("Retorna uma lista de arquivos, conforme a busca.")
	@GetMapping("/buscarFile")
	public ResponseEntity<?> buscarFile(@RequestParam String text) {
		SearchResponse result = buscador.searchFiles(text);
		
		return ResponseEntity.ok(result);
	}
	
	@ApiOperation("Retorna uma lista de documentos, conforme a busca.")
	@GetMapping("/buscarDoc")
	public ResponseEntity<?> buscarDoc(@RequestParam String text) {
		SearchResponse result = buscador.searchDocs(text);
		
		return ResponseEntity.ok(result);
	}

}
