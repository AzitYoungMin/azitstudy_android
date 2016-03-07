package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.trams.azit.R;
import com.trams.azit.model.TutModel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 11/4/2015.
 */
public class TutAdapter extends PagerAdapter {

    private static final String TAG = TutAdapter.class.getName();
    private Context context;
    private ArrayList<TutModel> tutModels;
    private LayoutInflater inflater;

    public TutAdapter(Context context, ArrayList<TutModel> tutModels) {
        this.context = context;
        this.tutModels = tutModels;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return tutModels.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final TutModel item = tutModels.get(position);
        View itemView = inflater.inflate(R.layout.item_tut, container, false);

        ImageView imgIcon = (ImageView) itemView.findViewById(R.id.img_tut_icon_item);
        imgIcon.setImageResource(item.getIdIcon());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //    public TutAdapter(Context context, ArrayList<TutModel> tutModels) {
//        super(context, R.layout.item_tut, tutModels);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final TutModel item = getItem(position);
//
//        final ViewHolder holder;
//        if (convertView == null) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//            convertView = layoutInflater.inflate(R.layout.item_tut, parent, false);
//            holder = new ViewHolder();
//            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_tut_icon_item);
//            holder.imgDes = (ImageView) convertView.findViewById(R.id.img_tut_des_icon);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.imgIcon.setImageResource(item.getIdIcon());
//        holder.imgDes.setImageResource(item.getIdDes());
//
//        return convertView;
//    }
//
    public static class ViewHolder {
        ImageView imgIcon;
        ImageView imgDes;
    }

}
