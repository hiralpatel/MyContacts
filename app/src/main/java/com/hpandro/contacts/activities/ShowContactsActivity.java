package com.hpandro.contacts.activities;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.Manifest;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import com.hpandro.contacts.model.ContactData;
import com.hpandro.contacts.adapter.ContactListAdapter;
import com.hpandro.contacts.R;

/**
 * Created by hpAndro on 12/8/2016.
 */
public class ShowContactsActivity extends AppCompatActivity {

    private ContactListAdapter adapter;
    private ListView contactListView;
    private TextView tvEmptyView;

    EditText searchbox;
    ImageView searchIcon, searchCancel;
    String searchQuery;
    private ArrayList<ContactData> addressBookDatas;
    private ArrayList<ContactData> addressBookDatasSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_contacts);

        tvEmptyView = (TextView) findViewById(R.id.tvEmptyView);

        contactListView = (ListView) findViewById(R.id.contactlistview);
        contactListView.setDivider(null);
        addressBookDatas = new ArrayList<>();
        adapter = new ContactListAdapter(ShowContactsActivity.this, addressBookDatas);

        addressBookDatasSearch = new ArrayList<>();
        contactListView.setAdapter(adapter);
        requestMultiplePermissions();

        searchbox = (EditText) findViewById(R.id.send_txt_box);

        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = searchbox.getText().toString().trim();
                if (searchQuery.length() != 0) {
//                    if (!isLoading)
                    getAllPhoneContacts(searchQuery);
                } else if (searchbox.getText().toString().length() == 0) {
                    adapter.setData(addressBookDatas);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                searchQuery = searchbox.getText().toString().trim();
//                if (searchQuery.length() != 0) {
////                    if (!isLoading)
//                    getAllPhoneContacts(searchQuery);
//                } else if (searchbox.getText().toString().length() == 0) {
//                    servicesView.setVisibility(View.VISIBLE);
//                    recycler_view_friends.setVisibility(View.GONE);
//                }
            }
        });
        searchIcon = (ImageView) findViewById(R.id.send_search_icon);
        searchCancel = (ImageView) findViewById(R.id.send_search_cancel_btn);

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox.setText("");
                addressBookDatasSearch.clear();
                adapter.setData(addressBookDatas);
                adapter.notifyDataSetChanged();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = searchbox.getText().toString();
                if (searchQuery.length() != 0) {
//                    if (!isLoading)
                    getAllPhoneContacts(searchQuery);
                }
            }
        });
    }

    private void requestMultiplePermissions() {
        String readContactsPermission = Manifest.permission.READ_CONTACTS;
        int hasReadContactsPermission = ContextCompat.checkSelfPermission(ShowContactsActivity.this, readContactsPermission);

        List<String> permissions = new ArrayList<String>();
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(readContactsPermission);
        }

        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(params, 1);
            }
        } else {
            getAddressBookInfos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == permissions.length) {

                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        return;
                }
                getAddressBookInfos();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Search contact.
     *
     * @param query
     */
    private void getAllPhoneContacts(String query) {
        int searchListLength = addressBookDatas.size();
        addressBookDatasSearch.clear();
        for (int i = 0; i < searchListLength; i++) {
            if (addressBookDatas.get(i).getName().contains(query)) {
                addressBookDatasSearch.add(addressBookDatas.get(i));
            }
        }
        adapter.setData(addressBookDatasSearch);
        adapter.notifyDataSetChanged();
    }

    private void getAddressBookInfos() {
        addressBookDatas.clear();

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String PHOTO_URI = ContactsContract.CommonDataKinds.Phone.PHOTO_URI;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
        int PHONE_TYPEMOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Uri photo_uri;
//                Bitmap photoBmp;
                String phone = null;
                String name = "";

                String id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String url = cursor.getString(cursor.getColumnIndex(PHOTO_URI));
                if (url != null)
                    photo_uri = Uri.parse(url);
                else
                    photo_uri = null;

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, new String[]{PHONE_NUMBER},
                            PHONE_CONTACT_ID + " = ? AND " + PHONE_TYPE + " = " + PHONE_TYPEMOBILE, new String[]{id}, null);

                    while (phoneCursor.moveToNext()) {
                        phone = phoneCursor.getString(phoneCursor.getColumnIndex(PHONE_NUMBER));
                    }

                    addressBookDatas.add(new ContactData(photo_uri, name, phone, false));

                    phoneCursor.close();
                }

                //TODO for sord array as alphabetical order
                Collections.sort(addressBookDatas, new Comparator<ContactData>() {
                    public int compare(ContactData p1, ContactData p2) {
                        return p1.getName().compareTo(p2.getName());
                    }
                });

//                if (photo_uri != null){
//                    try{
//                        photoBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(photo_uri));
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
            }

            if (addressBookDatas.size() > 0) {
                adapter.setData(addressBookDatas);
                adapter.notifyDataSetChanged();
                tvEmptyView.setVisibility(View.GONE);
            } else {
                tvEmptyView.setVisibility(View.VISIBLE);
                contactListView.setEmptyView(tvEmptyView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.in_slide_left_right, R.anim.fade_out);
    }
}