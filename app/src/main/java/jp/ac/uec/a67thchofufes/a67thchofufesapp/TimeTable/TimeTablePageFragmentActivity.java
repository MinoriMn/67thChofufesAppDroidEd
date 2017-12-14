package jp.ac.uec.a67thchofufes.a67thchofufesapp.TimeTable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;


public class TimeTablePageFragmentActivity extends Fragment {

    private static final String ARG_PARAM = "page";
    private OnFragmentInteractionListener mListener;

    // コンストラクタ
    public TimeTablePageFragmentActivity() {
    }

    public static TimeTablePageFragmentActivity newInstance(int page) {
        TimeTablePageFragmentActivity fragment = new TimeTablePageFragmentActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam = getArguments().getString(ARG_PARAM);
        }
    }


    private SharedPreferences pref;
    private int page;
    private View view;
    private TextView tableText;
    private ImageView timetableimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pref = getContext().getSharedPreferences("TableInfo", Context.MODE_PRIVATE);

        page = getArguments().getInt(ARG_PARAM, 0);

        view = inflater.inflate(R.layout.activity_time_table_page_fragment, container, false);

        tableText = (TextView)view.findViewById(R.id.VerText);
        timetableimage = (ImageView)view.findViewById(R.id.TimeTableImage);

        //既存呼び出し
        Log.d("TimeTableBitmap", "d" + page + " 既存呼び出し");
        String BitmapStr = pref.getString( "d" + page + "TableImage", "");

        //初期設定
        tableText.setText("Ver." + pref.getInt( "d" + page + "TableVer", 6) + " " + pref.getString( "d" + page + "TableDate", "10/31"));

        if (!BitmapStr.equals("")) {
            byte[] b = Base64.decode(BitmapStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length).copy(Bitmap.Config.ARGB_8888, true);
            timetableimage.setImageBitmap(bitmap);

        }else{
            int imageId = getResources().getIdentifier("d" + page, "drawable", getContext().getPackageName());
            timetableimage.setImageResource(imageId);
            System.out.println("画像データが存在しません");
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        System.out.println("Detach");

        pref = null;
        view = null;
        tableText = null;
        if(timetableimage != null) {
            timetableimage.setImageBitmap(null);
        }
        mListener = null;

        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}