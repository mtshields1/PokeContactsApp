package com.example.owner.pokecontacts;

/**
 * Created by Owner on 11/29/2016.
 */

import android.graphics.Bitmap;
import android.webkit.WebView;

import java.io.Serializable;

public class Android_Contact implements Serializable
{
    //private Bitmap mAvatar;
    protected WebView mAvatar = null;
    protected String android_contact_name = "";
    protected String android_contact_number = "";
    protected int pokemonAvatarNumber = 0;  //0 as default. 0 is not a possible random number

    public Android_Contact()
    {
        //no op
    }

    /*public Bitmap getmAvatar()
    {
        return mAvatar;
    }*/

    public void setmAvatar(Bitmap mAvatar)
    {
        //this.mAvatar = mAvatar;
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
    
    public int getPokemonAvatarNumber () 
    {
        return pokemonAvatarNumber; 
    }
    
    public void setAvatarNum (int num)
    {
        if (pokemonAvatarNumber == 0) 
        {
            pokemonAvatarNumber = num;
        }
    }

    public void changeAvatarNum(int num)
    {
        pokemonAvatarNumber = num;
    }

}
