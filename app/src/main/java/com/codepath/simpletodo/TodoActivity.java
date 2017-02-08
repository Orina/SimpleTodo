package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {

    private ListView lvItems;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private TextView editTextView;

    private static int EDIT_ITEM_REQUEST_CODE = 1;
    private static final String ITEMS_FILE_NAME = "todo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        editTextView = (EditText) findViewById(R.id.etNewItem);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        editTextView.setFocusable(true);
        editTextView.setFocusableInTouchMode(true);
        editTextView.requestFocus();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onEditItem(position);
            }
        });
    }

    public void onAddItem(View view) {
        String itemText = editTextView.getText().toString();
        editTextView.setText("");
        if ("".equals(itemText.trim())) {
            return;
        }
        itemsAdapter.add(itemText.trim());
        editTextView.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            String itemText = data.getStringExtra(EditItemActivity.ITEM_NAME);
            int pos = data.getIntExtra(EditItemActivity.ITEM_POSITION, -1);
            if (pos != -1 && itemText != null && !"".equals(itemText.trim())) {
                items.set(pos, itemText.trim());
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    private void onEditItem(int pos) {
        Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
        intent.putExtra(EditItemActivity.ITEM_NAME, items.get(pos));
        intent.putExtra(EditItemActivity.ITEM_POSITION, pos);
        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, ITEMS_FILE_NAME);
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, ITEMS_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException ignore) {
        }
    }
}
