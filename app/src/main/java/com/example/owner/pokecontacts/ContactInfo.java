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
            else if (splitTypes[ind].equals("fighting")) {
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
        else if (item.getTitle() == "raichu")
        {
            current_contact.changeAvatarNum(26);
        }
        else if (item.getTitle() == "sandshrew")
        {
            current_contact.changeAvatarNum(27);
        }
        else if (item.getTitle() == "sandslash")
        {
            current_contact.changeAvatarNum(28);
        }
        else if (item.getTitle() == "nidoran-female")
        {
            current_contact.changeAvatarNum(29);
        }
        else if (item.getTitle() == "nidorina")
        {
            current_contact.changeAvatarNum(30);
        }
        else if (item.getTitle() == "nidoqueen")
        {
            current_contact.changeAvatarNum(31);
        }
        else if (item.getTitle() == "nidoran-male")
        {
            current_contact.changeAvatarNum(32);
        }
        else if (item.getTitle() == "nidorino")
        {
            current_contact.changeAvatarNum(33);
        }
        else if (item.getTitle() == "nidoking")
        {
            current_contact.changeAvatarNum(34);
        }
        else if (item.getTitle() == "clefairy")
        {
            current_contact.changeAvatarNum(35);
        }
        else if (item.getTitle() == "clefable")
        {
            current_contact.changeAvatarNum(36);
        }
        else if (item.getTitle() == "vulpix")
        {
            current_contact.changeAvatarNum(37);
        }
        else if (item.getTitle() == "ninetales")
        {
            current_contact.changeAvatarNum(38);
        }
        else if (item.getTitle() == "jigglypuff")
        {
            current_contact.changeAvatarNum(39);
        }
        else if (item.getTitle() == "wigglytuff")
        {
            current_contact.changeAvatarNum(40);
        }
        else if (item.getTitle() == "zubat")
        {
            current_contact.changeAvatarNum(41);
        }
        else if (item.getTitle() == "golbat")
        {
            current_contact.changeAvatarNum(42);
        }
        else if (item.getTitle() == "oddish")
        {
            current_contact.changeAvatarNum(43);
        }
        else if (item.getTitle() == "gloom")
        {
            current_contact.changeAvatarNum(44);
        }
        else if (item.getTitle() == "vileplume")
        {
            current_contact.changeAvatarNum(45);
        }
        else if (item.getTitle() == "paras")
        {
            current_contact.changeAvatarNum(46);
        }
        else if (item.getTitle() == "parasect")
        {
            current_contact.changeAvatarNum(47);
        }
        else if (item.getTitle() == "venonat")
        {
            current_contact.changeAvatarNum(48);
        }
        else if (item.getTitle() == "venomoth")
        {
            current_contact.changeAvatarNum(49);
        }
        else if (item.getTitle() == "diglett")
        {
            current_contact.changeAvatarNum(50);
        }
        else if (item.getTitle() == "dugtrio")
        {
            current_contact.changeAvatarNum(51);
        }
        else if (item.getTitle() == "meowth")
        {
            current_contact.changeAvatarNum(52);
        }
        else if (item.getTitle() == "persian")
        {
            current_contact.changeAvatarNum(53);
        }
        else if (item.getTitle() == "psyduck")
        {
            current_contact.changeAvatarNum(54);
        }
        else if (item.getTitle() == "golduck")
        {
            current_contact.changeAvatarNum(55);
        }
        else if (item.getTitle() == "mankey")
        {
            current_contact.changeAvatarNum(56);
        }
        else if (item.getTitle() == "primeape")
        {
            current_contact.changeAvatarNum(57);
        }
        else if (item.getTitle() == "growlithe")
        {
            current_contact.changeAvatarNum(58);
        }
        else if (item.getTitle() == "arcanine")
        {
            current_contact.changeAvatarNum(59);
        }
        else if (item.getTitle() == "poliwag")
        {
            current_contact.changeAvatarNum(60);
        }
        else if (item.getTitle() == "poliwhirl")
        {
            current_contact.changeAvatarNum(61);
        }
        else if (item.getTitle() == "poliwrath")
        {
            current_contact.changeAvatarNum(62);
        }
        else if (item.getTitle() == "abra")
        {
            current_contact.changeAvatarNum(63);
        }
        else if (item.getTitle() == "kadabra")
        {
            current_contact.changeAvatarNum(64);
        }
        else if (item.getTitle() == "alakazam")
        {
            current_contact.changeAvatarNum(65);
        }
        else if (item.getTitle() == "machop")
        {
            current_contact.changeAvatarNum(66);
        }
        else if (item.getTitle() == "machoke")
        {
            current_contact.changeAvatarNum(67);
        }
        else if (item.getTitle() == "machamp")
        {
            current_contact.changeAvatarNum(68);
        }
        else if (item.getTitle() == "bellsprout")
        {
            current_contact.changeAvatarNum(69);
        }
        else if (item.getTitle() == "weepinbell")
        {
            current_contact.changeAvatarNum(70);
        }
        else if (item.getTitle() == "victreebel")
        {
            current_contact.changeAvatarNum(71);
        }
        else if (item.getTitle() == "tentacool")
        {
            current_contact.changeAvatarNum(72);
        }
        else if (item.getTitle() == "tentacruel")
        {
            current_contact.changeAvatarNum(73);
        }
        else if (item.getTitle() == "geodude")
        {
            current_contact.changeAvatarNum(74);
        }
        else if (item.getTitle() == "graveler")
        {
            current_contact.changeAvatarNum(75);
        }
        else if (item.getTitle() == "golem")
        {
            current_contact.changeAvatarNum(76);
        }
        else if (item.getTitle() == "ponyta")
        {
            current_contact.changeAvatarNum(77);
        }
        else if (item.getTitle() == "rapidash")
        {
            current_contact.changeAvatarNum(78);
        }
        else if (item.getTitle() == "slowpoke")
        {
            current_contact.changeAvatarNum(79);
        }
        else if (item.getTitle() == "slowbro")
        {
            current_contact.changeAvatarNum(80);
        }
        else if (item.getTitle() == "magnemite")
        {
            current_contact.changeAvatarNum(81);
        }
        else if (item.getTitle() == "magneton")
        {
            current_contact.changeAvatarNum(82);
        }
        else if (item.getTitle() == "farfetch'd")
        {
            current_contact.changeAvatarNum(83);
        }
        else if (item.getTitle() == "doduo")
        {
            current_contact.changeAvatarNum(84);
        }
        else if (item.getTitle() == "dodrio")
        {
            current_contact.changeAvatarNum(85);
        }
        else if (item.getTitle() == "seel")
        {
            current_contact.changeAvatarNum(86);
        }
        else if (item.getTitle() == "dewgong")
        {
            current_contact.changeAvatarNum(87);
        }
        else if (item.getTitle() == "grimer")
        {
            current_contact.changeAvatarNum(88);
        }
        else if (item.getTitle() == "muk")
        {
            current_contact.changeAvatarNum(89);
        }
        else if (item.getTitle() == "shellder")
        {
            current_contact.changeAvatarNum(90);
        }
        else if (item.getTitle() == "cloyster")
        {
            current_contact.changeAvatarNum(91);
        }
        else if (item.getTitle() == "gastly")
        {
            current_contact.changeAvatarNum(92);
        }
        else if (item.getTitle() == "haunter")
        {
            current_contact.changeAvatarNum(93);
        }
        else if (item.getTitle() == "gengar")
        {
            current_contact.changeAvatarNum(94);
        }
        else if (item.getTitle() == "onix")
        {
            current_contact.changeAvatarNum(95);
        }
        else if (item.getTitle() == "drowzee")
        {
            current_contact.changeAvatarNum(96);
        }
        else if (item.getTitle() == "hypno")
        {
            current_contact.changeAvatarNum(97);
        }
        else if (item.getTitle() == "krabby")
        {
            current_contact.changeAvatarNum(98);
        }
        else if (item.getTitle() == "kingler")
        {
            current_contact.changeAvatarNum(99);
        }
        else if (item.getTitle() == "voltorb")
        {
            current_contact.changeAvatarNum(100);
        }
        else if (item.getTitle() == "electrode")
        {
            current_contact.changeAvatarNum(101);
        }
        else if (item.getTitle() == "exeggcute")
        {
            current_contact.changeAvatarNum(102);
        }
        else if (item.getTitle() == "exeggutor")
        {
            current_contact.changeAvatarNum(103);
        }
        else if (item.getTitle() == "cubone")
        {
            current_contact.changeAvatarNum(104);
        }
        else if (item.getTitle() == "marowak")
        {
            current_contact.changeAvatarNum(105);
        }
        else if (item.getTitle() == "hitmonlee")
        {
            current_contact.changeAvatarNum(106);
        }
        else if (item.getTitle() == "hitmonchan")
        {
            current_contact.changeAvatarNum(107);
        }
        else if (item.getTitle() == "lickitung")
        {
            current_contact.changeAvatarNum(108);
        }
        else if (item.getTitle() == "koffing")
        {
            current_contact.changeAvatarNum(109);
        }
        else if (item.getTitle() == "weezing")
        {
            current_contact.changeAvatarNum(110);
        }
        else if (item.getTitle() == "rhyhorn")
        {
            current_contact.changeAvatarNum(111);
        }
        else if (item.getTitle() == "rhydon")
        {
            current_contact.changeAvatarNum(112);
        }
        else if (item.getTitle() == "chansey")
        {
            current_contact.changeAvatarNum(113);
        }
        else if (item.getTitle() == "tangela")
        {
            current_contact.changeAvatarNum(114);
        }
        else if (item.getTitle() == "kangaskhan")
        {
            current_contact.changeAvatarNum(115);
        }
        else if (item.getTitle() == "horsea")
        {
            current_contact.changeAvatarNum(116);
        }
        else if (item.getTitle() == "seadra")
        {
            current_contact.changeAvatarNum(117);
        }
        else if (item.getTitle() == "goldeen")
        {
            current_contact.changeAvatarNum(118);
        }
        else if (item.getTitle() == "seaking")
        {
            current_contact.changeAvatarNum(119);
        }
        else if (item.getTitle() == "staryu")
        {
            current_contact.changeAvatarNum(120);
        }
        else if (item.getTitle() == "starmie")
        {
            current_contact.changeAvatarNum(121);
        }
        else if (item.getTitle() == "mrmime")
        {
            current_contact.changeAvatarNum(122);
        }
        else if (item.getTitle() == "scyther")
        {
            current_contact.changeAvatarNum(123);
        }
        else if (item.getTitle() == "jynx")
        {
            current_contact.changeAvatarNum(124);
        }
        else if (item.getTitle() == "electabuzz")
        {
            current_contact.changeAvatarNum(125);
        }
        else if (item.getTitle() == "magmar")
        {
            current_contact.changeAvatarNum(126);
        }
        else if (item.getTitle() == "pinsir")
        {
            current_contact.changeAvatarNum(127);
        }
        else if (item.getTitle() == "tauros")
        {
            current_contact.changeAvatarNum(128);
        }
        else if (item.getTitle() == "magikarp")
        {
            current_contact.changeAvatarNum(129);
        }
        else if (item.getTitle() == "gyarados")
        {
            current_contact.changeAvatarNum(130);
        }
        else if (item.getTitle() == "lapras")
        {
            current_contact.changeAvatarNum(131);
        }
        else if (item.getTitle() == "ditto")
        {
            current_contact.changeAvatarNum(132);
        }
        else if (item.getTitle() == "eevee")
        {
            current_contact.changeAvatarNum(133);
        }
        else if (item.getTitle() == "vaporeon")
        {
            current_contact.changeAvatarNum(134);
        }
        else if (item.getTitle() == "jolteon")
        {
            current_contact.changeAvatarNum(135);
        }
        else if (item.getTitle() == "flareon")
        {
            current_contact.changeAvatarNum(136);
        }
        else if (item.getTitle() == "porygon")
        {
            current_contact.changeAvatarNum(137);
        }
        else if (item.getTitle() == "omanyte")
        {
            current_contact.changeAvatarNum(138);
        }
        else if (item.getTitle() == "omastar")
        {
            current_contact.changeAvatarNum(139);
        }
        else if (item.getTitle() == "kabuto")
        {
            current_contact.changeAvatarNum(140);
        }
        else if (item.getTitle() == "kabutops")
        {
            current_contact.changeAvatarNum(141);
        }
        else if (item.getTitle() == "aerodactyl")
        {
            current_contact.changeAvatarNum(142);
        }
        else if (item.getTitle() == "snorlax")
        {
            current_contact.changeAvatarNum(143);
        }
        else if (item.getTitle() == "articuno")
        {
            current_contact.changeAvatarNum(144);
        }
        else if (item.getTitle() == "zapdos")
        {
            current_contact.changeAvatarNum(145);
        }
        else if (item.getTitle() == "moltres")
        {
            current_contact.changeAvatarNum(146);
        }
        else if (item.getTitle() == "dratini")
        {
            current_contact.changeAvatarNum(147);
        }
        else if (item.getTitle() == "dragonair")
        {
            current_contact.changeAvatarNum(148);
        }
        else if (item.getTitle() == "dragonite")
        {
            current_contact.changeAvatarNum(149);
        }
        else if (item.getTitle() == "mewtwo")
        {
            current_contact.changeAvatarNum(150);
        }
        else if (item.getTitle() == "mew")
        {
            current_contact.changeAvatarNum(151);
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
        pokeList.put(26, "raichu");
        pokeList.put(27, "sandshrew");
        pokeList.put(28, "sandslash");
        pokeList.put(29, "nidoran-female");
        pokeList.put(30, "nidorina");
        pokeList.put(31, "nidoqueen");
        pokeList.put(32, "nidoran-male");
        pokeList.put(33, "nidorino");
        pokeList.put(34, "nidoking");
        pokeList.put(35, "clefairy");
        pokeList.put(36, "clefable");
        pokeList.put(37, "vulpix");
        pokeList.put(38, "ninetales");
        pokeList.put(39, "jigglypuff");
        pokeList.put(40, "wigglytuff");
        pokeList.put(41, "zubat");
        pokeList.put(42, "golbat");
        pokeList.put(43, "oddish");
        pokeList.put(44, "gloom");
        pokeList.put(45, "vileplume");
        pokeList.put(46, "paras");
        pokeList.put(47, "parasect");
        pokeList.put(48, "venonat");
        pokeList.put(49, "venomoth");
        pokeList.put(50, "diglett");
        pokeList.put(51, "dugtrio");
        pokeList.put(52, "meowth");
        pokeList.put(53, "persian");
        pokeList.put(54, "psyduck");
        pokeList.put(55, "golduck");
        pokeList.put(56, "mankey");
        pokeList.put(57, "primeape");
        pokeList.put(58, "growlithe");
        pokeList.put(59, "arcanine");
        pokeList.put(60, "poliwag");
        pokeList.put(61, "poliwhirl");
        pokeList.put(62, "poliwrath");
        pokeList.put(63, "abra");
        pokeList.put(64, "kadabra");
        pokeList.put(65, "alakazam");
        pokeList.put(66, "machop");
        pokeList.put(67, "machoke");
        pokeList.put(68, "machamp");
        pokeList.put(69, "bellsprout");
        pokeList.put(70, "weepinbell");
        pokeList.put(71, "victreebel");
        pokeList.put(72, "tentacool");
        pokeList.put(73, "tentacruel");
        pokeList.put(74, "geodude");
        pokeList.put(75, "graveler");
        pokeList.put(76, "golem");
        pokeList.put(77, "ponyta");
        pokeList.put(78, "rapidash");
        pokeList.put(79, "slowpoke");
        pokeList.put(80, "slowbro");
        pokeList.put(81, "magnemite");
        pokeList.put(82, "magneton");
        pokeList.put(83, "farfetch'd");
        pokeList.put(84, "doduo");
        pokeList.put(85, "dodrio");
        pokeList.put(86, "seel");
        pokeList.put(87, "dewgong");
        pokeList.put(88, "grimer");
        pokeList.put(89, "muk");
        pokeList.put(90, "shellder");
        pokeList.put(91, "cloyster");
        pokeList.put(92, "gastly");
        pokeList.put(93, "haunter");
        pokeList.put(94, "gengar");
        pokeList.put(95, "onix");
        pokeList.put(96, "drowzee");
        pokeList.put(97, "hypno");
        pokeList.put(98, "krabby");
        pokeList.put(99, "kingler");
        pokeList.put(100, "voltorb");
        pokeList.put(101, "electrode");
        pokeList.put(102, "exeggcute");
        pokeList.put(103, "exeggutor");
        pokeList.put(104, "cubone");
        pokeList.put(105, "marowak");
        pokeList.put(106, "hitmonlee");
        pokeList.put(107, "hitmonchan");
        pokeList.put(108, "lickitung");
        pokeList.put(109, "koffing");
        pokeList.put(110, "weezing");
        pokeList.put(111, "rhyhorn");
        pokeList.put(112, "rhydon");
        pokeList.put(113, "chansey");
        pokeList.put(114, "tangela");
        pokeList.put(115, "kangaskhan");
        pokeList.put(116, "horsea");
        pokeList.put(117, "seadra");
        pokeList.put(118, "goldeen");
        pokeList.put(119, "seaking");
        pokeList.put(120, "staryu");
        pokeList.put(121, "starmie");
        pokeList.put(122, "mrmime");
        pokeList.put(123, "scyther");
        pokeList.put(124, "jynx");
        pokeList.put(125, "electabuzz");
        pokeList.put(126, "magmar");
        pokeList.put(127, "pinsir");
        pokeList.put(128, "tauros");
        pokeList.put(129, "magikarp");
        pokeList.put(130, "gyarados");
        pokeList.put(131, "lapras");
        pokeList.put(132, "ditto");
        pokeList.put(133, "eevee");
        pokeList.put(134, "vaporeon");
        pokeList.put(135, "jolteon");
        pokeList.put(136, "flareon");
        pokeList.put(137, "porygon");
        pokeList.put(138, "omanyte");
        pokeList.put(139, "omastar");
        pokeList.put(140, "kabuto");
        pokeList.put(141, "kabutops");
        pokeList.put(142, "aerodactyl");
        pokeList.put(143, "snorlax");
        pokeList.put(144, "articuno");
        pokeList.put(145, "zapdos");
        pokeList.put(146, "moltres");
        pokeList.put(147, "dratini");
        pokeList.put(148, "dragonair");
        pokeList.put(149, "dragonite");
        pokeList.put(150, "mewtwo");
        pokeList.put(151, "mew");
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
        typeList.put(26, "electric,none");
        typeList.put(27, "ground,none");
        typeList.put(28, "ground,none");
        typeList.put(29, "poison,none");
        typeList.put(30, "poison,none");
        typeList.put(31, "poison,ground");
        typeList.put(32, "poison,none");
        typeList.put(33, "poison,none");
        typeList.put(34, "poison,ground");
        typeList.put(35, "fairy,none");
        typeList.put(36, "fairy,none");
        typeList.put(37, "fire,none");
        typeList.put(38, "fire,none");
        typeList.put(39, "normal,fairy");
        typeList.put(40, "normal,fairy");
        typeList.put(41, "poison,flying");
        typeList.put(42, "poison,flying");
        typeList.put(43, "grass,poison");
        typeList.put(44, "grass,poison");
        typeList.put(45, "grass,poison");
        typeList.put(46, "bug,grass");
        typeList.put(47, "bug,grass");
        typeList.put(48, "bug,poison");
        typeList.put(49, "bug,poison");
        typeList.put(50, "ground,none");
        typeList.put(51, "ground,none");
        typeList.put(52, "normal,none");
        typeList.put(53, "normal,none");
        typeList.put(54, "water,none");
        typeList.put(55, "water,none");
        typeList.put(56, "fighting,none");
        typeList.put(57, "fighting,none");
        typeList.put(58, "fire,none");
        typeList.put(59, "fire,none");
        typeList.put(60, "water,none");
        typeList.put(61, "water,none");
        typeList.put(62, "water,fighting");
        typeList.put(63, "psychic,none");
        typeList.put(64, "psychic,none");
        typeList.put(65, "psychic,none");
        typeList.put(66, "fighting,none");
        typeList.put(67, "fighting,none");
        typeList.put(68, "fighting,none");
        typeList.put(69, "grass,poison");
        typeList.put(70, "grass,poison");
        typeList.put(71, "grass,poison");
        typeList.put(72, "water,poison");
        typeList.put(73, "water,poison");
        typeList.put(74, "rock,ground");
        typeList.put(75, "rock,ground");
        typeList.put(76, "rock,ground");
        typeList.put(77, "fire,none");
        typeList.put(78, "fire,none");
        typeList.put(79, "water,psychic");
        typeList.put(80, "water,psychic");
        typeList.put(81, "electric,steel");
        typeList.put(82, "electric,steel");
        typeList.put(83, "normal,flying");
        typeList.put(84, "normal,flying");
        typeList.put(85, "normal,flying");
        typeList.put(86, "water,none");
        typeList.put(87, "water,ice");
        typeList.put(88, "poison,none");
        typeList.put(89, "poison,none");
        typeList.put(90, "water,none");
        typeList.put(91, "water,ice");
        typeList.put(92, "ghost,poison");
        typeList.put(93, "ghost,poison");
        typeList.put(94, "ghost,poison");
        typeList.put(95, "rock,ground");
        typeList.put(96, "psychic,none");
        typeList.put(97, "psychic,none");
        typeList.put(98, "water,none");
        typeList.put(99, "water,none");
        typeList.put(100, "electric,none");
        typeList.put(101, "electric,none");
        typeList.put(102, "grass,psychic");
        typeList.put(103, "grass,psychic");
        typeList.put(104, "ground,none");
        typeList.put(105, "ground,none");
        typeList.put(106, "fighting,none");
        typeList.put(107, "fighting,none");
        typeList.put(108, "normal,none");
        typeList.put(109, "poison,none");
        typeList.put(110, "poison,none");
        typeList.put(111, "ground,rock");
        typeList.put(112, "ground,rock");
        typeList.put(113, "normal,none");
        typeList.put(114, "grass,none");
        typeList.put(115, "normal,none");
        typeList.put(116, "water,none");
        typeList.put(117, "water,none");
        typeList.put(118, "water,none");
        typeList.put(119, "water,none");
        typeList.put(120, "water,none");
        typeList.put(121, "water,psychic");
        typeList.put(122, "psychic,none");
        typeList.put(123, "bug,flying");
        typeList.put(124, "ice,psychic");
        typeList.put(125, "electric,none");
        typeList.put(126, "fire,none");
        typeList.put(127, "bug,none");
        typeList.put(128, "normal,none");
        typeList.put(129, "water,none");
        typeList.put(130, "water,flying");
        typeList.put(131, "water,ice");
        typeList.put(132, "normal,none");
        typeList.put(133, "normal,none");
        typeList.put(134, "water,none");
        typeList.put(135, "electric,none");
        typeList.put(136, "fire,none");
        typeList.put(137, "normal,none");
        typeList.put(138, "rock,water");
        typeList.put(139, "rock,water");
        typeList.put(140, "rock,water");
        typeList.put(141, "rock,water");
        typeList.put(142, "rock,flying");
        typeList.put(143, "normal,none");
        typeList.put(144, "ice,flying");
        typeList.put(145, "electric,flying");
        typeList.put(146, "fire,flying");
        typeList.put(147, "dragon,none");
        typeList.put(148, "dragon,none");
        typeList.put(149, "dragon,flying");
        typeList.put(150, "psychic,none");
        typeList.put(151, "psychic,none");
    }
}
