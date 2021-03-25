package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.babysfirstphone.contacts.ContactsAdapter;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SettingsInternal extends AppCompatActivity {

    ArrayList<Contacts> arrayListContact;
    ContactsAdapter contactAdapter;
    Contacts contacts;
    Button contactAddButton;
    ListView listContacts;

    final int CONTACT_VIEW = 1, CONTACT_DELETE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_internal);

        FloatingActionButton fab = findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

        Button addContactButton = findViewById(R.id.contactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactCreation();
            }
        });
    }


    public void  openContactCreation() {

        setContentView(R.layout.activity_internal_contact_create);

        arrayListContact = new ArrayList<Contacts>();
        listContacts = (ListView) findViewById(R.id.listView);
        contactAddButton = (Button) findViewById(R.id.contactAddButton);

        /*
            Click Listener on Contact Button.
            User click on Contact Button, it takes user to the ContactDataActivity, where we take
            input from the user.
         */
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsInternal.this, ContactDataActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Here we called the constructor of ContactsAdpater class, in  which we passed the context and ArrayList.
        contactAdapter = new ContactsAdapter(SettingsInternal.this, arrayListContact);

        listContacts.setAdapter(contactAdapter);

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                registerForContextMenu(listContacts);
            }
        });

    }


    /*
        Menu render when long-press.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            menu.add(0, CONTACT_VIEW, 1, "Call");
            menu.add(0, CONTACT_DELETE, 2, "Delete");
        }
    }

    /*
        Here i implemented the Menu option, when user do a long Press on contacts.
        In here we write the code for each Menu.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTACT_VIEW:
///TODO: Make phone call available to each contact.
//                callPhoneNumber();
                break;

            case CONTACT_DELETE:
                Toast.makeText(SettingsInternal.this, "Delete", Toast.LENGTH_SHORT).show();
                AdapterView.AdapterContextMenuInfo infoDelete = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = infoDelete.position;

                Log.e("index", index + " ");
                arrayListContact.remove(index);
                contactAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    /*
        We receive the data coming from ContactDataActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            contacts = (Contacts) data.getSerializableExtra("data");
            arrayListContact.add(contacts);
            contactAdapter.notifyDataSetChanged();
        }
    }
    /** Called when the user taps the helper button */
    public void testContactActivity(View view) {
        Intent intent = new Intent(getBaseContext(), ContactDataActivity.class);
        startActivity(intent);
    }
}