package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ListView;
import android.view.ContextMenu;
import android.widget.Toast;

import com.example.babysfirstphone.contacts.ContactDetails;
import com.example.babysfirstphone.contacts.ContactsAdapter;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // We make a Caller object, from Controllers/Caller.java
    Caller caller = new Caller();

    // We used ArrayList because its size can be increased or reduced dynamically.
//    ArrayList<Contacts> arrayListContact;
//    ContactsAdapter contactAdapter;
//    Contacts contacts;
//
//    Button contactAddButton;
//    ListView listContacts;
//
//    final int CONTACT_VIEW = 1, CONTACT_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        arrayListContact = new ArrayList<Contacts>();
//        listContacts = (ListView) findViewById(R.id.listView);
//        contactAddButton = (Button) findViewById(R.id.contactAddButton);

        /*
            Click Listener on Contact Button.
            User click on Contact Button, it takes user to the ContactDataActivity, where we take
            input from the user.
         */
//        contactAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ContactDataActivity.class);
//                startActivityForResult(intent, 1);
//            }
//        });
//
//        // Here we called the constructor of ContactsAdpater class, in  which we passed the context and ArrayList.
//        contactAdapter = new ContactsAdapter(MainActivity.this, arrayListContact);
//
//        listContacts.setAdapter(contactAdapter);
//
//        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                registerForContextMenu(listContacts);
//            }
//        });

//        // Call interface
//        ImageButton im_contact = (ImageButton) findViewById(R.id.image_button_android);
//        im_contact.setOnClickListener(view -> callPhoneNumber());

        // Bottom Menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        break;
                    case R.id.nav_emergency:
                        selectedFragment = new EmergencyFragment();
                        break;
                    case R.id.nav_groups:
                        selectedFragment = new GroupsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*
          This function checks on system permissions to make phone calls,
          then connects the phone call.
         */
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber() {

        // If permission is already given, connect a call.
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
            }
            startActivity(caller.makeCall());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    /** Called when the user taps the helper button */
//    public void testContactActivity(View view) {
//        Intent intent = new Intent(getBaseContext(), ContactDataActivity.class);
//        startActivity(intent);
//    }

//    /*
//        Menu render when long-press.
//     */
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        if (v.getId() == R.id.listView) {
//            menu.add(0, CONTACT_VIEW, 1, "Call");
//            menu.add(0, CONTACT_DELETE, 2, "Delete");
//        }
//    }
//
//    /*
//        Here i implemented the Menu option, when user do a long Press on contacts.
//        In here we write the code for each Menu.
//     */
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case CONTACT_VIEW:
////                Intent intent = new Intent(MainActivity.this, ContactDetails.class);
////                AdapterView.AdapterContextMenuInfo infoView = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
////                int index1 = infoView.position;
////
////                intent.putExtra("details", arrayListContact.get(index1));
////
////                startActivity(intent);
//        // Call interface
/////TODO: Make phone call available to each contact.
//                callPhoneNumber();
//                break;
//
//            case CONTACT_DELETE:
//                Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();
//                AdapterView.AdapterContextMenuInfo infoDelete = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                int index = infoDelete.position;
//
//                Log.e("index", index + " ");
//                arrayListContact.remove(index);
//                contactAdapter.notifyDataSetChanged();
//                break;
//        }
//        return true;
//    }
//
//    /*
//        We receive the data coming from ContactDataActivity.
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == 2) {
//            contacts = (Contacts) data.getSerializableExtra("data");
//            arrayListContact.add(contacts);
//            contactAdapter.notifyDataSetChanged();
//        }
//    }
}