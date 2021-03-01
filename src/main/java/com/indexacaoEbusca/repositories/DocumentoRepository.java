package com.indexacaoEbusca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.indexacaoEbusca.models.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	
}
