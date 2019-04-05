package mra.com.androidchattingapplicationfinalyear;

/**
 * Created by mr. A on 25-01-2019.
 */

public class feedback
{
    String id,name,contact,comment;

    public feedback()
    {
    }

    public feedback(String id, String name, String contact, String comment) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getComment() {
        return comment;
    }
}
