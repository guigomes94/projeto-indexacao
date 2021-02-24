/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indexacaoEbusca.services;


import java.io.IOException;
import java.nio.file.Paths;

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

/** Simple command-line based search demo. */
public class SearchFiles {

	public static void main(String[] args) throws Exception {
		String pastaIndice = "indice";
		String consulta = "tecnologia";
		String field = "contents";

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(pastaIndice)));
		IndexSearcher searcher = new IndexSearcher(reader);
		//Analyzer analyzer = new StandardAnalyzer();
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
			System.out.println("Nome do Arquivo: " + d.get("path"));
			System.out.println("Detalhando os resultados:");
			System.out.println(searcher.explain(query, sd.doc));
		}
	
		reader.close();
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search engine presents 
	 * pages of size n to the user. The user can then go to the next page if interested in
	 * the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results are collected
	 * to fill 5 result pages. If the user wants to page beyond this limit, then the query
	 * is executed another time and all hits are collected.
	 * 
	 */
	public static void doPagingSearch(IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = Math.toIntExact(results.totalHits.value);
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				String line = "y";
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String path = doc.get("path");
				if (path != null) {
					System.out.println((i+1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.println("   Title: " + doc.get("title"));
					}
				} else {
					System.out.println((i+1) + ". " + "No path for this document");
				}

			}

			//			if (!interactive || end == 0) {
			//				break;
			//			}

			//			if (numTotalHits >= end) {
			//				boolean quit = false;
			//				while (true) {
			//					System.out.print("Press ");
			//					if (start - hitsPerPage >= 0) {
			//						System.out.print("(p)revious page, ");  
			//					}
			//					if (start + hitsPerPage < numTotalHits) {
			//						System.out.print("(n)ext page, ");
			//					}
			//					System.out.println("(q)uit or enter number to jump to a page.");
			//
			//					String line = in.readLine();
			//					if (line.length() == 0 || line.charAt(0)=='q') {
			//						quit = true;
			//						break;
			//					}
			//					if (line.charAt(0) == 'p') {
			//						start = Math.max(0, start - hitsPerPage);
			//						break;
			//					} else if (line.charAt(0) == 'n') {
			//						if (start + hitsPerPage < numTotalHits) {
			//							start+=hitsPerPage;
			//						}
			//						break;
			//					} else {
			//						int page = Integer.parseInt(line);
			//						if ((page - 1) * hitsPerPage < numTotalHits) {
			//							start = (page - 1) * hitsPerPage;
			//							break;
			//						} else {
			//							System.out.println("No such page");
			//						}
			//					}
			//				}
			//				if (quit) break;
			//				end = Math.min(numTotalHits, start + hitsPerPage);
		}//while
	}
}
