package com.indexacaoEbusca.services;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.br.BrazilianStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.services.utils.Stem;

/**
 * Keywords extractor functionality handler
 */
@Service
public class ProcessadorService {
	private static List<Stem> lista;
	/**
	 * Get list of keywords with stem form, frequency rank, and terms dictionary
	 *
	 * @param fullText
	 * @return List<Stem>, which contains keywords cards
	 * @throws IOException
	 */
	public List<Stem> getKeywordsList(String fullText) throws IOException {
		TokenStream tokenStream = null;

		try {
			//Modulo limpeza
			
			// treat the dashed words, don't let separate them during the processing
			fullText = fullText.replaceAll("-+", "-0");
			// replace any punctuation char but apostrophes and dashes with a space
			fullText = fullText.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
			// replace most common English contractions
			fullText = fullText.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

			if(lista==null) {
				lista = new ArrayList<>();
			}
			StandardTokenizer stdToken = new StandardTokenizer();
			stdToken.setReader(new StringReader(fullText));
			
			tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), 
							BrazilianAnalyzer.getDefaultStopSet());
			
			tokenStream.reset();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			
			//Modulo Tokenizacao
			while (tokenStream.incrementToken()) {
				String term = token.toString();
				String s = getStemForm(term);

				if (s != null) {
					Stem stem = find(lista, new Stem(s.replaceAll("-0", "-")));
					stem.add(term.replaceAll("-0", "-"));
				}
			}

			// reverse sort by frequency
			Collections.sort(lista);

			return lista;
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get stem form of the term
	 *
	 * @param term
	 * @return String, which contains the stemmed form of the term
	 * @throws IOException
	 */
	private static String getStemForm(String term) throws IOException {
		TokenStream tokenStream = null;
		try {
			StandardTokenizer stdToken = new StandardTokenizer();
			stdToken.setReader(new StringReader(term));

			//algoritmo de Porter
			tokenStream = new BrazilianStemFilter(stdToken);
			tokenStream.reset();

			// eliminate duplicate tokens by adding them to a set
			Set<String> stems = new HashSet<>();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

			while (tokenStream.incrementToken()) {
				stems.add(token.toString());
			}

			// if stem form was not found or more than 2 stems have been found, return null
			if (stems.size() != 1) {
				return null;
			}

			String stem = stems.iterator().next();

			// if the stem form has non-alphanumerical chars, return null
			if (!stem.matches("[a-zA-Z0-9-]+")) {
				return null;
			}

			return stem;
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Find sample in collection
	 *
	 * @param collection
	 * @param sample
	 * @param <T>
	 * @return <T> T, which contains the found object within collection if exists, otherwise the initially searched object
	 */
	private static <T> T find(Collection<T> collection, T sample) {

		for (T element : collection) {
			if (element.equals(sample)) {
				return element;
			}
		}

		collection.add(sample);

		return sample;
	}

	public String getRadicais(String extracao) {
		List<Stem> keywords = new LinkedList<>();
		String radicais = "";

		try {
			keywords = getKeywordsList(extracao);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!keywords.isEmpty()) {
			
			StringBuilder result = new StringBuilder();
			
			for (Stem keyword : keywords) {
				result.append(keyword.getStem() + " ");
				System.out.println(keyword.getStem());
			}
			
			radicais = result.toString();
		}
		
		return radicais;
	}
}
