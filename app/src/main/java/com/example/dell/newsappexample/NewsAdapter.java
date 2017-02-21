package com.example.dell.newsappexample;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DELL on 09-02-2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Activity context, ArrayList<News> news ) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //check if the existing view is being reused , otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        News currentNews = getItem(position);

        //Find the Textview in the list.
        TextView title = (TextView)listItemView.findViewById(R.id.sectionView);
        String titleForView = currentNews.getNewsTitle();
        title.setText(titleForView);

        //Find the Textview in the list.
        TextView section = (TextView)listItemView.findViewById(R.id.titleView);
        String sectionView = currentNews.getSectionTitle();
        section.setText(sectionView);

        return listItemView;
    }
}
