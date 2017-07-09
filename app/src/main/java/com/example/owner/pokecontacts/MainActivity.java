package com.example.owner.pokecontacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends Activity
{

    private ListView lvPhone;
    protected String previousName = "";
    protected String previousNum = "";
    protected  Android_Contact curr_selected = null;  //This will be for calling, texting, etc with the user selected contact
    protected static final HashMap<String, Integer> savedAvatarNums = new HashMap<String, Integer>(); //For saving contact's avatars. Key will be contact phone number, value will be pokedex number
    private Random rand = new Random();   //for getting random pokemon avatar outcomes at initialization
    private int low = 1;    //minimum pokedex number for random seed
    private int high = 10;   //maximum pokedex number (exclusive) for random seed. This should be 1 higher than maximum seed number

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        
        //---< App has been used before. Recover the save state >---
        if (savedInstanceState != null)
        {
            super.onRestoreInstanceState(savedInstanceState);
            HashMap<String, Integer> savedAvatarNums = (HashMap<String, Integer>) savedInstanceState.getSerializable("saveData");
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

        //-----< This arraylist will contain information for each contact >-----
        ArrayList<Android_Contact> android_contact_data = new ArrayList<Android_Contact>();

        //-----< Now get the contacts >-----
        Cursor cursor_android_contacts = null;

        //-----< This will give all contacts >-----
        cursor_android_contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor_android_contacts.getCount() > 0)
        {
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
                    
                    //----< retrieve and add the number to the contact object >-----
                    String number = cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    theContact.android_contact_number = number.replaceAll("\\D+","");
                    
                    if (savedAvatarNums.containsKey(number.replaceAll("\\D", "")))  //This contact has existed before. Get their pokedex number
                    {
                        theContact.setAvatarNum(savedAvatarNums.get(number.replaceAll("\\D", "")));
                    }
                    else
                    {
                        //<----< get a random number for the contact's pokemon avatar and put the number into the saveData map >----
                        int pokedexNumber = rand.nextInt(high-low) + low;   //this ensures a random pokemon avatar outcome
                        theContact.setAvatarNum(pokedexNumber);
                        savedAvatarNums.put(number.replaceAll("\\D", ""), pokedexNumber);
                    }

                    //-----< add the contact to the contact arraylist if it wasn't added previously >-----
                    if (!contactDisplayName.equals(previousName)){
                        android_contact_data.add(theContact);
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
                view.showContextMenu();
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listPhone){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Options");
            menu.add(0, v.getId(), 0, "Call");
            menu.add(0, v.getId(), 0, "Text");
            menu.add(0, v.getId(), 0, "Change Pokemon");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getTitle() == "Call"){
            
            //-----< The call function will display the number in the keypad so the user can decide to call or cancel >-----
            //-----< This makes for a better user experience >-----
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", curr_selected.getmPhone(), null));
            startActivity(intent);
        }
        else if (item.getTitle() == "Text"){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + curr_selected.getmPhone()));
            startActivity(intent);
        }
        else if (item.getTitle() == "Change Pokemon")
        {
            //savedAvatarNums.put("17178842967", 1);
            //showContacts();
        }
        else{
            return false;
        }
        return true;
    }
}
