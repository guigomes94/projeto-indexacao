package com.indexacaoEbusca.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.indexacaoEbusca.models.ExtracaoResponse;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OCRService {
	
	@Autowired
	private FileLocalStorageService fileLocalStorageService;
	
	@Autowired
	private ProcessadorService processador;
	
	public ExtracaoResponse extrairTexto(MultipartFile file) {
		String pathStorage = fileLocalStorageService.storeFile(file);
		String result = extrair(pathStorage);
		ExtracaoResponse res = new ExtracaoResponse(result);
		return res;
	}
	
	private String extrair(String filePath) {
		File image = new File(filePath);
		String LIB_FOLDER_PATH = "./tessdata";
		
		ITesseract instance = new Tesseract();
		instance.setDatapath(LIB_FOLDER_PATH);
		instance.setLanguage("por");
		
		String result;
		
		try {
			result = instance.doOCR(image);
			String textoLimpo = processador.limparTexto(result);
			
			return textoLimpo;
		} catch (TesseractException e) {
			return null;
		} finally {
			image.delete();
		}

	}

}
