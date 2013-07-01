package lucene;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Class that makes all the search
 * @author tony
 */
public class Searcher {
    /**
     * Performs a search
     * @param query Word to search
     * @throws ParseException
     * @throws IOException 
     */
    public static void search(String query) throws ParseException, IOException {
        Directory index = FSDirectory.open(new File(Lucene.indexDir));
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        // set searchable fields
        String[] fields = new String[] {"text", "text_old", "text_very_old"};
        Query q = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse(query);
        //MultiFieldQueryParser
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(Lucene.hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        
        System.out.println("Found " + hits.length + " hits.");
        for(int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("id") + ". " + d.get("title"));
        }
        
        reader.close();
    }
}
