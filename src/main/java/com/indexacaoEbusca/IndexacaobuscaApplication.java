package com.indexacaoEbusca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.indexacaoEbusca.configs.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class IndexacaobuscaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndexacaobuscaApplication.class, args);
	}

}
