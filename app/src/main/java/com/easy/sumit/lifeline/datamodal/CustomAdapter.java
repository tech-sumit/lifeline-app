package com.easy.sumit.lifeline.datamodal;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easy.sumit.lifeline.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements OnClickListener {

    private static LayoutInflater inflater = null;
    private ListModel tempValues = null;
    private ArrayList data;
    public CustomAdapter(Fragment fragment, ArrayList data) {
        this.data = data;
        inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.name_text);
            holder.hivText = (TextView) vi.findViewById(R.id.last_donated_text);
            holder.genderText = (TextView) vi.findViewById(R.id.gender_text);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (data.size() <= 0) {
            holder.name.setText("No Data");
        }
        else {
            tempValues = null;
            tempValues = (ListModel) data.get(position);

            holder.name.setText(tempValues.getName());
            holder.genderText.setText(tempValues.getGender());
            holder.hivText.setText(tempValues.getLastDonated());
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked");
    }

    public static class ViewHolder {

        public TextView name;
        public TextView hivText;
        public TextView genderText;

    }
}