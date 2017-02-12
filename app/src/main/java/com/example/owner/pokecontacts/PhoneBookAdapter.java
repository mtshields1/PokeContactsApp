package com.example.owner.pokecontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Owner on 11/29/2016.
 */

public class PhoneBookAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Android_Contact> mListPhoneBook;
    private HashMap<Integer, String> pokeList = new HashMap<Integer, String>();  //this hashmap will map random integers to their respective pokemon number for contact avatars
    private StringBuilder avatarString = new StringBuilder();   //this will hold the avatar name for the contact

    public PhoneBookAdapter(Context context, ArrayList<Android_Contact> list)
    {
        mContext = context;
        mListPhoneBook = list;
    }

    @Override
    public int getCount()
    {
        return mListPhoneBook.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mListPhoneBook.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Android_Contact entry = mListPhoneBook.get(position);

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.phonebook_row, null);
        }

        //-----< Retrieve the contact's pokedex number to set the contact's avatar >-----
        int pokedexNumber = entry.getPokemonAvatarNumber();
        String pokemonName = pokeList.get(pokedexNumber);
        avatarString.append(pokemonName + ".gif");

        //-----< The following code sets the pokemon avatar gif image for the contact >-----
        WebView wv = (WebView)convertView.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/" + avatarString.toString());  //use the avatar string to retrieve the gif image for this contact's avatar
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.setClickable(false);
        wv.setLongClickable(false);
        wv.setFocusable(false);
        wv.setFocusableInTouchMode(false);

        avatarString.setLength(0);     //clear the stringbuilder

        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getmName());

        TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getmPhone());

        return convertView;
    }
    
    public void setPokemon(){
        pokeList.put(1, "bulbasaur");
        pokeList.put(2, "ivysaur");
        pokeList.put(3, "venusaur");
        pokeList.put(4, "charmander");
        pokeList.put(5, "charmeleon");
        pokeList.put(6, "charizard");
        pokeList.put(7, "squirtle");
        pokeList.put(8, "wartortle");
        pokeList.put(9, "blastoise");
    }
}
