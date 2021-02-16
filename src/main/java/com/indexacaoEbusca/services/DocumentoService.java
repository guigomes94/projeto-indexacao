package com.indexacaoEbusca.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.Repositories.DocumentoRepository;
import com.indexacaoEbusca.models.Documento;

@Service
public class DocumentoService {
	
	@Autowired
	private DocumentoRepository documentos;
	
	public List<Documento> listAll() {
		return documentos.findAll();
	}
	
	public List<Documento> listBySearch(String text) {
		return documentos.search(text.toUpperCase()); 
	}
	
	public Documento create(Documento obj) {
		try {
			return documentos.save(obj);
			
		} catch (IllegalArgumentException e) {
			return null;
			
		}
	}

}
