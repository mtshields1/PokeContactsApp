package com.example.owner.pokecontacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import java.util.*;
import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.content.Intent;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.content.Context;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class MainActivity extends Activity
{

    private ListView lvPhone;
    protected String previousName = "";
    protected String previousNum = "";
    protected  Android_Contact curr_selected = null;  //This will be for calling, texting, etc with the user selected contact
    protected static final HashMap<String, Integer> savedAvatarNums = new HashMap<String, Integer>(); //For saving contact's avatars. Key will be contact phone number, value will be pokedex number
    private Random rand = new Random();   //for getting random pokemon avatar outcomes at initialization
    private int low = 1;    //minimum pokedex number for random seed
    private int high = 26;   //maximum pokedex number (exclusive) for random seed. This should be 1 higher than maximum seed number
    //The following variables are each for each letter's clicklistener in the vertical alphabet at left
    private TextView clickA;
    private TextView clickB;
    private TextView clickC;
    private TextView clickD;
    private TextView clickE;
    private TextView clickF;
    private TextView clickG;
    private TextView clickH;
    private TextView clickI;
    private TextView clickJ;
    private TextView clickK;
    private TextView clickL;
    private TextView clickM;
    private TextView clickN;
    private TextView clickO;
    private TextView clickP;
    private TextView clickQ;
    private TextView clickR;
    private TextView clickS;
    private TextView clickT;
    private TextView clickU;
    private TextView clickV;
    private TextView clickW;
    private TextView clickX;
    private TextView clickY;
    private TextView clickZ;
    protected Map<Character, Integer> contactLocations = new HashMap<>();  //This map will keep locations on the starting index of each character letter (a, b, c) in the listview

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        
        //---< App has been used before. Recover the save state >---
        if (savedInstanceState != null)
        {
            super.onRestoreInstanceState(savedInstanceState);
            //HashMap<String, Integer> savedAvatarNums = (HashMap<String, Integer>) savedInstanceState.getSerializable("saveData");
        }
        else
        {
            //---< Enter this else either if the app hasn't been used before, or if it has >--
            //---< been used bfore, but was either killed by the system/user or the phone >--
            //---< was restarted. See the onPause method for motre details >--
            try
            {
                String FILENAME = "pokefile";
                FileInputStream fos = openFileInput(FILENAME);
                ObjectInputStream in = new ObjectInputStream(fos);
                HashMap<String, Integer> newMap = (HashMap<String, Integer>) in.readObject();
                savedAvatarNums.putAll(newMap);
                in.close();
                fos.close();
            }
            catch (java.io.IOException ioe)
            {

            }
            catch (ClassNotFoundException cnfe)
            {

            }
        }
        
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1);

    }
    
    //---< This method is invoked upon leaving the app in several instances. >--
    //---< This particular method call and File creation is for when the app >--
    //---< is either killed by the system/user or when restarting the phone. >--
    //---< The savedAvatarNums hashmap is written to the pokefile through a >--
    //---< ObjectOutputStream. Then, in onCreate, if savedInstanceState is >--
    //---< null, this file is attempted to be open to recover the hashmap instead >--
    @Override
    public void onPause()
    {
        try
        {
            String FILENAME = "pokefile";
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(savedAvatarNums);
            out.close();
            fos.close();
        }
        catch (java.io.IOException ioe)
        {

        }
        super.onPause();
    }
    
    //---< This method is invoked upon pressing the back button or switching apps -->
    @Override
    public void onStop()
    {
        getIntent().putExtra("theMap", savedAvatarNums);
        super.onStop();
    }

    //---< Restore save data when resuming the app; the complement to onStop >---
    @Override
    public void onResume()
    {
        super.onResume();
        HashMap<String, Integer> savedAvatarNums = (HashMap<String, Integer>)getIntent().getSerializableExtra("theMap");
    }
    
    //----< This method is called upon the app activity stopping and stores >-----
    //----< Needed save state information. This method overrides another saveInstanceState >----
    //----< that needs to be called, the super call >-----
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable("saveData", savedAvatarNums);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showContacts();
                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void showContacts()
    {
        lvPhone = (ListView)findViewById(R.id.listPhone);
        
        //-----< Initialize the map that will be used for finding contact's that start with certain letters quickly >-----
        for (char ch = 'A'; ch <= 'Z'; ch++)
        {
            contactLocations.put(ch, -1);
        }

        //-----< This arraylist will contain information for each contact >-----
        ArrayList<Android_Contact> android_contact_data = new ArrayList<Android_Contact>();

        //-----< Now get the contacts >-----
        Cursor cursor_android_contacts = null;

        //-----< This will give all contacts >-----
        cursor_android_contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor_android_contacts.getCount() > 0)
        {
            int currIndex = 0; //This will be used to keep track of where each contact is located
            
            while (cursor_android_contacts.moveToNext())
            {
                //-----< create a new android contact object for retrieval >-----
                Android_Contact theContact = new Android_Contact();

                //-----< ensure that the contact has a phone number >-----
                int hasPhoneNumber = Integer.parseInt(cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0)
                {
                    //<----< retrieve and add the contact's name to the contact object >----
                    String contactDisplayName = cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    theContact.android_contact_name = contactDisplayName;
                    
                    //-----< Attempt to add the index to the first letter of this contact if it is the first of this contact that starts with this letter >-----
                    char startingChar = contactDisplayName.charAt(0);
                    if (contactLocations.get(startingChar) == -1)
                    {
                        contactLocations.put(startingChar, currIndex);
                    }
                    
                    //----< retrieve and add the number to the contact object >-----
                    String number = cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    theContact.android_contact_number = number.replaceAll("\\D+","");
                    
                    if (savedAvatarNums.containsKey(number.replaceAll("\\D", "")))  //This contact has existed before. Get their pokedex number
                    {
                        //theContact.setAvatarNum(savedAvatarNums.get(number.replaceAll("\\D", "")));
                        theContact.changeAvatarNum(savedAvatarNums.get(number.replaceAll("\\D", "")));
                    }
                    else
                    {
                        //<----< get a random number for the contact's pokemon avatar and put the number into the saveData map >----
                        int pokedexNumber = rand.nextInt(high-low) + low;   //this ensures a random pokemon avatar outcome
                        theContact.setAvatarNum(pokedexNumber);
                        savedAvatarNums.put(number.replaceAll("\\D", ""), pokedexNumber);
                    }

                    //-----< add the contact to the contact arraylist if it wasn't added previously >-----
                    if (!contactDisplayName.equals(previousName))
                    {
                        android_contact_data.add(theContact);
                        currIndex++;  //Moving onto the next contact, so increase the index count
                    }
                    previousName = contactDisplayName;
                    previousNum = number.replaceAll("\\D+","");
                }
            }
        }

        final PhoneBookAdapter adapter = new PhoneBookAdapter(this, android_contact_data);
        adapter.setPokemon();      //this will set the possible pokemon avatars
        lvPhone.setAdapter(adapter);
        
        //-----< To implement the vertical alphabet at right for selecting a row of >----
        //-----< contacts that start with that letter quickly, make the top level parent >----
        //-----< view a view group. Then, inflate the new view to implement on top of the >----
        //-----< already created view, phonebook_row, and add the view to the parent >----
        ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.vertical_alphabet, vg,false);
        vg.addView(menuLayout, 1);
        
        //-----< Register a context menu for the list view when a contact is selected >-----
        registerForContextMenu(lvPhone);

        //-----< set an action on clicking an item >-----
        lvPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Android_Contact selected_contact = (Android_Contact) adapter.getItem(position);
                curr_selected = selected_contact;

                //-----< Make an intent to send data between activities when a contact is selected >-----
                Intent sendIntent = new Intent();
                Bundle contactBundle = new Bundle();
                contactBundle.putSerializable("current_contact", curr_selected);
                sendIntent.putExtras(contactBundle);
                sendIntent.setClass(getApplicationContext(), ContactInfo.class);

                //----< This will return the result, in the form of the changed contact avatar (if the avatar was changed) >----
                startActivityForResult(sendIntent, 1);
            }
        });
        initializeClickListeners();  //Initialize every clicklistener for the vertical alphabet
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK)
            {
                final Android_Contact curr_contact = (Android_Contact) data.getSerializableExtra("changed_contact");
                savedAvatarNums.put(curr_contact.getmPhone(), curr_contact.getPokemonAvatarNumber());
                showContacts();
                //TODO: may implement changing the view to go back to where this contact was selected. i.e. for now,
                //TODO: if the user selects a D contact, showContacts will bring it back to the top of the contact list
            }
        }
    }
    
    public void initializeClickListeners()
    {
        clickA = (TextView) findViewById(R.id.letterA);
        clickA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('A') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('A'), 0);
                }
            }
        });
        clickB = (TextView) findViewById(R.id.letterB);
        clickB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('B') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('B'), 0);
                }
            }
        });
        clickC = (TextView) findViewById(R.id.letterC);
        clickC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('C') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('C'), 0);
                }
            }
        });
        clickD = (TextView) findViewById(R.id.letterD);
        clickD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('D') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('D'), 0);
                }
            }
        });
        clickE = (TextView) findViewById(R.id.letterE);
        clickE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('E') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('E'), 0);
                }
            }
        });
        clickF = (TextView) findViewById(R.id.letterF);
        clickF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('F') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('F'), 0);
                }
            }
        });
        clickG = (TextView) findViewById(R.id.letterG);
        clickG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('G') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('G'), 0);
                }
            }
        });
        clickH = (TextView) findViewById(R.id.letterH);
        clickH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('H') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('H'), 0);
                }
            }
        });
        clickI = (TextView) findViewById(R.id.letterI);
        clickI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('I') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('I'), 0);
                }
            }
        });
        clickJ = (TextView) findViewById(R.id.letterJ);
        clickJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('J') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('J'), 0);
                }
            }
        });
        clickK = (TextView) findViewById(R.id.letterK);
        clickK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('K') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('K'), 0);
                }
            }
        });
        clickL = (TextView) findViewById(R.id.letterL);
        clickL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('L') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('L'), 0);
                }
            }
        });
        clickM = (TextView) findViewById(R.id.letterM);
        clickM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('M') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('M'), 0);
                }
            }
        });
        clickN = (TextView) findViewById(R.id.letterN);
        clickN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('N') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('N'), 0);
                }
            }
        });
        clickO = (TextView) findViewById(R.id.letterO);
        clickO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('O') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('O'), 0);
                }
            }
        });
        clickP = (TextView) findViewById(R.id.letterP);
        clickP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('P') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('P'), 0);
                }
            }
        });
        clickQ = (TextView) findViewById(R.id.letterQ);
        clickQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('Q') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('Q'), 0);
                }
            }
        });
        clickR = (TextView) findViewById(R.id.letterR);
        clickR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('R') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('R'), 0);
                }
            }
        });
        clickS = (TextView) findViewById(R.id.letterS);
        clickS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('S') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('S'), 0);
                }
            }
        });
        clickT = (TextView) findViewById(R.id.letterT);
        clickT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('T') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('T'), 0);
                }
            }
        });
        clickU = (TextView) findViewById(R.id.letterU);
        clickU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('U') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('U'), 0);
                }
            }
        });
        clickV = (TextView) findViewById(R.id.letterV);
        clickV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('V') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('V'), 0);
                }
            }
        });
        clickW = (TextView) findViewById(R.id.letterW);
        clickW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('W') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('W'), 0);
                }
            }
        });
        clickX = (TextView) findViewById(R.id.letterX);
        clickX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('X') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('X'), 0);
                }
            }
        });
        clickY = (TextView) findViewById(R.id.letterY);
        clickY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('Y') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('Y'), 0);
                }
            }
        });
        clickZ = (TextView) findViewById(R.id.letterZ);
        clickZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ListView topView = (ListView) findViewById(R.id.listPhone);
                if (contactLocations.get('Z') != -1)
                {
                    topView.setSelectionFromTop(contactLocations.get('Z'), 0);
                }
            }
        });
    }
}
