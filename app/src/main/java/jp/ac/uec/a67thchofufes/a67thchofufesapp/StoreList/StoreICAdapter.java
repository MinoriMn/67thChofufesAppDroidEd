package jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.TwitterAuthCredential;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;

/**
 * Created by minorim_n on 2017/02/17.
 */

public class StoreICAdapter extends ArrayAdapter<StoreInformationCard> {
    private List<StoreInformationCard> mCards;
    private int storeType = 0;

    //<code, number>
    private Map<Integer, Integer> typeData = new HashMap<Integer, Integer>();;


    StoreICAdapter(Context context, int layoutResourceId, List<StoreInformationCard> objects){
        super(context, layoutResourceId, objects);
        mCards = objects;
        typeData.put(0, 0);

    }

    /*
    public void setCode(List<StoreType> types){
        for(StoreType type : types){
            typeData.put(type.code, 0);
        }
    }
    */

    void addRequest(int code){
        typeData.put(code, (typeData.containsKey(code) ? typeData.get(code) : 0) + 1);
        System.out.println("addRCalled " + code +" " + typeData.get(code));
    }

    @Override
    public int getCount(){
        if(storeType == 0) {
            return mCards.size();
        }else{
            return typeData.get(storeType);
        }
    }

    @Override
    public StoreInformationCard getItem(int position){
        return mCards.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;
        StoreInformationCard item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_information_card, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }


        if(item.getTwitter() == null){
            viewHolder.Twitter.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.Twitter.setVisibility(View.VISIBLE);
        }

        if(item.getHP() == null){
            viewHolder.Cite.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.Cite.setVisibility(View.VISIBLE);
        }


        viewHolder.StoreOrga.setText(item.getStoreOrga());
        viewHolder.StoreName.setText(item.getStoreName());
        viewHolder.StoreIntro.setText(item.getIntroduction());
        Bitmap STimage= item.getStoreImage();
        if(STimage != null){
            viewHolder.StoreImage.setImageBitmap(STimage);
        }else{
            viewHolder.StoreImage.setImageResource(R.drawable.noimage);
            //Log.d("Image", "No Image");
        }

        return convertView;
    }

    void setStoreType(int storeType){
        this.storeType = storeType;
    }

    private class ViewHolder{
        ImageView StoreImage;
        TextView StoreOrga;
        TextView StoreName;
        TextView StoreIntro;
        ImageView Twitter, Cite;


        ViewHolder(View view){
            StoreImage = (ImageView)view.findViewById(R.id.icon);
            StoreOrga = (TextView)view.findViewById(R.id.StoreOrga);
            StoreName = (TextView)view.findViewById(R.id.StoreName);
            StoreIntro = (TextView)view.findViewById(R.id.StoreIntro);

            Twitter = (ImageView)view.findViewById(R.id.Twitter);
            Cite = (ImageView)view.findViewById(R.id.Cite);

           // Log.d("ViewHolder", "SetCalled");
        }
    }
}

