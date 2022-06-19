package com.example.gdteamproject11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddStore extends AppCompatActivity {
    ListView listView1;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    EditText addCk, addPR;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        addCk = findViewById(R.id.addCook);
        //addPR = findViewById(R.id.addPrice);
        btnAdd = findViewById(R.id.btnAdd);

        listItem = new ArrayList<String>();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            listItem.add(addCk.getText().toString());
            adapter.notifyDataSetChanged();
            addCk.setText("");
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listItem);
        listView1 = findViewById(R.id.listView1);
        listView1.setAdapter(adapter);
    }
}