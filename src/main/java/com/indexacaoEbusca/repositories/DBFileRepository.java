package com.indexacaoEbusca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.indexacaoEbusca.models.DBFile;

public interface DBFileRepository extends JpaRepository<DBFile, Long> {
	
	/*
	 * @Query("SELECT f FROM files f WHERE f.id IN (?)") List<DBFile>
	 * search(List<Long> ids);
	 */
	
	List<DBFile> findByIdIn(List<Long> ids);

}
