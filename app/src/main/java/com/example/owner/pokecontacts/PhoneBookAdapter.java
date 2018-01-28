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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import android.net.Uri;

import android.webkit.WebView;
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
        /*WebView wv = (WebView)convertView.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/" + avatarString.toString());  //use the avatar string to retrieve the gif image for this contact's avatar
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.setClickable(false);
        wv.setLongClickable(false);
        wv.setFocusable(false);
        wv.setFocusableInTouchMode(false);*/ //this code is for non fresco gif usage

        SimpleDraweeView draweeView = (SimpleDraweeView)convertView.findViewById(R.id.my_image_view);
        Uri uri = Uri.parse("asset:///sprite/" + avatarString.toString());
        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)
                        .build();

        draweeView.setController(controller);

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
}
