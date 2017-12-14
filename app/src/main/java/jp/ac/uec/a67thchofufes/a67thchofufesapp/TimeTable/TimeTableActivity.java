package jp.ac.uec.a67thchofufes.a67thchofufesapp.TimeTable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;

public class TimeTableActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TimeTablePageFragmentActivity.OnFragmentInteractionListener {
    /*
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    */

    private StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://thchofufes.appspot.com/TimeTable/");
    private int ver;
    private DatabaseReference refMsg;

    static long MAX_SIZE_BYTE = 1024 * 1024;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        setTitle( "タイムテーブル" );

        DatabaseReference refMsg = FirebaseDatabase.getInstance().getReference("TimeTable");

        final SharedPreferences pref = getSharedPreferences("TableInfo", Context.MODE_PRIVATE);

        //更新
        refMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    final TimeTableCard value = dataSnapshot.getValue(TimeTableCard.class);
                    ver = value.getVer();

                    final String dpage = dataSnapshot.getKey();

                    Log.d("TimeTable", "dataSnapshotVer" + ver + dpage);
                    if (pref == null) {
                        return;
                    }
                    if (pref.getInt(dpage + "TableVer", 6) != ver) {
                        StorageReference imageReference;
                        imageReference = storageReference.child(dpage + ".png");

                        imageReference.getBytes(MAX_SIZE_BYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Log.d("TimeTable", "ダウンロード成功 " + dpage);

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                if (bitmap == null) {
                                    return;
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                String bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                                Log.d("TimeTable", "画像保存 " + dpage);
                                pref.edit().putString(dpage + "TableImage", bitmapStr).apply();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("STBitmap", "ダウンロード失敗 " + dpage);
                                Toast.makeText(getBaseContext(), "通信エラーが発生しています¥nしばらく時間を置くか、別の環境下でお試しください。", Toast.LENGTH_SHORT).show();
                            }
                        });

                        pref.edit().putInt(dpage + "TableVer", ver).apply();
                        pref.edit().putString(dpage + "TableDate", value.getDate()).apply();

                    } else {
                        Log.d("TimeTable", "Ver既存" + dpage);
                    }
                }catch (DatabaseException e){
                    System.out.println(e.toString());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //変更後の表示はアクティビティを再びIntentした時とする
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("TT通信エラー");
                Toast.makeText(getBaseContext(), "通信エラーが発生しています¥nしばらく時間を置くか、別の環境下でお試しください。", Toast.LENGTH_LONG).show();
            }
        });

        // xmlからTabLayoutの取得
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // xmlからViewPagerを取得
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        // ページタイトル配列
        final String[] pageTitle = {"1日目", "2日目", "3日目"};

        // 表示Pageに必要な項目を設定
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TimeTablePageFragmentActivity.newInstance(position + 1);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitle[position];
            }

            @Override
            public int getCount() {
                return pageTitle.length;
            }
        };

        // ViewPagerにページを設定
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);

        // ViewPagerをTabLayoutを設定
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TimeTableBitmap",  outState.toString());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("TT Destroy");
    if (refMsg != null) {refMsg.onDisconnect(); refMsg = null;}
        storageReference = null;
    }
}

class TimeTableCard {
    private int Ver = 6;
    private String Date = "error";

    public TimeTableCard(){
        //firebase用の空コンストラクタ

    }

    public TimeTableCard(String Date, int Ver){
        this.Date = Date;
        this.Ver = Ver;
    }

    public void setVer(int Ver){
        this.Ver = Ver;
    }
    public void setDate(String Date){
        this.Date = Date;
    }
    int getVer(){
        System.out.println("TTgetVer " + Ver);
        return Ver;
    }
    String getDate(){
        System.out.println("TTgetDate " + Date);
        return Date;
    }
}