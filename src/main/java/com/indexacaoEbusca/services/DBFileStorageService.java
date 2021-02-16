package com.indexacaoEbusca.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.indexacaoEbusca.Repositories.DBFileRepository;
import com.indexacaoEbusca.models.DBFile;
import com.indexacaoEbusca.services.exceptions.FileStorageException;
import com.indexacaoEbusca.services.exceptions.MyFileNotFoundException;

@Service
public class DBFileStorageService {
	
	@Autowired
    private DBFileRepository repository;
	
	 public DBFile storeFile(MultipartFile file) {
	        // Normalize file name
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	        try {
	            // Check if the file's name contains invalid characters
	            if(fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }

	            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());

	            return repository.save(dbFile);
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }
	
	 public DBFile getFile(Long fileId) {
	        return repository.findById(fileId)
	                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
	    }

}
