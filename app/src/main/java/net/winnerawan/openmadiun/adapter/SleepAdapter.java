package net.winnerawan.openmadiun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.winnerawan.openmadiun.R;
import net.winnerawan.openmadiun.model.Place;

import java.util.List;

/**
 * Created by winnerawan on 2/9/17.
 */

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.PlaceViewHolder>{

    public List<Place> places;
    private int rowLayout;
    private Context context;

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView txtTitle;
        TextView txtSubtitle;
        TextView txtSubSubTitle;
        TextView txtButton;
        ImageView imageView;

        public PlaceViewHolder(View v) {
            super(v);
            layout = (RelativeLayout) v.findViewById(R.id.listAdapter);
            txtTitle = (TextView) v.findViewById(R.id.title);
            txtSubtitle = (TextView) v.findViewById(R.id.detail);
            txtSubSubTitle = (TextView) v.findViewById(R.id.price);
            imageView = (ImageView) v.findViewById(R.id.img_content);
            txtButton = (TextView) v.findViewById(R.id.book);
        }
    }

    public SleepAdapter(List<Place> places, int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.places = places;
        this.context = context;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.txtTitle.setText(places.get(position).getTitle());
        holder.txtSubtitle.setText(places.get(position).getLocation().getAddress());
        holder.txtButton.setText("Check");
        String price = places.get(position).getPriceRange();
        String telp = places.get(position).getTelephone();
        if (price.isEmpty()) {
            holder.txtSubSubTitle.setText(places.get(position).getTelephone());
        } else if (telp.isEmpty()) {
            holder.txtSubSubTitle.setText(places.get(position).getPriceRange());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.layout.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public Object getItem(int location) {
        return places.get(location);
    }
}
