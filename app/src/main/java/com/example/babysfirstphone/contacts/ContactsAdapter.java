package com.example.babysfirstphone.contacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;

import java.util.ArrayList;

/**
 * Instead of creating object of each and setting attributes(By creating object of each and
 * setting attributes for each View, the code become lengthy) of each we make an XML file
 * lay_contact.xml, which have all the views.
 */
public class ContactsAdapter extends BaseAdapter {

    Context context;
    ArrayList<Contacts> contact;

    public ContactsAdapter(Context context, ArrayList<Contacts> contact) {
        this.context = context;
        this.contact = contact;
    }

    @Override
    public int getCount() {
        return contact.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        /*
            LayoutInflater class, which is used to wrap up some view into one.
            Inflate using LayoutInflater class. Because of this, the code is more readable.
         */
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            // Here in the arguments, we have given the XML file which has to be Inflate.
            view = layoutInflater.inflate(R.layout.lay_conatct, null);
        }

        else {
            view = convertView;
        }

        ImageView imgContact = (ImageView) view.findViewById(R.id.contactImage);
        TextView contactName = (TextView) view.findViewById(R.id.contactName);

        //Retrieve data.
        Contacts contacts = contact.get(position);
        imgContact.setImageResource(contacts.getImage());
        contactName.setText(contacts.getName());
        System.getProperty("line.separator");
        Log.e("name", contacts.getName() + " ");

        return view;
    }
}