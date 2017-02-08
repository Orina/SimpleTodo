package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    public static String ITEM_NAME = "itemName";
    public static String ITEM_POSITION = "itemPos";

    private int position = -1;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String text = getIntent().getStringExtra(ITEM_NAME);
        position = getIntent().getIntExtra(ITEM_POSITION, -1);

        if (position < 0 || text == null || "".equals(text.trim())) {
            setResult(RESULT_CANCELED);
            finish();
        }

        textView = (TextView) findViewById(R.id.editItemText);
        textView.setText(text);
    }

    public void onSaveButtonClick(View view) {
        String text = textView.getText().toString();

        Intent data = new Intent();
        data.putExtra(ITEM_NAME, text);
        data.putExtra(ITEM_POSITION, position);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
