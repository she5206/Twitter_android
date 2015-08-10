package com.example.mchsieh.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mchsieh on 8/2/15.
 */
public class PopularPhotosAdapter extends ArrayAdapter<PopularPhotos> {

    public PopularPhotosAdapter(Context context, List<PopularPhotos> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularPhotos photo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // get object
        ImageView ivProfilePhoto = (ImageView)convertView.findViewById(R.id.ivProfilePhoto);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvTimestamp = (TextView)convertView.findViewById(R.id.tvTimestamp);
        TextView tvLikeCount = (TextView)convertView.findViewById(R.id.tvLikeCount);
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);

        // set value
        // profile
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        ivProfilePhoto.setImageResource(0); // clear img every time!!!
        Picasso.with(getContext()).load(photo.profile_img_url)
                .transform(new RoundedTransformation(100, 0))
                .into(ivProfilePhoto);
        // username
        tvUserName.setText(photo.user_name);
        // timestamp
        Date date = new Date(Long.parseLong(photo.createdTime)*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom
        tvTimestamp.setText(timeAgoInWords(date));
        // img
        ivPhoto.setImageResource(0); // clear img every time!!!
        Picasso.with(getContext()).load(photo.img_url).into(ivPhoto);
        // like count
        tvLikeCount.setText(String.valueOf(photo.like_count)+" like");
        // caption
        tvCaption.setText(photo.caption);


        return convertView;
    }

    public static String timeAgoInWords(Date from) {
        Date now = new Date();
        long difference = now.getTime() - from.getTime();
        long distanceInMin = difference / 60000;

        if ( 0 <= distanceInMin && distanceInMin <= 1 ) {
            return "1 minute";
        } else if ( 1 <= distanceInMin && distanceInMin <= 45 ) {
            return distanceInMin + " minutes";
        } else if ( 45 <= distanceInMin && distanceInMin <= 89 ) {
            return "1 hour";
        } else if ( 90 <= distanceInMin && distanceInMin <= 1439 ) {
            return (distanceInMin / 60) + " hours";
        } else if ( 1440 <= distanceInMin && distanceInMin <= 2529 ) {
            return "1 day";
        } else if ( 2530 <= distanceInMin && distanceInMin <= 43199 ) {
            return (distanceInMin / 1440) + "days";
        } else if ( 43200 <= distanceInMin && distanceInMin <= 86399 ) {
            return "1 month";
        } else if ( 86400 <= distanceInMin && distanceInMin <= 525599 ) {
            return (distanceInMin / 43200) + " months";
        } else {
            long distanceInYears = distanceInMin / 525600;
            return distanceInYears + " years ago";
        }
    }
}

