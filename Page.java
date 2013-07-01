package lucene;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
* This class represents a page parsed from the XML file
*/
public class Page {
    private int id;
    private String title;
    private String text;
    private String date;
    private HashMap<String, String> revisions = new HashMap<>();

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public int getId() {
        return this.id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
    
    /**
     * Add revision to HashMap
     * @param timestamp XML timestamp
     * @param text Revision text
     */
    public void addRevision(String timestamp, String text) {
        revisions.put(timestamp, text);
    }
    
    /**
     * Return all collected revisions
     * @return HashMap with revisions
     */
    public Set<Entry<String, String>> getRevisions() {
        return revisions.entrySet();
    }
}