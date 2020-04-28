package com.easy.sumit.lifeline.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

public class ContactHandler {
    private String displayName = "Lifeline";
    private String contact_no = "";
    private Context context;
    private Activity  activity;
    private ArrayList<ContentProviderOperation> arrayList;

    public ContactHandler(Activity activity) {
        this.activity=activity;
        context=activity.getBaseContext();
    }

    public void setContact(String contact_no){
        this.contact_no =contact_no;
    }

    public void createContact() {
        arrayList = new ArrayList<>();
        arrayList.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (displayName != null) {
            arrayList.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }
        if (contact_no != null) {
            arrayList.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact_no)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, arrayList);
            Log.i("**CONTACT","ADDED Successfully, "+contact_no);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteContact() {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contact_no));
        Cursor cursor = context.getContentResolver().query(contactUri, null, null, null, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex(
                            ContactsContract.PhoneLookup.DISPLAY_NAME))
                            .equalsIgnoreCase(displayName)) {
                        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        context.getContentResolver().delete(uri, null, null);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        cursor.close();
    }

    public void deleteLog() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.WRITE_CALL_LOG}, 1);
        }

        ContentResolver contentResolver = context.getContentResolver();
        String nameSelector = CallLog.Calls.NUMBER+"="+contact_no;
        int num_deleted=contentResolver.delete(CallLog.Calls.CONTENT_URI,nameSelector,null);
        Log.i("**Call Logs affected", "Count: " + num_deleted+"\nnameSelector:"+nameSelector);
    }
}

