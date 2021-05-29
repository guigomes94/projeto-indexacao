package com.indexacaoEbusca.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	
	private String uploadDir;
	
	private String indiceDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

	public String getIndiceDir() {
		return indiceDir;
	}

	public void setIndiceDir(String indiceDir) {
		this.indiceDir = indiceDir;
	}

}
