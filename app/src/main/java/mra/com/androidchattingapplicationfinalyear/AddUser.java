package mra.com.androidchattingapplicationfinalyear;

/**
 * Created by mr. A on 03-01-2019.
 */

public class AddUser
{
    public String username,fullname,email,imgurl,id,status,search,phonenumber,lock;

    public AddUser()
    {

    }

    public AddUser(String username, String fullname, String email, String imgurl, String id, String status, String search,String phonenumber,String lock) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.imgurl = imgurl;
        this.id = id;
        this.status = status;
        this.search = search;
        this.phonenumber=phonenumber;
        this.lock=lock;


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String lastname) {
        this.fullname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
