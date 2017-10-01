package com.example.owner.pokecontacts;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContactInfo extends AppCompatActivity
{
    protected Android_Contact current_contact;
    private LinkedHashMap<Integer, String> pokeList = new LinkedHashMap<Integer, String>();  //this hashmap will map random integers to their respective pokemon number for contact avatars

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setPokemon();
        displayContactInfo();
    }

    public void displayContactInfo()
    {
        Bundle contactBundle = this.getIntent().getExtras();
        if (contactBundle != null) {
            final Android_Contact curr_contact = (Android_Contact) contactBundle.getSerializable("current_contact");
            current_contact = curr_contact;

            //-----< Set the pokemon avatar >------
            setAvatar(curr_contact);

            //-----< Display the contact's name below their avatar >-----
            TextView contactText = (TextView)findViewById(R.id.contactView);
            contactText.setText(curr_contact.getmName());

            //-----< Set up the click listener for the call button >-----
            FloatingActionButton callButton = (FloatingActionButton) findViewById(R.id.callbutton);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", curr_contact.getmPhone(), null));
                    startActivity(intent);
                }
            });

            //-----< Set up the click listener for the text button >-----
            FloatingActionButton textButton = (FloatingActionButton) findViewById(R.id.textbutton);
            textButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + curr_contact.getmPhone()));
                    startActivity(intent);
                }
            });

            //-----< Set up the click listener for the change pokemon button >-----
            FloatingActionButton changePokemonButton = (FloatingActionButton) findViewById(R.id.changePokemonButton);
            changePokemonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerForContextMenu(view);
                    view.showContextMenu();
                }
            });

            //-----< Set up the click listener for the cry button >-----
            FloatingActionButton cryButton = (FloatingActionButton) findViewById(R.id.crybutton);
            cryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuilder cryString = new StringBuilder();
                    int pokedexNumber = curr_contact.getPokemonAvatarNumber();
                    String pokemonName = pokeList.get(pokedexNumber);
                    cryString.append(pokemonName + ".ogg");

                    //-----< Set up the media player to play the sound file >-----
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        AssetFileDescriptor afd = getAssets().openFd("Cries/" + cryString.toString());
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                    }
                    catch (Exception e){}
                }
            });
        }
    }

    public void setAvatar(Android_Contact curr_contact){
        //-----< Get the contact's pokedex number for their avatar and create the name of the gif file >-----
        StringBuilder avatarString = new StringBuilder();
        int pokedexNumber = curr_contact.getPokemonAvatarNumber();
        String pokemonName = pokeList.get(pokedexNumber);
        avatarString.append(pokemonName + ".gif");

        //-----< Create the Drawee view that will show the contact's avatar, a gif file >-----
        SimpleDraweeView draweeView = (SimpleDraweeView)findViewById(R.id.gifView);
        Uri uri = Uri.parse("asset:///3D/" + avatarString.toString());
        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)
                        .build();

        draweeView.setController(controller);

        avatarString.setLength(0);     //clear the stringbuilder
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Pokemon to change to");
        for (Integer key: pokeList.keySet()){
            menu.add(0, v.getId(), 0, pokeList.get(key));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getTitle() == "bulbasaur")
        {
            current_contact.changeAvatarNum(1);
        }
        else if (item.getTitle() == "ivysaur")
        {
            current_contact.changeAvatarNum(2);
        }
        else if (item.getTitle() == "venusaur")
        {
            current_contact.changeAvatarNum(3);
        }
        else if (item.getTitle() == "charmander")
        {
            current_contact.changeAvatarNum(4);
        }
        else if (item.getTitle() == "charmeleon")
        {
            current_contact.changeAvatarNum(5);
        }
        else if (item.getTitle() == "charizard")
        {
            current_contact.changeAvatarNum(6);
        }
        else if (item.getTitle() == "squirtle")
        {
            current_contact.changeAvatarNum(7);
        }
        else if (item.getTitle() == "wartortle")
        {
            current_contact.changeAvatarNum(8);
        }
        else if (item.getTitle() == "blastoise")
        {
            current_contact.changeAvatarNum(9);
        }
        else
        {
            return false;
        }
        setAvatar(current_contact);
        return true;
    }

    @Override
    public void onBackPressed() {
        //-----< Make an intent to send data between activities when a contact is selected >-----
        Intent sendIntent = new Intent();
        sendIntent.putExtra("changed_contact", current_contact);
        setResult(RESULT_OK, sendIntent);
        finish();
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
