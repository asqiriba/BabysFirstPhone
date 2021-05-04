package com.example.babysfirstphone.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.babysfirstphone.ContactDataActivity;
import com.example.babysfirstphone.ContactEditionActivity;
import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactEditActivity extends AppCompatActivity {
    ArrayList<Contacts> arrayListContact;
    ContactsAdapter contactAdapter;
    Contacts contacts, editedContact;
    Button contactAddButton, contactListArrange;
    ListView listContacts;
    int index = -1;

    final int CONTACT_VIEW = 1, CONTACT_DELETE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_contact_create);

        loadData();
        listContacts = findViewById(R.id.listView);
        contactAddButton = findViewById(R.id.contactAddButton);
        contactListArrange = findViewById(R.id.listEditButton);

        /*
            Click Listener on Contact Button.
            User click on Contact Button, it takes user to the ContactDataActivity, where we take
            input from the user.
         */
        contactAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactEditActivity.this, ContactDataActivity.class);
            startActivityForResult(intent, 1);
        });

        contactListArrange.setOnClickListener(view -> {
            saveData(arrayListContact);
            startActivity(new Intent(view.getContext(),
                    ContactListEditActivity.class));
        });

        // Here we called the constructor of ContactsAdpater class, in  which we passed the context and ArrayList.
        contactAdapter = new ContactsAdapter(ContactEditActivity.this, arrayListContact);

        listContacts.setAdapter(contactAdapter);
        listContacts.setOnItemClickListener((parent, view, position, id) -> registerForContextMenu(listContacts));
    }

    /*
        Menu render when long-press.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            menu.add(0, CONTACT_VIEW, 1, "Edit");
            menu.add(0, CONTACT_DELETE, 2, "Delete");
        }
    }

    /*
        Here I implemented the Menu option, when user do a long Press on contacts.
        In here we write the code for each Menu.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTACT_VIEW:
                Toast.makeText(ContactEditActivity.this, "Edit", Toast.LENGTH_SHORT).show();
                AdapterView.AdapterContextMenuInfo infoEdit = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                index = infoEdit.position;

                editedContact = arrayListContact.get(index);

                Intent intent = new Intent(this, ContactEditionActivity.class);
                intent.putExtra("name", editedContact.getName());
                intent.putExtra("number", editedContact.getNumberFormat());
                intent.putExtra("image", editedContact.getImage());
                intent.putExtra("type", editedContact.getType());
                startActivityForResult(intent, 2);
                break;

            case CONTACT_DELETE:
                Toast.makeText(ContactEditActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                AdapterView.AdapterContextMenuInfo infoDelete = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int i = infoDelete.position;

                arrayListContact.remove(i);
                deleteData(arrayListContact);

                contactAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    /*
        We receive the data coming from ContactDataActivity. This is what the method do with
        the data that comes from both the Adapters.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e("onActivityResult", "RequestCode == " + requestCode);
//        Log.e("onActivityResult", "ResultCode == " + resultCode);

        if (resultCode == 0) {
            loadData();
            contactAdapter.notifyDataSetChanged();
        }

        if (resultCode == 2 && requestCode != 2) {
            contacts = (Contacts) data.getSerializableExtra("data");
            arrayListContact.add(contacts);
            contactAdapter.notifyDataSetChanged();
        }

        if (requestCode == 2) {
            contacts = (Contacts) data.getSerializableExtra("data");

//            assert contacts != null;
            editedContact.setName(contacts.getName());
            editedContact.setNumber(contacts.getNumber());
            editedContact.setContactType(contacts.getType());
            editedContact.setImageId(contacts.getImage());

            arrayListContact.remove(index);
            arrayListContact.add(index, editedContact);
            deleteData(arrayListContact);

            contactAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        saveData(arrayListContact);
        finish();
    }

    /*
       Writes the created user into a json file for persistence.
    */
    private void saveData(ArrayList<Contacts> arrayListContact) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayListContact);
        editor.putString("contact list", json);
        editor.apply();
    }

    /*
        Reads the created user into a json file for persistence.

        Usage:
        ArrayList<Contacts> arrayListContact;
        loadData();

     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<>();
        }
    }

    /*
        To delete from file, go to View/Tool Windows/Device File Explorer; the file in which it's all
        written is in /data/data/com.example.babysfirstphone/shared_prefs/shared preferences.xml
     */
    private void deleteData(ArrayList<Contacts> arrayListContact) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        Gson gson = new Gson();
        String json = gson.toJson(arrayListContact);
        editor.putString("contact list", json);
        editor.apply();
    }
}
