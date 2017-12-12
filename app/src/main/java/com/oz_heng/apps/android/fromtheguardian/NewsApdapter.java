package com.oz_heng.apps.android.fromtheguardian;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.dateToDateString;
import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.dateToTimeString;
import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.stringToDate;
import static com.oz_heng.apps.android.fromtheguardian.Utils.Helper.showSnackBar;


/**
 * Provide views to RecycleView with data from newsList.
 */
public class NewsApdapter extends RecyclerView.Adapter<NewsApdapter.ViewHolder> {

    private List<News> newsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title, date, category;

        /**
         * Provide a reference to the views that we are using.
         */
        public ViewHolder(View view) {
            super(view);

            // TODO: Define click listener for the ViewHolder's View?
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    showSnackBar(view, "Element " + position + " clicked.");
                }
            });

            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            category = (TextView) view.findViewById(R.id.category);
            date = (TextView) view.findViewById(R.id.date);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    /**
     * Initialise the dataset of the Adapter.
     */
    public NewsApdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    /**
     * Create new views (invoked by the layout manager).
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_news_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of views (invoked by the layout manager).
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.category.setText(news.getCategory());

        // Display date and time.
        Date date = stringToDate(news.getDate());
        String dateAndTime = dateToDateString(date) + ", " + dateToTimeString(date);
        holder.date.setText(dateAndTime);

        // Display the tumbnail image if it exists.
        if (news.getThumbnailBitmap() == null) {
            holder.thumbnail.setVisibility(View.INVISIBLE);
        } else {
            holder.thumbnail.setImageBitmap(news.getThumbnailBitmap());
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager).
     */
    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
