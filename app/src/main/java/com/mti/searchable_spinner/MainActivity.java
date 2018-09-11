package com.mti.searchable_spinner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mti.searchable_spinner_ext.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> mArrayList=new ArrayList<>(Arrays.asList("Red_Apple","Green_Apple","Yellow_Banana" ,"orange", "Goava", "potato","Beans","cucamber","carrot","apple_syrup"  ));

    SearchableSpinner    searchableSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchableSpinner  = findViewById(R.id.searchableSpinner);
      searchableSpinner.setTitle("Category",ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)); //without theme);
      searchableSpinner.setArrayList_String(mArrayList);




        //For getting selected Item Data

      findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               String slect = searchableSpinner.getSelectedItem().toString();

               int pos=searchableSpinner.getSelectedItemPosition();

                  Toast.makeText(MainActivity.this,  pos+"-" + slect, Toast.LENGTH_SHORT).show();
              }

      });
    }

}
