package ro.munteanu.insight.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import ro.munteanu.insight.Vote;


public class VoteManager {

  private static final Logger logger = Logger.getLogger(VoteManager.class.getName());
  private static final String VOTE_KIND = "Vote";
  private static final FetchOptions FETCH_OPTIONS = FetchOptions.Builder.withLimit(Integer.MAX_VALUE);

  private DatastoreService datastore;

  public VoteManager() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  public void createVote(Vote v) {
    Entity vote = new Entity(VOTE_KIND);

    vote.setProperty("name", v.getVoter());
    datastore.put(vote);
  }

  public Collection<Vote> readVotes() {
      try {
    ArrayList<Vote> results = new ArrayList<Vote>();

    //add projection here
    PropertyProjection pi = new PropertyProjection("name", String.class);
    Query q = new Query(VOTE_KIND).addProjection(pi).setDistinct(true);

    PreparedQuery pq = datastore.prepare(q);

    for (Entity resultEntity : pq.asIterable()) {
        String name = resultEntity.getProperty("name").toString();
        Filter nameFilter = new FilterPredicate("name", 
                                                FilterOperator.EQUAL,
                                                name);
        Query innerQ = new Query(VOTE_KIND).setFilter(nameFilter);
        PreparedQuery innerPq = datastore.prepare(innerQ);
        int count = innerPq.countEntities(FETCH_OPTIONS);
        results.add(new Vote(count, name));
    }
    return results;
      } catch (IllegalArgumentException iae) {
        logger.log(Level.WARNING, "IAE: " + iae.getMessage(), iae);
        throw iae;
      }

  }
}
