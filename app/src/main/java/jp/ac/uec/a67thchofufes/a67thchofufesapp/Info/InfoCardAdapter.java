package jp.ac.uec.a67thchofufes.a67thchofufesapp.Info;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;
import jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList.StoreICAdapter;
import jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList.StoreInformationCard;

/**
 * Created by minorim_n on 2017/10/13.
 */

public class InfoCardAdapter  extends ArrayAdapter<InformationCard> {
    List<InformationCard> mCards;

    public InfoCardAdapter(Context context, int layoutResourceId, List<InformationCard> objects){
        super(context, layoutResourceId, objects);
        // Log.d("Adapter", "objects.exist = " + (objects != null));//S
        mCards = objects;
    }

    @Override
    public int getCount(){
        return mCards.size();
    }

    @Override
    public InformationCard getItem(int position){
        return mCards.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final InfoCardAdapter.ViewHolder viewHolder;
        InformationCard item = getItem(position);

        //Log.d("getView", "item.exist = " + (item != null));

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.information_card, parent, false);
            viewHolder = new InfoCardAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (InfoCardAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.DayText.setText(item.getDay());
        viewHolder.TitleText.setText(item.getTitle());
        viewHolder.ContentText.setText(item.getContent());

        Log.d("ViewHolder", "EnterCalled");

        return convertView;
    }

    private class ViewHolder{
        TextView DayText;
        TextView TitleText;
        TextView ContentText;


        public ViewHolder(View view){
            DayText = (TextView)view.findViewById(R.id.InfoDay);
            TitleText = (TextView)view.findViewById(R.id.InfoTitle);
            ContentText = (TextView)view.findViewById(R.id.InfoContent);

            Log.d("ViewHolder", "SetCalled");
        }
    }
}
