package com.trams.azit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.RatingItem;

import java.util.List;

/**
 * Created by zin9x on 1/15/2016.
 */
public class RatingAdapter extends ArrayAdapter<RatingItem> {
    private static final String TAG = StudentPlusAdapter.class.getName();
    private Context context;

    public RatingAdapter(Context _context, List<RatingItem> students) {
        super(_context, R.layout.item_rating, students);
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RatingItem item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_rating, parent, false);
            holder = new ViewHolder();
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            holder.tvStandardizedScore = (TextView) convertView.findViewById(R.id.tvStandardizedScore);
            holder.tvOriginalScore = (TextView) convertView.findViewById(R.id.tvOriginalScore);
            holder.tvTier = (TextView) convertView.findViewById(R.id.tvTier);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMonth.setText(item.getMonth()+"월");
        holder.tvStandardizedScore.setText(item.getStandard());
        holder.tvOriginalScore.setText(item.getOrigin());
        holder.tvTier.setText(item.getRating());

        return convertView;
    }

    public static class ViewHolder {
        TextView tvMonth;
        TextView tvStandardizedScore;
        TextView tvOriginalScore;
        TextView tvTier;
    }
}
