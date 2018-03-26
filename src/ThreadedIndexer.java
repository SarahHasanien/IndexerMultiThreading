import javafx.util.Pair;
import org.bson.Document;
import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

public class ThreadedIndexer {
    public static void main(String[] args) throws InterruptedException {
	// write your code here
        Indexer[] indexers = new Indexer[25];
        MyDB DB = new MyDB();
        ArrayList<Pair<String, Pair<String, String> >> urls = DB.retrieveURLs();
        
        int part = urls.size()/25, cnt = 0;
        for (Indexer indexer: indexers) {
            indexer = new Indexer();
            for (int i = 0; i < part; i++) {
                indexer.content.add(urls.get(cnt).getValue());
                indexer.addresses.add(urls.get(cnt++).getKey());
            }
            indexer.run();
        }
        Indexer updatedThread=new Indexer();
        urls = DB.retrieveUpdatedURLs();
        for (int i = 0; i < urls.size(); i++) 
        {
        	updatedThread.content.add(urls.get(i).getValue());
            updatedThread.addresses.add(urls.get(i).getKey());
        }
        updatedThread.run();
        for (Indexer indexer: indexers) 
        {
           try {
              if(indexer != null) 
              {
                    indexer.join();
              }
              } 
           catch (InterruptedException e) 
           {
                //e.printStackTrace();
           }
        }
        ///////////////////////////////////////
       //Thread.sleep(1000000);
        Boolean updated=false;
        if(updated)
        while(true)
        {
        	System.out.println("Checking for updates");
        	ArrayList<Pair<String, Pair<String, String> >> updatedArray =DB.retrieveUpdatedURLs();
  		   	int k=0;
            Indexer idx = new Indexer();
            for (int i = 0; i < updatedArray.size(); i++) 
            {
            	if(updatedArray.get(k).getValue().getValue()!="1")
            	{
            		DB.removeURL(updatedArray.get(k).getKey());//remove address from URLs collection
                    idx.content.add(updatedArray.get(k).getValue());
                    idx.addresses.add(updatedArray.get(k++).getKey());
            	}
            }
            idx.run();
            Thread.sleep(100000);
        }
	}
}
