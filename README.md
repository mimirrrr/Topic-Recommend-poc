Legal Recommender - PoC

Jak spustit v IntelliJ / z příkazové řádky:
1) Otevři projekt v IntelliJ jako Maven project.
2) Spusť třídu cz.codexis.legalrecommender.LegalRecommenderApplication
3) Endpoint pro doporučení: GET http://localhost:8080/api/recommend/{docId}
   Např.: http://localhost:8080/api/recommend/doc1

Poznámky:
- EmbeddingUtil.generateEmbedding používá pro PoC deterministickou pseudo-embedding funkci.
  V produkci nahraď voláním Gemini/OpenAI embeddings a ulož výsledky do vektorové DB (Milvus, FAISS, Pinecone).
- Projekt je minimální PoC pro hackathon.
