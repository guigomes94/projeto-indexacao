package com.indexacaoEbusca.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.Documento;
import com.indexacaoEbusca.repositories.DocumentoRepository;

@Service
public class DocumentoService {
	
	@Autowired
	private DocumentoRepository documentos;
	
	public List<Documento> listAll() {
		return documentos.findAll();
	}
	
	public Documento create(Documento obj) {
		try {
			return documentos.save(obj);
			
		} catch (IllegalArgumentException e) {
			return null;
			
		}
	}

}
