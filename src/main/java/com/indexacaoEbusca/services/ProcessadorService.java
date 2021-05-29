package com.indexacaoEbusca.services;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.br.BrazilianStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;


@Service
public class ProcessadorService {
	
	public String limparTexto(String fullText) {
		TokenStream tokenStream = null;

		try {

			// remocao de caracteres especiais
			fullText = fullText.replaceAll("[\\p{Punct}&&[^'-]]+", "");
			fullText = fullText.replaceAll("'", "");
			fullText = removerCaracteresEspeciais(fullText);

			StandardTokenizer stdToken = new StandardTokenizer();
			stdToken.setReader(new StringReader(fullText));
			
			tokenStream = new LowerCaseFilter(stdToken); // filtrar ruídos e transformar para minusculas
			tokenStream = new StopFilter(tokenStream, BrazilianAnalyzer.getDefaultStopSet()); // remover stop words
			
			tokenStream.reset();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			StringBuilder result = new StringBuilder();
			
			while (tokenStream.incrementToken()) {
				String term = token.toString();
				if (term.length() > 2) {
					result.append(term + " ");
				}
			}
			tokenStream.end();
			
			return result.toString();
			
		} catch(IOException e) {	
			e.printStackTrace();	
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public String prepararIndices(String texto) {
		TokenStream tokenStream = null;

		try {

			StandardTokenizer stdToken = new StandardTokenizer();
			stdToken.setReader(new StringReader(texto));
			
			tokenStream = new BrazilianStemFilter(stdToken); // radicalizar termos
					
			tokenStream.reset();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			StringBuilder result = new StringBuilder();
			
			while (tokenStream.incrementToken()) {
				String term = token.toString();
				result.append(term + " ");
				
			}
			tokenStream.end();
			return result.toString();
			
		} catch(IOException e) {	
			e.printStackTrace();	
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private String removerCaracteresEspeciais(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

}
