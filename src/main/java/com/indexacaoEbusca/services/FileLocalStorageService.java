package com.indexacaoEbusca.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.indexacaoEbusca.configs.FileStorageProperties;
import com.indexacaoEbusca.services.exceptions.FileStorageException;

@Service
public class FileLocalStorageService {
	
	private final Path fileStorageLocation;

    @Autowired
    public FileLocalStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    public void storeKeywords(String text) {
    	FileWriter arq;
    	PrintWriter escrever;
    	
		try {
			arq = new FileWriter("./keywords/keywords.txt");
			escrever = new PrintWriter(arq);
			escrever.printf(text);
			
			arq.close();
			escrever.close();
			
		} catch (IOException e) {
			throw new FileStorageException("Could not store file keywords.txt. Please try again!", e);
		}
    }

}
