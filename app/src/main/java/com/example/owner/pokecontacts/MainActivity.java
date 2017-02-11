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

public class MainActivity extends AppCompatActivity
{

    private ListView lvPhone;
    protected String previousName = "";
    protected String previousNum = "";
    protected  Android_Contact curr_selected = null;  //This will be for calling, texting, etc with the user selected contact

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1);

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

                    //-----< add the contact to the contact arraylist if it wasn't added previously >-----
                    if (!contactDisplayName.equals(previousName)){
                        android_contact_data.add(theContact);
                    }
                    previousName = contactDisplayName;
                    previousNum = number.replaceAll("\\D+","");
                }
            }
        }
        //List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
        //listPhoneBook.add(new PhoneBook(BitmapFactory.decodeResource(getResources(), R.mipmap.doge_stare), "Contact 1", "765-5678", "tru1@gmail.com"));
        //add contacts here

        PhoneBookAdapter adapter = new PhoneBookAdapter(this, android_contact_data);
        lvPhone.setAdapter(adapter);
        
        //-----< Register a context menu for the list view when a contact is selected >-----
        registerForContextMenu(lvPhone);

        //-----< set an action on clicking an item >-----
        lvPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Android_Contact selected_contact = (Android_Contact) adapter.getItem(position);
                curr_selected = selected_contact;
                //System.out.println("selected contact num: " + selected_contact.getmPhone());
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
        else if (item.getTitle() == "Change Pokemon"){
            //Toast.makeText(getApplicationContext(),"mon", Toast.LENGTH_LONG).show();  //DEBUG DELETE
        }
        else{
            return false;
        }
        return true;
    }
}
