package ro.munteanu.insight;

public class Vote {

  public int count;
  public String voter;

  public Vote() {}

  public Vote(int count, String voter) {
    this.count = count;
    this.voter = voter;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getVoter() {
    return voter;
  }

  public void setVoter(String voter) {
    this.voter = voter;
  }
}
