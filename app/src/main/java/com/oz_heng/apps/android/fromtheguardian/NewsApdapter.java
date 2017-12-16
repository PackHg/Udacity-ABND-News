package com.oz_heng.apps.android.fromtheguardian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.dateToDateString;
import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.dateToTimeString;
import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.stringToDate;


/**
 * {@link NewsApdapter} provides the layout for each list News item based on a data
 * source.
 */
public class NewsApdapter extends ArrayAdapter<News> {

    public NewsApdapter(@NonNull Context context, @NonNull ArrayList<News>newsList) {
        super(context, 0, newsList);
    }

    static class ViewHolder {
        public ImageView thumbnail;
        public TextView title, date, category;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_news_item, parent,
                    false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        }

        News news = getItem(position);

        if (news != null) {
            if (!news.getTitle().isEmpty()) {
                holder.title.setText(news.getTitle());
            } else {
                holder.title.setText(R.string.no_title);
            }

            if (!news.getCategory().isEmpty()) {
                holder.category.setText(R.string.no_category);
            }

            if (!news.getDate().isEmpty()) {
                Date date = stringToDate(news.getDate());
                String s = dateToDateString(date) + ", " + dateToTimeString(date);
                holder.date.setText(s);
            } else {
                holder.date.setText(R.string.no_date);
            }

            if (news.getThumbnailBitmap() != null) {
                holder.thumbnail.setImageBitmap(news.getThumbnailBitmap());
            }
        }

        return convertView;
    }
}