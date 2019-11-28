package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.News;
import com.example.android.newsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hp on 8/30/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String Location_seperator = "T";

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_layout, parent, false);
        }

        News currentNews = getItem(position);

        TextView headline = (TextView) convertView.findViewById(R.id.headline);
        headline.setText(currentNews.getHeadline().trim());

        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText((Html.fromHtml(currentNews.getContent()).toString().trim()));

        TextView sectionName = (TextView) convertView.findViewById(R.id.section);
        sectionName.setText(currentNews.getSectionName().trim());
        String oringinalTime = currentNews.getmPublicationDate();
        String date;
        String time1;
        String[] parts = oringinalTime.split(Location_seperator);
        date = parts[0];
        time1 = parts[1];
        StringBuilder sb = new StringBuilder(time1);
        sb.deleteCharAt(8);
        String time = sb.toString();
        TextView datetxt = (TextView) convertView.findViewById(R.id.date);
        datetxt.setText(date);
        TextView timetext = (TextView) convertView.findViewById(R.id.time);
        timetext.setText(time);

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText("by - " + currentNews.getAuthor());

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.news_img);

        Picasso.with(getContext()).load(currentNews.getImageUrl()).into(thumbnail);


        return convertView;
    }
}
