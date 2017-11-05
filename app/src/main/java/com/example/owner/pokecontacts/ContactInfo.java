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
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;

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
    private LinkedHashMap<Integer, String> typeList = new LinkedHashMap<>();  //this hashmap will keep the typing for each pokemon

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setPokemon();
        setTypes();
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

            //-----< Set up the click listener for the get type button >-----
            FloatingActionButton typeButton = (FloatingActionButton) findViewById(R.id.typebutton);
            typeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getTypes();
                }
            });
        }
    }

    public void getTypes() {
        final Dialog settingsDialog = new Dialog(ContactInfo.this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.popup_image, null));
        Button dismiss = (Button) settingsDialog.findViewById(R.id.closeButton);
        String typeString = typeList.get(current_contact.getPokemonAvatarNumber());
        ImageView type1 = (ImageView) settingsDialog.findViewById(R.id.type_pure);
        ImageView type2 = (ImageView) settingsDialog.findViewById(R.id.type_secondary);
        setTypeImages(type1, type2, typeString);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                settingsDialog.dismiss();
            }
        });
        settingsDialog.show();
    }

    public void setTypeImages(ImageView type1, ImageView type2, String typeString) {
        String[] splitTypes = typeString.split(",");
        for (int ind = 0; ind < splitTypes.length; ind++){
            if (splitTypes[ind].equals("none")) { break; }
            else if (splitTypes[ind].equals("bug")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.bug); }
                else { type2.setImageResource(R.mipmap.bug); }
            }
            else if (splitTypes[ind].equals("dark")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.dark); }
                else { type2.setImageResource(R.mipmap.dark); }
            }
            else if (splitTypes[ind].equals("dragon")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.dragon); }
                else { type2.setImageResource(R.mipmap.dragon); }
            }
            else if (splitTypes[ind].equals("electric")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.electric); }
                else { type2.setImageResource(R.mipmap.electric); }
            }
            else if (splitTypes[ind].equals("fairy")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.fairy); }
                else { type2.setImageResource(R.mipmap.fairy); }
            }
            else if (splitTypes[ind].equals("fight")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.fight); }
                else { type2.setImageResource(R.mipmap.fight); }
            }
            else if (splitTypes[ind].equals("fire")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.fire); }
                else { type2.setImageResource(R.mipmap.fire); }
            }
            else if (splitTypes[ind].equals("flying")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.flying); }
                else { type2.setImageResource(R.mipmap.flying); }
            }
            else if (splitTypes[ind].equals("ghost")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.ghost); }
                else { type2.setImageResource(R.mipmap.ghost); }
            }
            else if (splitTypes[ind].equals("grass")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.grass); }
                else { type2.setImageResource(R.mipmap.grass); }
            }
            else if (splitTypes[ind].equals("ground")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.ground); }
                else { type2.setImageResource(R.mipmap.ground); }
            }
            else if (splitTypes[ind].equals("ice")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.ice); }
                else { type2.setImageResource(R.mipmap.ice); }
            }
            else if (splitTypes[ind].equals("normal")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.normal); }
                else { type2.setImageResource(R.mipmap.normal); }
            }
            else if (splitTypes[ind].equals("poison")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.poison); }
                else { type2.setImageResource(R.mipmap.poison); }
            }
            else if (splitTypes[ind].equals("psychic")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.psychic); }
                else { type2.setImageResource(R.mipmap.psychic); }
            }
            else if (splitTypes[ind].equals("rock")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.rock); }
                else { type2.setImageResource(R.mipmap.rock); }
            }
            else if (splitTypes[ind].equals("steel")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.steel); }
                else { type2.setImageResource(R.mipmap.steel); }
            }
            else if (splitTypes[ind].equals("water")) {
                if (ind == 0) { type1.setImageResource(R.mipmap.water); }
                else { type2.setImageResource(R.mipmap.water); }
            }
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
        else if (item.getTitle() == "caterpie")
        {
            current_contact.changeAvatarNum(10);
        }
        else if (item.getTitle() == "metapod")
        {
            current_contact.changeAvatarNum(11);
        }
        else if (item.getTitle() == "butterfree")
        {
            current_contact.changeAvatarNum(12);
        }
        else if (item.getTitle() == "weedle")
        {
            current_contact.changeAvatarNum(13);
        }
        else if (item.getTitle() == "kakuna")
        {
            current_contact.changeAvatarNum(14);
        }
        else if (item.getTitle() == "beedrill")
        {
            current_contact.changeAvatarNum(15);
        }
        else if (item.getTitle() == "pidgey")
        {
            current_contact.changeAvatarNum(16);
        }
        else if (item.getTitle() == "pidgeotto")
        {
            current_contact.changeAvatarNum(17);
        }
        else if (item.getTitle() == "pidgeot")
        {
            current_contact.changeAvatarNum(18);
        }
        else if (item.getTitle() == "rattata")
        {
            current_contact.changeAvatarNum(19);
        }
        else if (item.getTitle() == "raticate")
        {
            current_contact.changeAvatarNum(20);
        }
        else if (item.getTitle() == "spearow")
        {
            current_contact.changeAvatarNum(21);
        }
        else if (item.getTitle() == "fearow")
        {
            current_contact.changeAvatarNum(22);
        }
        else if (item.getTitle() == "ekans")
        {
            current_contact.changeAvatarNum(23);
        }
        else if (item.getTitle() == "arbok")
        {
            current_contact.changeAvatarNum(24);
        }
        else if (item.getTitle() == "pikachu")
        {
            current_contact.changeAvatarNum(25);
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
        pokeList.put(10, "caterpie");
        pokeList.put(11, "metapod");
        pokeList.put(12, "butterfree");
        pokeList.put(13, "weedle");
        pokeList.put(14, "kakuna");
        pokeList.put(15, "beedrill");
        pokeList.put(16, "pidgey");
        pokeList.put(17, "pidgeotto");
        pokeList.put(18, "pidgeot");
        pokeList.put(19, "rattata");
        pokeList.put(20, "raticate");
        pokeList.put(21, "spearow");
        pokeList.put(22, "fearow");
        pokeList.put(23, "ekans");
        pokeList.put(24, "arbok");
        pokeList.put(25, "pikachu");
    }

    public void setTypes() {
        typeList.put(1, "grass,poison");
        typeList.put(2, "grass,poison");
        typeList.put(3, "grass,poison");
        typeList.put(4, "fire,none");
        typeList.put(5, "fire,none");
        typeList.put(6, "fire,flying");
        typeList.put(7, "water,none");
        typeList.put(8, "water,none");
        typeList.put(9, "water,none");
        typeList.put(10, "bug,none");
        typeList.put(11, "bug,none");
        typeList.put(12, "bug,flying");
        typeList.put(13, "bug,poison");
        typeList.put(14, "bug,poison");
        typeList.put(15, "bug,poison");
        typeList.put(16, "normal,flying");
        typeList.put(17, "normal,flying");
        typeList.put(18, "normal,flying");
        typeList.put(19, "normal,none");
        typeList.put(20, "normal,none");
        typeList.put(21, "normal,flying");
        typeList.put(22, "normal,flying");
        typeList.put(23, "poison,none");
        typeList.put(24, "poison,none");
        typeList.put(25, "electric,none");
    }
}
