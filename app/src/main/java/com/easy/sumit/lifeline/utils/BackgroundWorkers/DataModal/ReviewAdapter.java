package com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal;

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

public class ReviewAdapter extends BaseAdapter implements OnClickListener {

    private static LayoutInflater inflater = null;
    private ReviewModel tempValues = null;
    private ArrayList data;
    public ReviewAdapter(Fragment fragment, ArrayList data) {
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

        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.review_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name_text);
            holder.user_name= (TextView) view.findViewById(R.id.from_name_text);
            holder.date = (TextView) view.findViewById(R.id.date_text);
            holder.time= (TextView) view.findViewById(R.id.time_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() <= 0) {
            holder.name.setText("No Data");
        }
        else {
            tempValues = null;
            tempValues = (ReviewModel) data.get(position);

            holder.name.setText(tempValues.getFrom_user());
            holder.user_name.setText(tempValues.getFrom_user_name());
            holder.date.setText(tempValues.getDate());
            holder.time.setText(tempValues.getTime());
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked");
    }

    public static class ViewHolder {

        public TextView name;
        public TextView user_name;
        public TextView date;
        public TextView time;

    }
}