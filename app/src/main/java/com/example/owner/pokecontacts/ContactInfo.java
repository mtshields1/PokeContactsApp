package com.example.owner.pokecontacts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ContactInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        doStuff();
    }

    public void doStuff()
    {
        Android_Contact curr_contact = null;
        Bundle contactBundle = this.getIntent().getExtras();
        if (contactBundle != null)
        {
            curr_contact = (Android_Contact)contactBundle.getSerializable("current_contact");

            TextView contactName = (TextView) findViewById(R.id.conName);
            contactName.setText(curr_contact.getmName());

            TextView contactPhone = (TextView)findViewById(R.id.conPhone);
            contactPhone.setText(curr_contact.getmPhone());
        }
    }

}
