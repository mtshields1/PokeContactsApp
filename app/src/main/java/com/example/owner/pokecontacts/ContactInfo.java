package com.example.owner.pokecontacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

public class ContactInfo extends AppCompatActivity
{
    private HashMap<Integer, String> pokeList = new HashMap<Integer, String>();  //this hashmap will map random integers to their respective pokemon number for contact avatars

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPokemon();
        doStuff();
    }

    public void doStuff()
    {
        Bundle contactBundle = this.getIntent().getExtras();
        if (contactBundle != null) {
            final Android_Contact curr_contact = (Android_Contact) contactBundle.getSerializable("current_contact");

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
        }
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
