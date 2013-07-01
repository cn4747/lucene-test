/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;

/**
 *
 * @author tony
 */
public class Lucene {
    /**
     * Scores
     */
    public static float scoreText = 1.0f;
    public static float scoreTextOld = 0.7f;
    public static float scoreTextVeryOld = 0.5f;
    /**
     * Directory where XML files are located
     */
    public static String dataPath = "data/";
    
    /**
     * Directory where index will be stored
     */
    public static String indexDir = "index";
    
    /**
     * Number of shown results
     */
    public static int hitsPerPage = 10;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException, ParseException {
        boolean createIndex = false;
        String reply;
        String xmlFileName = "example.xml";
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        // get XML file name from commandline, if any
        if (args.length > 0) {
            xmlFileName = args[0];
        }
        // check for file existence
        while (new File(Lucene.dataPath + xmlFileName).exists() == false) {
            System.out.print(
                    "No file '" + xmlFileName + "'! Enter new file name: ");
            xmlFileName = in.readLine();
        }

        // asking user whether to rebuild index
        /*System.out.print("Rebuild index? (y/n) ");
        reply = in.readLine();
        if (reply.equals("y") || reply.equals("yes")) {*/
            createIndex = true;
        //}
        
        //if (createIndex == true) {
            // create new index
            Indexer.create();
            // instantiate parser and parse XML
            XmlParser parser = new XmlParser();
            parser.parse(Lucene.dataPath + xmlFileName);
            // close index writer
            Indexer.close();
        //}
        
        // do the query and search thing
        /*boolean continueSearch = true;
        do {
            System.out.print("Enter word to search (q to quit): ");
            reply = in.readLine();
            if (reply.equals("q") == false) {
                Searcher.search(reply);
            } else {
                continueSearch = false;
            }
        } while (continueSearch);*/
        Searcher.search("April");
    }
}
