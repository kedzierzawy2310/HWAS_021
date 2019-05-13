package com.example.hwas_021;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EventFragment extends Fragment implements View.OnClickListener{

    private ImageView imageView;
    private TextView titleView;
    private TextView countryView;
    private TextView descriptionView;
    private Intent intent;
    public static final int REQUEST_IMAGE_CAPTURE_DET = 5;
    private OnViewInteractionListener mListener;
    private String mCurrentPhotoPath;
    private Event mCurrent;

    public EventFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.event_fragment, container, false);
        imageView = view.findViewById(R.id.imageView2);
        titleView = view.findViewById(R.id.textView);
        countryView = view.findViewById(R.id.textView3);
        descriptionView = view.findViewById(R.id.textView5);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewInteractionListener) {
            mListener = (OnViewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private File CreatePhotoFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = mCurrent.getTitle() + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        mListener.setPhotoPath(mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onClick(View v) {
        if(mCurrent!=null)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null)
            {
                File photoFile = null;
                try{
                    photoFile = CreatePhotoFile();
                }
                catch(IOException ex)
                {

                }
                if(photoFile!=null)
                {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), getString(R.string.fileProvider), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_DET);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.imageView2).setOnClickListener(this);
        intent = getActivity().getIntent();
        if(intent != null)
        {
            Event event = intent.getParcelableExtra("event");
            if(event != null) displayEvent(event);
        }

    }

    public void displayEvent(Event event)
    {
        mCurrent = event;
        titleView.setText(event.getTitle());
        countryView.setText(event.getCountry());
        descriptionView.setText(event.getDescription());

        if(event.getImageString() != null && !event.getImageString().isEmpty()) {
            if (event.getImageString().contains("basketball")) {
                switch (event.getImageString()) {
                    case "basketball_ante":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ante));
                        break;
                    case "basketball_james":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.james));
                        break;
                    case "basketball_kyrie":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.kyrie));
                        break;
                    case "basketball_rose":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.rose));
                        break;
                    case "basketball_walker":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.walker));
                        break;
                    case "basketball_wall":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.wall));
                        break;
                    default:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lana));
                        break;
                }
            }
            else{
                Handler handler = new Handler();
                imageView.setVisibility(View.INVISIBLE);
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run()
                    {
                        imageView.setVisibility(View.VISIBLE);
                        Bitmap cameraImage = Camera.decodePic(mCurrent.getImageString(), imageView.getWidth(), imageView.getHeight());
                        imageView.setImageBitmap(cameraImage);
                    }
                }, 200);
            }
        }
        else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.lana));
        }
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent data)
    {
        if(request_code == REQUEST_IMAGE_CAPTURE_DET && result_code == RESULT_OK)
        {
            FragmentActivity holdingActivity = getActivity();
            if(holdingActivity != null)
            {
                ImageView eventImage = holdingActivity.findViewById(R.id.imageView2);
                Bitmap cameraImage = Camera.decodePic(mCurrentPhotoPath, eventImage.getWidth(), eventImage.getHeight());
                eventImage.setImageBitmap(cameraImage);
                mCurrent.setImageString(mCurrentPhotoPath);
                mListener.changeImage(mCurrent, mCurrentPhotoPath);
            }
        }
    }

    public interface OnViewInteractionListener {
        void changeImage(Event event, String image);
        void setPhotoPath(String photoPath);
    }


}
