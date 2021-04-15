package com.example.babysfirstphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import android.content.SharedPreferences;

import com.example.babysfirstphone.contacts.RecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import com.example.babysfirstphone.contacts.ContactsAdapter;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class SettingsInternal extends AppCompatActivity {

    ArrayList<Contacts> arrayListContact;
    ContactsAdapter contactAdapter;
    Contacts contacts, editedContact;
    Button contactAddButton;
    ListView listContacts;
    int index = -1;

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

        Button manageListButton = findViewById(R.id.listButton);
        manageListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openContactList(); }
        });
    }

    @Override
    public void onBackPressed() {
        saveData(arrayListContact);
        finish();
    }

    /*
        Contact Create, Edit and Deletion.
     */
    public void  openContactCreation() {
        setContentView(R.layout.activity_internal_contact_create);

        loadData();
        listContacts = findViewById(R.id.listView);
        contactAddButton = findViewById(R.id.contactAddButton);

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
        Contact sorting functionality.
     */
    public void  openContactList() {
        setContentView(R.layout.activity_internal_contact_list);

        loadData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayListContact);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /*
        Handle list animation and sorting.
     */
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(
                @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(arrayListContact, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }
    };

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
        Here i implemented the Menu option, when user do a long Press on contacts.
        In here we write the code for each Menu.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTACT_VIEW:
                Toast.makeText(SettingsInternal.this, "Edit", Toast.LENGTH_SHORT).show();
                AdapterView.AdapterContextMenuInfo infoEdit = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                index = infoEdit.position;

                editedContact = arrayListContact.get(index);

                Log.e("onContextItemSelected", "index == " + index);
                Log.e("onContextItemSelected", "editedContactName == " + editedContact.getName());
                Log.e("onContextItemSelected", "editedContactPlainNo == " + editedContact.getNumberFormat());
                Log.e("onContextItemSelected", "editedContactImage == " + editedContact.getImage());
                Log.e("onContextItemSelected", "editedContactType == " + editedContact.getType());

                Intent intent = new Intent(this, ContactEditionActivity.class);
                intent.putExtra("name", editedContact.getName());
                intent.putExtra("number", editedContact.getNumberFormat());
                intent.putExtra("image", editedContact.getImage()); //2131231006 || 2131231007
                intent.putExtra("type", editedContact.getType());
                startActivityForResult(intent, 2);
                break;

            case CONTACT_DELETE:
                Toast.makeText(SettingsInternal.this, "Delete", Toast.LENGTH_SHORT).show();
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
        We receive the data coming from ContactDataActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "RequestCode == " + requestCode);
        Log.e("onActivityResult", "ResultCode == " + resultCode);

        if (resultCode == 2 && requestCode != 2) {
            contacts = (Contacts) data.getSerializableExtra("data");
            arrayListContact.add(contacts);
            contactAdapter.notifyDataSetChanged();
        }

        if (requestCode == 2) {
            contacts = (Contacts) data.getSerializableExtra("data");

            editedContact.setName(contacts.getName());
            editedContact.setNumber(contacts.getNumber());
            editedContact.setContactType(contacts.getType());

            arrayListContact.remove(index);
            arrayListContact.add(index, editedContact);
            deleteData(arrayListContact);

            contactAdapter.notifyDataSetChanged();
        }
    }
    /** Called when the user taps the helper button */
    public void testContactActivity(View view) {
        Intent intent = new Intent(getBaseContext(), ContactDataActivity.class);
        startActivity(intent);
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
            arrayListContact = new ArrayList<Contacts>();
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

    private void deleteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}