package com.example.krzysztof.foodfinder.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krzysztof.foodfinder.Model.Place;
import com.example.krzysztof.foodfinder.R;


import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>
{

    LayoutInflater layoutInflater;
    ArrayList<Place> listOfPlaces;
    Context context;

    public CustomAdapter(LayoutInflater layoutInflater, ArrayList<Place> listOfPlaces, Context context) {
        this.layoutInflater = layoutInflater;
        this.listOfPlaces = listOfPlaces;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new CustomAdapter.CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {

        Place currentItem= listOfPlaces.get(position);

        holder.placeTitle.setText(
                currentItem.getName()
        );

            /*holder.isOpen.setText(
                    currentItem.isOpen()
            );*/

        holder.placeAddress.setText(
                currentItem.getAddress()
        );

        Random rnd= new Random();


        if(currentItem.isOpen()== "OPEN")
        {
            holder.placeLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.door_open_icon));
            holder.placeLogo.setBackgroundColor(Color.rgb(0,rnd.nextInt(105)+150, 0));
        }
        else if(currentItem.isOpen()== "CLOSED")
        {
            holder.placeLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.closed_door_icon));
            holder.placeLogo.setBackgroundColor(Color.rgb(rnd.nextInt(105)+150,0,0));
        }
        else
        {
            holder.placeLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.default_item_logo));
            holder.placeLogo.setBackgroundColor(Color.rgb(0,0,rnd.nextInt(105)+150));
        }
    }

    @Override
    public int getItemCount() {
        return listOfPlaces.size();

    }

    class CustomViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener
    {

        private ImageView placeLogo;
        private TextView placeTitle;

        private TextView placeAddress;
        private ViewGroup container;

        public CustomViewHolder(View itemView)
        {
            super(itemView);
            this.placeLogo= itemView.findViewById(R.id.ivItemLogo);
            this.placeTitle= itemView.findViewById(R.id.tvItemName);
            this.placeTitle.setSelected(true);

            this.placeAddress= itemView.findViewById(R.id.tvPlaceAddress);
            this.container= itemView.findViewById(R.id.itemContainer);

            this.container.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Place listItem= listOfPlaces.get(
                    this.getAdapterPosition()
            );

            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+listItem.getAddress()+ " "+ listItem.getName() ));
            context.startActivity(intent);

                /*controller.onListItemClick(
                        listItem,
                        view
                );*/
        }
    }
}