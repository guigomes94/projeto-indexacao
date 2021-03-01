package com.indexacaoEbusca.services;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.SearchResponse;

@Service
public class SearchService {

	public List<SearchResponse> searchFiles(String search) throws Exception {
		String pastaIndice = "indice";
		String field = "contents";
		String consulta = search;
		
		List<SearchResponse> result = new ArrayList<>();

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(pastaIndice)));
		IndexSearcher searcher = new IndexSearcher(reader);
		//Analyzer analyzer = new StandardAnalyzer();
		Analyzer analizador = new BrazilianAnalyzer();
		
		QueryParser parser = new QueryParser(field, analizador);

		Query query = parser.parse(consulta);
		// System.out.println("Buscando por: " + query.toString(field));
		TopDocs hits = searcher.search(query, 100);
		ScoreDoc[] scoreDocs = hits.scoreDocs;

		// int n = Math.toIntExact(hits.totalHits.value);
		// System.out.println(n + " total de acertos");

		for(ScoreDoc sd : scoreDocs){
			Document d = searcher.doc(sd.doc);
			SearchResponse res = new SearchResponse();
			res.setId(Long.parseLong(d.get("id")));
			res.setName(d.get("name"));
			result.add(res);
		}
	
		reader.close();
		
		return result;
	}


}
