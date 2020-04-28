package com.easy.sumit.lifeline.utils;

import android.Manifest;
import android.content.ContentProviderOperation;
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
    private ArrayList<ContentProviderOperation> arrayList;

    public ContactHandler(Context context) {
        this.context = context;
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
            Log.i("CONTACT","ADDED Successfully, "+contact_no);
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
    }

    public void deleteLog() {
        try{
            String logKey[] = {contact_no};
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Cursor cursor = context
                    .getContentResolver()
                    .query(CallLog.Calls.CONTENT_URI,
                            null, CallLog.Calls._ID + " = ? ", logKey, null);
            boolean bol = cursor.moveToFirst();
            if (bol) {
                do {
                    int idOfRowToDelete = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
                    context.getContentResolver().delete(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI,
                            String.valueOf(idOfRowToDelete)),null,null);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }catch (UnsupportedOperationException e){
            e.printStackTrace();
        }
    }
    /*
    12-29 19:08:54.408 2016-14977/? E/DatabaseUtils: Writing exception to parcel
     java.lang.UnsupportedOperationException: Cannot delete that URL: content://call_log/calls/8
         at com.android.providers.contacts.CallLogProvider.delete(CallLogProvider.java:368)
         at android.content.ContentProvider$Transport.delete(ContentProvider.java:339)
         at android.content.ContentProviderNative.onTransact(ContentProviderNative.java:206)
         at android.os.Binder.execTransact(Binder.java:453)
     */
}