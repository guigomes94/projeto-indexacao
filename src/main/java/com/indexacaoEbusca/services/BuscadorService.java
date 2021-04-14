package com.indexacaoEbusca.services;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.SearchResponse;
import com.indexacaoEbusca.services.exceptions.ConsultaIndiceException;
import com.indexacaoEbusca.services.exceptions.FileStorageException;

@Service
public class BuscadorService {

	public SearchResponse searchFiles(String search) {
		String pastaIndice = "indices";
		String field = "contents";
		String consulta = search;
		
		SearchResponse resultSearch = new SearchResponse();

		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(pastaIndice)));
		} catch (IOException e) {
			throw new FileStorageException("Não foi possível acessar os índices!", e);
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analizador = new BrazilianAnalyzer();
		
		QueryParser parser = new QueryParser(field, analizador);

		Query query;
		try {
			query = parser.parse(consulta);
		} catch (ParseException e) {
			throw new ConsultaIndiceException("Algo deu errado na consulta. Por favor, tente novamente!", e);
		}
		// System.out.println("Buscando por: " + query.toString(field));
		TopDocs hits;
		try {
			hits = searcher.search(query, 100);
		} catch (IOException e) {
			throw new ConsultaIndiceException("Algo deu errado na consulta. Por favor, tente novamente!", e);
		}
		ScoreDoc[] scoreDocs = hits.scoreDocs;

		// int n = Math.toIntExact(hits.totalHits.value);
		// System.out.println(n + " total de acertos");

		for(ScoreDoc sd : scoreDocs){
			Document d;
			try {
				d = searcher.doc(sd.doc);
			} catch (IOException e) {
				throw new ConsultaIndiceException("Algo deu errado na consulta. Por favor, tente novamente!", e);
			}
			

			resultSearch.ids.add(Long.parseLong(d.get("id")));
		}
	
		try {
			reader.close();
		} catch (IOException e) {
			throw new FileStorageException("Não foi possível acessar os índices!", e);
		}
		
		return resultSearch;
	}


}
