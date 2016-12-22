package com.example.owner.pokecontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import java.util.ArrayList;

/**
 * Created by Owner on 11/29/2016.
 */

public class PhoneBookAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Android_Contact> mListPhoneBook;

    public PhoneBookAdapter(Context context, ArrayList<Android_Contact> list)
    {
        mContext = context;
        mListPhoneBook = list;
    }

    @Override
    public int getCount()
    {
        return mListPhoneBook.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mListPhoneBook.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Android_Contact entry = mListPhoneBook.get(position);

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.phonebook_row, null);
        }

        //-----< this is where the gif avatars will be set >-----
        if (entry.getmName().toString().equals("Mekem")){
            WebView wv = (WebView)convertView.findViewById(R.id.webView);
            wv.loadUrl("file:///android_asset/shiny_shinx.gif");
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.setClickable(false);
            wv.setLongClickable(false);
            wv.setFocusable(false);
            wv.setFocusableInTouchMode(false);
        }
        else if (entry.getmName().toString().equals("Yo Khurl")){
            WebView wv = (WebView)convertView.findViewById(R.id.webView);
            wv.loadUrl("file:///android_asset/hitmonchan.gif");
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.setClickable(false);
            wv.setLongClickable(false);
            wv.setFocusable(false);
            wv.setFocusableInTouchMode(false);
        }
        else if (entry.getmName().toString().equals("Dan Dan")){
            WebView wv = (WebView)convertView.findViewById(R.id.webView);
            wv.loadUrl("file:///android_asset/salamence.gif");
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.setClickable(false);
            wv.setLongClickable(false);
            wv.setFocusable(false);
            wv.setFocusableInTouchMode(false);
        }
        else
        {
            WebView wv = (WebView)convertView.findViewById(R.id.webView);
            wv.loadUrl("file:///android_asset/pidgey.gif");
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.setClickable(false);
            wv.setLongClickable(false);
            wv.setFocusable(false);
            wv.setFocusableInTouchMode(false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getmName());

        TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getmPhone());

        return convertView;
    }
}
