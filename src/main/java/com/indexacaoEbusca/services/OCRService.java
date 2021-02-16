package com.indexacaoEbusca.services;

import java.io.File;

import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OCRService {
	
	public String crack(String filePath) {
		String LIB_FOLDER_PATH = "./tessdata";
		File imageFile = new File(filePath);
		
		ITesseract instance = new Tesseract();
		instance.setDatapath(LIB_FOLDER_PATH);
		instance.setLanguage("por");
		
		String result;
		
		try {
			result = instance.doOCR(imageFile);
			return result;
		} catch (TesseractException e) {
			return null;
		}

	}

}
