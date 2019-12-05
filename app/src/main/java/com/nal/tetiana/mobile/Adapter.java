package com.nal.tetiana.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nal.tetiana.mobile.Movies;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {

    private List<Movies> movieList;

    Adapter(List<Movies> moviesList) {
        this.movieList = moviesList;
    }

    @NonNull
    @Override
    public Adapter.CustomViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.title.setText(movieList.get(position).getTitle());
        holder.desctiption.setText(movieList.get(position).getDescription());
        Picasso.get().load(movieList.get(position).getPoster()).into(holder.poster);
        holder.year.setText(String.format("Year: %s", Integer.toString(movieList.get(position).getYear())));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

     class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView desctiption;
        private ImageView poster;
        private TextView year;

        private CustomViewHolder(final View itemView) {
            super(itemView);

            desctiption = itemView.findViewById(R.id.custom_desctription);
            poster = itemView.findViewById(R.id.custom_imageView);
            title = itemView.findViewById(R.id.custom_title);
            year = itemView.findViewById(R.id.custom_year);
        }
    }
}
