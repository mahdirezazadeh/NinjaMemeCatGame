package com.memegames.ninjacat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    private int[] imge;
    private int[] rating_stars;

    public CustomAdapter(Context context, String[] text1,int[] imageIds, int[] ratings) {
        mContext = context;
        Title = text1;
        imge = imageIds;
        rating_stars = ratings;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row;
        row = inflater.inflate(R.layout.level_list_item, parent, false);

        TextView title;
        ImageView i1;
        ImageView ratings;

        i1 = (ImageView) row.findViewById(R.id.level_image);
        title = (TextView) row.findViewById(R.id.level_name);
        ratings = (ImageView) row.findViewById(R.id.level_star);

        title.setText(Title[position]);
        i1.setImageResource(imge[position]);
        ratings.setImageResource(rating_stars[position]);

        return (row);
    }
}