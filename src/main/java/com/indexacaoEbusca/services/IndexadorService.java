package com.indexacaoEbusca.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indexacaoEbusca.models.IndexacaoRequest;
import com.indexacaoEbusca.models.IndexacaoResponse;

@Service
public class IndexadorService {

	@Autowired
	private ProcessadorService processador;

	private static final Logger logger = LogManager.getLogger(IndexadorService.class);
	private String pastaIndice = "Indices";

	public IndexacaoResponse indexarArquivo(IndexacaoRequest data) {

		try {
			indexar(data);
		} catch (IOException e) {
			logger.info(" erro na classe: " + e.getClass() + ", mensagem: " + e.getMessage());
		}

		LocalDate dataIndexacao = LocalDate.now();

		IndexacaoResponse res = new IndexacaoResponse(dataIndexacao);

		return res;
	}

	public void indexar(IndexacaoRequest data) throws IOException {
		try {
			IndexWriter writer = configurarIndice(); // true = apaga e cria indice
			Document doc = new Document();
			String dataIndexacao = DateTools.dateToString(new Date(), Resolution.DAY);
			String conteudo = processador.prepararIndices(data.getTexto());

			doc.add(new TextField("conteudo", conteudo, Store.YES));
			doc.add(new StringField("chavePrincipal", data.getChavePrincipal().toString(), Field.Store.YES));
			if (data.getChaveSecundaria() != null) {
				doc.add(new StringField("chaveSecundaria", data.getChaveSecundaria().toString(), Field.Store.YES));
			}
			doc.add(new TextField("tamanho", String.valueOf(data.getTexto().length()), Store.YES));
			doc.add(new StringField("dataIndexacao", dataIndexacao, Store.YES));

			logger.info("Adicionando Documento ao índice");
			writer.addDocument(doc);
			writer.close();
			logger.info("Fechando índice");

		} catch (Exception e) {
			logger.info(" erro na classe: " + e.getClass() + ", mensagem: " + e.getMessage());
		}
	}

	private IndexWriter configurarIndice() {
		try {
			Directory dir = FSDirectory.open(Paths.get(pastaIndice));
			Analyzer analizador = new BrazilianAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analizador);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND); // Add new documents to an existing index:
			logger.info("Abrindo pasta: '" + pastaIndice);

			IndexWriter writer = new IndexWriter(dir, iwc);
			return writer;

		} catch (IOException e) {
			logger.info(" erro na classe: " + e.getClass() + ", mensagem: " + e.getMessage());
		}
		return null;
	}

}
