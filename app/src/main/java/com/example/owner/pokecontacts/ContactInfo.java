package com.example.owner.pokecontacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ContactInfo extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doStuff();
    }

    public void doStuff()
    {
        Bundle contactBundle = this.getIntent().getExtras();
        if (contactBundle != null) {
            final Android_Contact curr_contact = (Android_Contact) contactBundle.getSerializable("current_contact");

            TextView contactName = (TextView) findViewById(R.id.conName);
            contactName.setText(curr_contact.getmName());

            TextView contactPhone = (TextView) findViewById(R.id.conPhone);
            contactPhone.setText(curr_contact.getmPhone());

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
}
