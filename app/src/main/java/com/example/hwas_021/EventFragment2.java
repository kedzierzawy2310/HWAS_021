package com.example.hwas_021;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class EventFragment2 extends Fragment {

    private OnListFragmentInteractionListener mListener;
    public static List<Event> eventList;
    private static RecyclerView recyclerView;

    public EventFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        if (view instanceof RecyclerView || view instanceof RelativeLayout) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
            eventList = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            EventAdapter eventAdapter = new EventAdapter(getContext(),MainActivity.eventList, mListener);
            recyclerView.setAdapter(eventAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mListener.checkChanged())
        {
            notifyDataSetChange();
            mListener.setChanged(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static  RecyclerView.Adapter getAdapter()
    {
        return recyclerView.getAdapter();
    }

    public void notifyDataSetChange()
    {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        void showDetails(Event event);
        void reset();
        void delete(Event deleted);
        boolean checkChanged();
        void setChanged(boolean val);
    }
}
