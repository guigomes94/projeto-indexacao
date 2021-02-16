package com.indexacaoEbusca.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.indexacaoEbusca.models.DBFile;

public interface DBFileRepository extends JpaRepository<DBFile, Long> {

}
