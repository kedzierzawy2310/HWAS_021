package com.example.hwas_021;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.example.hwas_021.EventFragment2.OnListFragmentInteractionListener;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private final Context cxt;
    private final List<Event> listEvent;

    public EventAdapter(Context cxt, List<Event> listEvent, OnListFragmentInteractionListener mListener) {
        this.cxt = cxt;
        this.listEvent = listEvent;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_layout, viewGroup, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder eventViewHolder, int i) {
        eventViewHolder.event = listEvent.get(i);
        eventViewHolder.textTitle.setText(eventViewHolder.event.getTitle());
        if(!eventViewHolder.event.getImageString().contains("basketball")){
            Bitmap cameraImage = Camera.decodePic(eventViewHolder.event.getImageString(), 60, 80);
            eventViewHolder.imageView.setImageBitmap(cameraImage);
        }
        else{
            switch (eventViewHolder.event.getImageString()){
                case "basketball_ante":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.ante));
                    break;
                case "basketball_james":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.james));
                    break;
                case "basketball_kyrie":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.kyrie));
                    break;
                case "basketball_rose":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.rose));
                    break;
                case "basketball_walker":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.walker));
                    break;
                case "basketball_wall":
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.wall));
                    break;
                default:
                    eventViewHolder.imageView.setImageDrawable(cxt.getResources().getDrawable(R.drawable.lana));
                    break;
            }
        }
        eventViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDetails(eventViewHolder.event);
            }
        });

        eventViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(cxt).setTitle("Delete event").setMessage("Delete an event?").setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mListener.delete(eventViewHolder.event);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", null).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView imageView;
        public final TextView textTitle;
        public final FloatingActionButton delete;
        public Event event;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textViewTitle);
            delete = itemView.findViewById(R.id.delete);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + textTitle.getText() + "'";
        }
    }
}
