package mra.com.androidchattingapplicationfinalyear;

/**
 * Created by mr. A on 02-03-2019.
 */

public class SetPass
{
    String id,lockstatus;

    public SetPass()
    {}

    public SetPass(String id, String lockstatus) {
        this.id = id;
        this.lockstatus = lockstatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLockstatus() {
        return lockstatus;
    }

    public void setLockstatus(String lockstatus) {
        this.lockstatus = lockstatus;
    }
}
