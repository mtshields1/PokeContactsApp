package com.example.owner.pokecontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        ImageView ivAvatar = (ImageView)convertView.findViewById(R.id.imgAvatar);
        ivAvatar.setImageBitmap(entry.getmAvatar());

        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getmName());

        TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getmPhone());

        return convertView;
    }
}
