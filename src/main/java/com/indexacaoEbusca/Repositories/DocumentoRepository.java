package com.indexacaoEbusca.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.indexacaoEbusca.models.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	
	@Query("SELECT d FROM Documento d WHERE UPPER(d.text) LIKE %?1%")
	List<Documento> search(String text);
	
}
