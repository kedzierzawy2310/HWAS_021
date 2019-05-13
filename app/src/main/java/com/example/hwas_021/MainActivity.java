package com.example.hwas_021;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.hwas_021.ActivityDet.DATA_CHANGED_KEY;
import static com.example.hwas_021.EventFragment.REQUEST_IMAGE_CAPTURE_DET;

public class MainActivity extends AppCompatActivity implements hwas, EventFragment2.OnListFragmentInteractionListener, EventFragment.OnViewInteractionListener{

    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int DETAILS = 4;
    private final String CURRENT_EVENT = "current_event";
    public static final String EVENT_ID="event_id";
    public static final String EVENT_TITLE="event_title";
    public static final String EVENT_COUNTRY="event_country";
    public static final String EVENT_DESCRIPTION="event_description";
    public static final String EVENT_IMAGE="event_image";
    public static final String EVENT_IMAGE_POSITION="event_image_int";
    public static final int BUTTON_REQUEST1=1;
    public static final int BUTTON_REQUEST2=2;
    private final String SHARED_PREFERENCES = "shared_preferences";
    private final String NUMBER = "number";
    public static final Bundle extras = new Bundle();
    public int id, image;
    private Event currentEvent;
    public String title, description, country;
    public static List<Event> eventList = new ArrayList<>();
    public static Integer[] images = {R.drawable.ante, R.drawable.james, R.drawable.kyrie, R.drawable.rose, R.drawable.walker, R.drawable.wall};
    public static String mCurrentPhotoPath;
    public Event cameraEvent;
    private boolean Changed;
    public static ImageView im;


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if(currentEvent != null) outState.putParcelable(CURRENT_EVENT, currentEvent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) currentEvent = savedInstanceState.getParcelable(CURRENT_EVENT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        restore();
        Changed = false;

        im = (ImageView) findViewById(R.id.imageView);
        EventFragment fragment = new EventFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.activityMain, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if(currentEvent != null) displayInFragment(currentEvent);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        save();
    }

    @Override
    public void showDetails(Event event) {
        currentEvent = event;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            displayInFragment(event);
        }
        else {
            Intent intent = new Intent(this, ActivityDet.class);
            intent.putExtra("event", event);
            startActivityForResult(intent, DETAILS);
        }

    }

    @Override
    public void reset() {
        save();
        restore();
    }

    @Override
    public void delete(Event deleted) {
        eventList.remove(deleted);
        reset();
    }

    public boolean checkChanged()
    {
        return Changed;
    }

    @Override
    public void setChanged(boolean val) {
        Changed = val;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Camera(View view) {
        Intent pickAdd = new Intent(this, FragmentsActivity.class);
        startActivityForResult(pickAdd,BUTTON_REQUEST1);
    }

    public void Add(View view) {
        Intent pickAdd = new Intent(this, FragmentsActivity.class);
        startActivityForResult(pickAdd,BUTTON_REQUEST2);
    }

    public void displayInFragment(Event event)
    {
        EventFragment detailsFragment = ((EventFragment) getSupportFragmentManager().findFragmentById(R.id.rightFragment));
        if(detailsFragment!=null)
        {
            detailsFragment.displayEvent(event);
        }
    }

    private void save()
    {
        SharedPreferences events = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = events.edit();
        editor.clear();
        editor.putInt(NUMBER, eventList.size());
        for(int i=0; i<eventList.size(); i++)
        {
            Event event = eventList.get(i);
            editor.putString(EVENT_TITLE+i, event.getTitle());
            editor.putString(EVENT_COUNTRY+i, event.getCountry());
            editor.putString(EVENT_DESCRIPTION+i, event.getDescription());
            editor.putInt(EVENT_IMAGE_POSITION+i, event.getImage());
            editor.putString(EVENT_IMAGE+i, event.getImageString());
            editor.putInt(EVENT_ID+i, i);
        }
        editor.apply();
    }

    public void clearList()
    {
        eventList.clear();
    }

    private void restore()
    {
        SharedPreferences events = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        int numOfTracks = events.getInt(NUMBER, 0);
        if(numOfTracks != 0)
        {
            clearList();
            for(int i=0; i<numOfTracks; i++)
            {
                String title = events.getString(EVENT_TITLE+i, "0");
                String country = events.getString(EVENT_COUNTRY+i, "0");
                String description = events.getString(EVENT_DESCRIPTION+i, "0");
                int position = events.getInt(EVENT_IMAGE_POSITION+i,0);
                String image = events.getString(EVENT_IMAGE+i, "0");
                eventList.add(new Event(i, country, title, description, position, image));
            }
        }
        EventFragment2.getAdapter().notifyDataSetChanged();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = CreatePhotoFile(cameraEvent.getTitle());
            }
            catch(IOException ex)
            {

            }
            if(photoFile!=null) {
                Uri photoURI = FileProvider.getUriForFile(this, getString(R.string.fileProvider), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File CreatePhotoFile(String name) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = name + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void setPhotoPath(String photoPath)
    {
        mCurrentPhotoPath = photoPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode==RESULT_OK){
            switch(requestCode) {
                case BUTTON_REQUEST1:
                    cameraEvent = data.getParcelableExtra("event");
                    dispatchTakePictureIntent();
                    break;
                case BUTTON_REQUEST2:
                    Event extraAdd = data.getParcelableExtra("event");
                    extraAdd.setImage();
                    add(extraAdd);
                    break;
                case REQUEST_IMAGE_CAPTURE_DET:
                    currentEvent.setImageString(mCurrentPhotoPath);
                    setChanged(true);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    cameraEvent.setImageString(mCurrentPhotoPath);
                    add(cameraEvent);
                    setChanged(true);
                    break;
                case DETAILS:
                    if(data.getBooleanExtra(DATA_CHANGED_KEY, false))
                    {
                        reset();
                        setChanged(true);
                    }
                    break;
                default:
                        break;
            }
        }
    }


    public void add(Event event){
        eventList.add(event);
        reset();}

    @Override
    public void changeImage(Event event, String image) {
        event.setImageString(image);
        setChanged(true);
    }
}
