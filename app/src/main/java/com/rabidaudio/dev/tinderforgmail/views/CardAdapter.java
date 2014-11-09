//package com.rabidaudio.dev.tinderforgmail.views;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.rabidaudio.dev.tinderforgmail.Email;
//import com.rabidaudio.dev.tinderforgmail.R;
//
//import java.util.List;
//
///**
// * Created by charles on 11/8/14.
// */
//public class CardAdapter extends ArrayAdapter<Email> {
//    private static final String TAG = CardAdapter.class.getSimpleName();
//
//    private static final int LAYOUT_ID = R.layout.card; //the layout to use as the base
//
//    private Context context;
//    private List<Email> emails;
//
//    public CardAdapter(Context context, List<Email> emails){
//        super(context, LAYOUT_ID, emails);
//        this.emails = emails;
//        this.context = context;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent){
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        View rowView;
//        ViewHolder h;
//        if(view==null){
//            Log.d(TAG, "making new view");
//            rowView = inflater.inflate(LAYOUT_ID, parent, false);
//            h = new ViewHolder(rowView);
//            rowView.setTag(h);
//        }else{
//            Log.d(TAG, "recycling view");
//            rowView = view;
//            h = (ViewHolder) rowView.getTag();
//            if(h==null){
//                Log.w(TAG, "for some reason, no holder attached");
//                h = new ViewHolder(rowView);
//                rowView.setTag(h);
//            }
//        }
////        h.name.setText(WifiDirectManager.getName(devices[position]));
//        return rowView;
//    }
//
//    /**
//     * Used for caching references to sub-elements. If you change elements in the view template,
//     * you will probably need to update this.
//     * See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
//     */
//    private static class ViewHolder {
//        TextView title;
//        TextView name;
//        ImageView icon;
//
//        public ViewHolder(View v){
////            title = (TextView) v.findViewById(R.id.title);
////            name = (TextView) v.findViewById(R.id.name);
////            icon = (ImageView) v.findViewById(R.id.icon);
//        }
//    }
//}
