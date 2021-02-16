package com.indexacaoEbusca.services;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

@Service
public class ProcessadorService {
	
	public String processarTexto(String fullText) {
		TokenStream tokenStream = null;
		try {
			// treat the dashed words, don't let separate them during the processing
			fullText = fullText.replaceAll("-+", "-0");
			// replace any punctuation char but apostrophes and dashes with a space
			fullText = fullText.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
			// replace most common English contractions
			fullText = fullText.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

			StandardTokenizer stdToken = new StandardTokenizer();
			stdToken.setReader(new StringReader(fullText));
			// tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new
			// LowerCaseFilter(stdToken))), EnglishAnalyzer.getDefaultStopSet());

			tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))),
					BrazilianAnalyzer.getDefaultStopSet());

			tokenStream.reset();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			StringBuilder resultado = new StringBuilder();
			// pegar as palavras (token) do texto
			while (tokenStream.incrementToken()) {
				String term = token.toString();
				resultado.append(term + "\n");
			}
			return resultado.toString();

		} catch (IOException e) {
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

}
