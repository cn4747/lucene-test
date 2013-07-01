package lucene;

import java.io.File;
import java.io.IOException;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

/**
 *
 * @author tony
 */
public class XmlParser {

    /**
     * Method that actually parses the XML and throws parsed data into index
     * @param file A XML file name to parse
     * @throws IOException
     * @throws SAXException 
     */
    public void parse(String file) throws IOException, SAXException {
        // create an instance of Digester
        Digester digester = new Digester();
        // disable XML validation
        digester.setValidating(false);
        
        // instantiate current class
        digester.push(this);
        // instantiate Page class
        digester.addObjectCreate("mediawiki/page", Page.class);
        
        // set callable methods for fields
        digester.addCallMethod("mediawiki/page/id", "setId", 0);
        digester.addCallMethod("mediawiki/page/title", "setTitle", 0);
        digester.addCallMethod("mediawiki/page/revision", "addRevision", 2);
        digester.addCallParam("mediawiki/page/revision/timestamp", 0);
        digester.addCallParam("mediawiki/page/revision/text", 1);

        // call 'addPage' method when the next page is seen
        digester.addSetNext("mediawiki/page", "addPage");

        digester.parse(new File(file));
    }
    
    /**
     * Method to add pages
     * @param page An instance of Page, with its fields
     */
    public void addPage(Page page) throws IOException {
        Indexer.add(page);
    }
}