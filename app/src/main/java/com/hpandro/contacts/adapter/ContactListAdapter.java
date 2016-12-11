package com.hpandro.contacts.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpandro.contacts.MyApplication;
import com.hpandro.contacts.R;
import com.hpandro.contacts.activities.ShowContactsActivity;
import com.hpandro.contacts.model.ContactData;
import com.hpandro.contacts.utils.UserBlankAvatar;

import java.util.ArrayList;

/**
 * Created by hpAndro on 12/8/2016.
 */
public class ContactListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ContactData> mItems;
    private ShowContactsActivity mActivity;

    public ContactListAdapter(Activity activity, ArrayList<ContactData> contactsdata) {

        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = contactsdata;
        mActivity = (ShowContactsActivity) activity;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_contact_list, parent, false);
            holder = new ViewHolder();
            holder.contact_photo = (ImageView) convertView.findViewById(R.id.contact_photo);
            holder.contact_photo_blank = (UserBlankAvatar) convertView.findViewById(R.id.contact_photo_blank);
            holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
            holder.contact_num = (TextView) convertView.findViewById(R.id.contact_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mItems.get(position).photoUri != null) {
            holder.contact_photo.setVisibility(View.VISIBLE);
            holder.contact_photo.setImageURI(mItems.get(position).photoUri);
            holder.contact_photo_blank.setVisibility(View.GONE);
        } else {
//            holder.contact_photo.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_launcher));
            holder.contact_photo.setVisibility(View.GONE);
            holder.contact_photo_blank.setVisibility(View.VISIBLE);
            MyApplication.getInstance().setBlankAvatar(holder.contact_photo_blank, mItems.get(position).name, true);
        }

        holder.contact_name.setText(mItems.get(position).name);
        holder.contact_num.setText(mItems.get(position).phonenum);

        return convertView;
    }

    public void setData(ArrayList<ContactData> datas) {
        mItems = datas;
    }

    private static class ViewHolder {
        public ImageView contact_photo;
        public UserBlankAvatar contact_photo_blank;
        public TextView contact_num, contact_name;
    }
}