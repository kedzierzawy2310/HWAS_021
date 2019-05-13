package com.example.hwas_021;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

public class FragmentsActivity extends AppCompatActivity implements AddFragment.OnAddInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_activity);
    }

    @Override
    public void add(Event event) {
        Intent intent = new Intent();
        intent.putExtra("event", event);
        setResult(RESULT_OK, intent);
        finish();
    }
}
