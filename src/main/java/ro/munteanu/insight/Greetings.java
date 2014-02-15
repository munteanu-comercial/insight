package ro.munteanu.insight;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import ro.munteanu.insight.domain.VoteManager;


/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(
    name = "insight",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
    Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
    audiences = {Constants.ANDROID_AUDIENCE}
)
public class Greetings {
  public static ArrayList<HelloGreeting> greetings = new ArrayList<HelloGreeting>();
  public static VoteManager voteManager = new VoteManager();

  static {
    greetings.add(new HelloGreeting("hello world!"));
    greetings.add(new HelloGreeting("goodbye world!"));
  }

  public HelloGreeting getGreeting(@Named("id") Integer id) {
    return greetings.get(id);
  }

  @ApiMethod(name = "greetings.multiply", httpMethod = "post")
  public HelloGreeting insertGreeting(@Named("times") Integer times, HelloGreeting greeting) {
    HelloGreeting response = new HelloGreeting();
    StringBuilder responseBuilder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      responseBuilder.append(greeting.getMessage());
    }
    response.setMessage(responseBuilder.toString());
    return response;
  }

  @ApiMethod(name = "greetings.authed", path = "greeting/authed")
  public HelloGreeting authedGreeting(User user) {
    HelloGreeting response = (user == null) ? new HelloGreeting("You didn't log in!")
        : new HelloGreeting("hello " + user.getEmail());
    return response;
  }

  // This should at least be concurrent!!
  public static Map<String, Vote> votes = new HashMap<>();

  public void updateVote(User user) {
    voteManager.createVote(new Vote(1, user.getEmail()));

//    if (votes.containsKey(user.getEmail())) {
//      Vote oldVote = votes.get(user.getEmail());
//      oldVote.setCount(oldVote.getCount() + 1);
//    } else {
//      votes.put(user.getEmail(), new Vote(1, user.getEmail())); 
//    }
  }

  public Collection<Vote> listVote() {
    return voteManager.readVotes();
    //    return votes.values();
  }
}
