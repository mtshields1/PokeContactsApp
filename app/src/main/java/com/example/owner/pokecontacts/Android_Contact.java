package com.example.owner.pokecontacts;

/**
 * Created by Owner on 11/29/2016.
 */

import android.graphics.Bitmap;

public class Android_Contact
{
    private Bitmap mAvatar;
    protected String android_contact_name = "";
    protected String android_contact_number = "";
    protected int android_contact_ID = 0;

    public Android_Contact()
    {
        //no op
    }

    public Bitmap getmAvatar()
    {
        return mAvatar;
    }

    public void setmAvatar(Bitmap mAvatar)
    {
        this.mAvatar = mAvatar;
    }

    public String getmName()
    {
        return android_contact_name;
    }

    public void setmName(String mName)
    {
        android_contact_name = mName;
    }

    public String getmPhone()
    {
        return android_contact_number;
    }

    public void setmPhone(String mPhone)
    {
        android_contact_number = mPhone;
    }

}
