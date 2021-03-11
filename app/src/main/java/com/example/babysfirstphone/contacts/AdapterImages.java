package com.example.babysfirstphone.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created the AdapterImages class which extends BaseAdapter , so that user can choose the profile
 * picture from Grid View in a efficient manner.
 *
 * Here we created the AdapterImages to select the contact picture from GridView. Implements four
 * methods. getCount(), getItem(), getItemId() and getView()—When creating custom Adapter we must
 * override this methods.
 *
 * @override getCount(), getItem(), getItemId() and getView()
 */
public class AdapterImages extends BaseAdapter {

    private int [] contactImageId;
    Context context;

    public AdapterImages(int[] contactImageId, Context context) {
        this.contactImageId = contactImageId;
        this.context = context;
    }

    /*
        Returns the number of element a view have. Here it will return the number of images
        GridView have.
     */
    @Override
    public int getCount() {
        return contactImageId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
        getView method have return type View, For CustomAdapters getView methods is most important.
        gridView request image for the 0th Position, then AdapterImages return the image for the
        0th Position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
            Show images in GridView.
         */
        ImageView imageView;

        /*
            The number of images fit into the screen, until then Convert View is NULL.
            After that convert view just used the same id to send the images.
         */
        if(convertView == null) {
            /*
                Wwe created the object of ImageView class if ConvertView is Null.
             */
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ListView.LayoutParams(300,300));
        }
        else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(contactImageId[position]);
        return imageView;
    }
}