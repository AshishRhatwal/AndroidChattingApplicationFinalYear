package mra.com.androidchattingapplicationfinalyear;

/**
 * Created by mr. A on 30-01-2019.
 */

public class Friendrequest
{
    String id,frndsent,frndresive;

   public Friendrequest()
   {}

    public Friendrequest(String id, String frndsent, String frndresive) {
        this.id = id;
        this.frndsent = frndsent;
        this.frndresive = frndresive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrndsent() {
        return frndsent;
    }

    public void setFrndsent(String frndsent) {
        this.frndsent = frndsent;
    }

    public String getFrndresive() {
        return frndresive;
    }

    public void setFrndresive(String frndresive) {
        this.frndresive = frndresive;
    }
}
