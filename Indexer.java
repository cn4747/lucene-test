package lucene;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map.Entry;
import javax.xml.bind.DatatypeConverter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author tony
 */
public class Indexer {
    private static IndexWriter writer;
    
    /**
     * Method to instantiate indexer with specified config
     * @param create Flag for creating new index
     * @throws IOException 
     */
    public static void create() throws IOException {
        // instantiating analyzer
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        // creating a config
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        // create new index
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // creating the index writer
        Indexer.writer = new IndexWriter(FSDirectory.open(new File(Lucene.indexDir)), config);
    }
    
    /**
     * Method adds document to index
     * @param page Instance of Page class, contains data to add to index
     * @throws IOException 
     */
    public static void add(Page page) throws IOException {
        Field field;
        // markers for old and very old documents
        Calendar old = Calendar.getInstance();
        old.add(Calendar.YEAR, -3);
        Calendar veryOld = Calendar.getInstance();
        veryOld.add(Calendar.YEAR, -5);
        // create a new document
        Document doc = new Document();
        // add fields to document
        doc.add(new IntField("id", page.getId(), Field.Store.YES));
        doc.add(new StringField("title", page.getTitle(), Field.Store.YES));
        // go through all revisions and fill text fields
        for (Entry<String, String> el : page.getRevisions()) {
            // compare date to some predefined values to determine old and very old docs
            if (DatatypeConverter.parseDateTime(el.getKey()).before(veryOld)) {
                field = new TextField("text_very_old", el.getValue(), Field.Store.YES);
                field.setBoost(Lucene.scoreText);
                doc.add(field);
            } else if (DatatypeConverter.parseDateTime(el.getKey()).before(old)) {
                field = new TextField("text_old", el.getValue(), Field.Store.YES);
                field.setBoost(Lucene.scoreTextOld);
                doc.add(field);
            } else {
                field = new TextField("text", el.getValue(), Field.Store.YES);
                field.setBoost(Lucene.scoreTextVeryOld);
                doc.add(field);
            }
        }
        // add document to index
        Indexer.writer.addDocument(doc);
    }

    public static void close() throws IOException {
        Indexer.writer.close();
    }
}
