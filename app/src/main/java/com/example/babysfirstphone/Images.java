package com.example.babysfirstphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * In this class, which extends Activity, we display profile pictures of user
 * using Grid View.
 */
public class Images extends Activity {

    /*
        Here we created the array of images.
     */
    private int [] arrayOfImages = {};
//    {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
//            R.drawable.img5,R.drawable.img6,R.drawable.img7,R.drawable.img1,R.drawable.img2,
//            R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7,
//            R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,
//            R.drawable.img6,R.drawable.img7
//    };

    /*
        Here we created the object of controllers/AdapterImages class.
     */
    AdapterImages adapterImages;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        gridView=(GridView)findViewById(R.id.gridview);

        /*
            Called the parameterized constructor of AdapterImages class.
         */
        adapterImages = new AdapterImages(arrayOfImages,this);

        gridView.setAdapter(adapterImages);

        /*
            Here we set the click listener on GridView.
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Images.this, position + "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("img", arrayOfImages[position]);

                /*
                    We send the selected contact image by user.
                 */
                setResult(1, intent);
                finish();
            }
        });
    }
}