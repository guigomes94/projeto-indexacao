package com.indexacaoEbusca.services;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.SearchResponse;

@Service
public class BuscadorService {
	
	private static final Logger logger = Logger.getLogger(BuscadorService.class);

	public SearchResponse search(String search) {
		SearchResponse resultSearch = new SearchResponse();
		
		String pastaIndice = "indices";
		String field = "conteudo";
		String consulta = search;
		
		try {
			
			Directory diretorio = FSDirectory.open(Paths.get(pastaIndice));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher searcher = new IndexSearcher(reader);
			
			Analyzer analizador = new BrazilianAnalyzer();
			
			QueryParser parser = new QueryParser(field, analizador);

			Query query = parser.parse(consulta);
			
			System.out.println("Buscando por: " + query.toString(field));
			
			TopDocs hits = searcher.search(query, 100);
			ScoreDoc[] scoreDocs = hits.scoreDocs;

			int n = Math.toIntExact(hits.totalHits.value);
			System.out.println(n + " total de acertos");

			for(ScoreDoc sd : scoreDocs){
				Document d = searcher.doc(sd.doc);
				
				resultSearch.chavesPrincipal.add(d.get("chavePrincipal"));
				resultSearch.chavesSecundaria.add(d.get("chaveSecundaria"));
			}
			
			reader.close();
			diretorio.close();
			
			return resultSearch;
		}
		catch(Exception e) {
			logger.info(" erro na classe: " + e.getClass() +	", mensagem: " + e.getMessage());
		}
		
		return resultSearch;
	}
	

}
