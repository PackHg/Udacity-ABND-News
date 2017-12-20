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
 * {@link NewsAdapter} provides the layout for each list News item based on a data
 * source.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(@NonNull Context context, @NonNull ArrayList<News>newsList) {
        super(context, 0, newsList);
    }

    static class ViewHolder {
        ImageView thumbnail;
        TextView title, authors, date, section;
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
            holder.authors = (TextView) convertView.findViewById(R.id.authors);
            holder.section = (TextView) convertView.findViewById(R.id.section);
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

            if (!news.getAuthors().isEmpty()) {
                holder.authors.setText(news.getAuthors());
            } else {
                holder.authors.setText(R.string.no_author);
            }

            if (!news.getSection().isEmpty()) {
                holder.section.setText(news.getSection());
            } else {
                holder.section.setText(R.string.no_section);
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
