package jp.ac.uec.a67thchofufes.a67thchofufesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.Info.InfoActivity;
import jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList.StoreListActivity;
import jp.ac.uec.a67thchofufes.a67thchofufesapp.TimeTable.TimeTableActivity;

public class MainActivity extends AppCompatActivity{
    private int Value = 0;
    private SharedPreferences pref;
    private boolean isInfoicon = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ImageView) findViewById(R.id.maintitle)).setImageResource(R.drawable.title);
        ((ImageView) findViewById(R.id.oficialcite)).setImageResource(R.drawable.chofufes_official_site);
        ((ImageView) findViewById(R.id.ListofStore)).setImageResource(R.drawable.chofufes_storelist);
        ((ImageView) findViewById(R.id.Map)).setImageResource(R.drawable.chofufes_map);
        ((ImageView) findViewById(R.id.TimeTable)).setImageResource(R.drawable.chofufes_timetable);
        ((ImageView) findViewById(R.id.Info)).setImageResource(R.drawable.chofufes_info);

        setTitle( "" );

        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        //お知らせのデータ参照
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refMsg = database.getReference("InfoVer");

        pref = getSharedPreferences("Info", Context.MODE_PRIVATE);

        refMsg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Value = dataSnapshot.getValue() != null ? Integer.parseInt(dataSnapshot.getValue().toString()) : 0;
                if(pref.getInt("Ver", 0) < Value){
                    ((ImageView)findViewById(R.id.newInfo)).setImageResource(R.drawable.new_info);
                    isInfoicon = true;
                    System.out.println("That value is more than existing value is true.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //push通知の準備
        FirebaseMessaging.getInstance().subscribeToTopic("Push");
    }

    //cite
    public void LoadOfficialSite(View v){
        Uri uri0 = Uri.parse("http://www.chofusai.uec.ac.jp");
        Intent i0 = new Intent(Intent.ACTION_VIEW, uri0);
        startActivity(i0);
    }

    //地図表示
    public void LoadMap(View v){
        DestroyImage();
        Intent intent = new Intent(this, MapOfLocateUecActivity.class);
        startActivityForResult(intent, 0);
    }

    //模擬店一覧
    public void LoadStorelist(View v){
        DestroyImage();
        Intent intent = new Intent(this, StoreListActivity.class);
        startActivityForResult(intent, 0);
    }

    public void LoadTimeTable(View v){
        DestroyImage();
        Intent intent = new Intent(this, TimeTableActivity.class);
        startActivityForResult(intent, 0);
    }
    public void LoadInfo(View v){
        DestroyImage();
        Intent intent = new Intent(this, InfoActivity.class);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Ver", Value);
        editor.apply();
        ((ImageView)findViewById(R.id.newInfo)).setImageBitmap(null);
        isInfoicon = false;
        startActivityForResult(intent, 0);
    }

    public void DestroyImage(){
            ((ImageView) findViewById(R.id.maintitle)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.oficialcite)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.ListofStore)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.Map)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.TimeTable)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.newInfo)).setImageBitmap(null);
            ((ImageView) findViewById(R.id.Info)).setImageBitmap(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((ImageView) findViewById(R.id.maintitle)).setImageResource(R.drawable.title);
        ((ImageView) findViewById(R.id.oficialcite)).setImageResource(R.drawable.chofufes_official_site);
        ((ImageView) findViewById(R.id.ListofStore)).setImageResource(R.drawable.chofufes_storelist);
        ((ImageView) findViewById(R.id.Map)).setImageResource(R.drawable.chofufes_map);
        ((ImageView) findViewById(R.id.TimeTable)).setImageResource(R.drawable.chofufes_timetable);
        ((ImageView) findViewById(R.id.Info)).setImageResource(R.drawable.chofufes_info);
        if(isInfoicon) {
            ((ImageView)findViewById(R.id.newInfo)).setImageResource(R.drawable.new_info);
        }
    }

}
