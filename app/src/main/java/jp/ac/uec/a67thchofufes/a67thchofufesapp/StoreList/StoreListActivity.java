package jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;

public class StoreListActivity extends AppCompatActivity {
    private int storeTypeIndex = 0;

    private List<StoreInformationCard> mCards;
    private List<StoreType> storeTypes;
    private StoreICAdapter mCardAdapter;
    private ListView mListview;
    private SharedPreferences pref;
    private AlertDialog.Builder selector;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://thchofufes.appspot.com/StoreImage/");

    private final long MAX_SIZE_BYTE = 1024 * 1024;

    private DatabaseReference refMsg;
    private DatabaseReference refMsgType;

    private TextView storeTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refMsg = database.getReference("StoreList");
        refMsgType = database.getReference("StoreType");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        setTitle( "模擬店一覧" );

        pref = getSharedPreferences("StoreInfo", Context.MODE_PRIVATE);

        storeTypeText = (TextView)findViewById(R.id.storeType);

        mListview = (ListView)findViewById(R.id.listView);
        mCards = new ArrayList<StoreInformationCard>();
        mCardAdapter = new StoreICAdapter(this, 0, mCards);
        mListview.setAdapter(mCardAdapter);
        storeTypes = new ArrayList<StoreType>();

        selector = new AlertDialog.Builder(this);

        refMsgType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null){return;}
                int key = 1;
                while (true) {
                    if(dataSnapshot.child(String.valueOf(key)).getValue() == null){break;}
                    StoreType storeType = new StoreType();
                    storeType.code = Integer.parseInt(dataSnapshot.child(String.valueOf(key)).child("Code").getValue().toString());
                    storeType.name = dataSnapshot.child(String.valueOf(key)).child("Name").getValue().toString();
                    System.out.println(storeType.name);
                    storeTypes.add(storeType);
                    key++;
                }
                //mCardAdapter.setCode(storeTypes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //final SharedPreferences pref = getSharedPreferences("StoreInfo", Context.MODE_PRIVATE);

        refMsgType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Finished?");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });



        refMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //データ取得
                //"s"には直前のKeyが入るらしい。特にST1であるところはNullとなっている。

                final StoreInformationCard value = dataSnapshot.getValue(StoreInformationCard.class);

                //idのセット
                //Log.d("GetData", String.format("Key = %s", dataSnapshot.getKey()));
                value.setId(Integer.parseInt(dataSnapshot.getKey()));

                if(value.getIconVer() != pref.getInt(value.getStoreOrga() + "IconVer", 0)) {
                //初期ダウンロード or VerUp
                    if (value.getStoreName() != null || value.getIntroduction() != null) {

                        //店舗画像のダウンロード
                        String Icon = value.getIcon();

                        if (Icon != null) {
                            StorageReference imageReference;
                            imageReference = storageReference.child(Icon + ".jpg");


                            imageReference.getBytes(MAX_SIZE_BYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Log.d("STBitmap", value.getStoreOrga() + " ダウンロード成功");

                                    Bitmap STbitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    value.setStoreImage(STbitmap);

                                    if(mCardAdapter != null){
                                        mCardAdapter.notifyDataSetChanged();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("STBitmap", value.getStoreOrga() + " ダウンロード失敗");
                                    Toast.makeText(getBaseContext(), "通信エラーが発生しています¥nしばらく時間を置くか、別の環境下でお試しください。", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    /*消したら上手く行ったが原因は謎
                    mCardAdapter.clear();
                    mCardAdapter.addAll(mCards);
                    */
                        Log.d("DatabaseAccess", "getValueSucceeded");

                    } else {
                        Log.d("DatabaseAccess", "getValueNulldata");
                    }
                }else{
                //既存の画像を呼び出す
                    Log.d("STBitmap", value.getStoreOrga() + " 既存呼び出し");
                    String BitmapStr = pref.getString(value.getStoreOrga() + "StoreIcon", "");
                    if (!BitmapStr.equals("")) {
                        byte[] b = Base64.decode(BitmapStr, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length).copy(Bitmap.Config.ARGB_8888, true);
                        value.setStoreImage(bitmap);
                    }

                }
                mCards.add(value);
                mCardAdapter.addRequest(value.getType());
                mCardAdapter.notifyDataSetChanged();
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

            }
        });

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {


                StoreInformationCard storeInformationCard = mCards.get(position);
                final String Twitter = storeInformationCard.getTwitter();
                final String HP = storeInformationCard.getHP();

                if(Twitter != null && HP != null) {

                    final String[] items = {"公式Twitter", "公式サイト"};
                    selector.setTitle("団体公式ページにアクセス");

                    selector.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Uri uri0 = Uri.parse(Twitter);
                                    Intent i0 = new Intent(Intent.ACTION_VIEW, uri0);
                                    startActivity(i0);
                                    break;
                                case 1:
                                    Uri uri1 = Uri.parse(HP);
                                    Intent i1 = new Intent(Intent.ACTION_VIEW, uri1);
                                    startActivity(i1);
                                    break;
                                default:
                            }
                        }
                    });

                    selector.show();

                }else{

                    if(Twitter != null){
                        Uri uri0 = Uri.parse(Twitter);
                        Intent i0 = new Intent(Intent.ACTION_VIEW, uri0);
                        startActivity(i0);
                    }else if(HP != null){
                        Uri uri1 = Uri.parse(HP);
                        Intent i1 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(i1);
                    }

                }

            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //SharedPreferences のファイルアクセスのタイミング
        //https://qiita.com/usamao/items/d7fbb19b508dc4cb5521

        if (refMsg != null){refMsg.onDisconnect(); refMsg = null;}
        if (refMsgType != null){refMsgType.onDisconnect(); refMsgType = null;}

        if(pref != null) {

            SharedPreferences.Editor editor = pref.edit();

            for (StoreInformationCard card : mCards) {
                if (card.getIconVer() != pref.getInt(card.getStoreOrga() + "IconVer", 0)) {

                    Bitmap bitmap = card.getStoreImage();
                    if (bitmap == null) {
                        continue;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    String bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                    Log.d("STBitmap", card.getStoreOrga() + " 画像保存");
                    editor.putString(card.getStoreOrga() + "StoreIcon", bitmapStr);
                    editor.putInt(card.getStoreOrga() + "IconVer", card.getIconVer());

                }
            }

            editor.apply();


        }
        releaseView();
    }

    private void releaseView() {
        if (mListview!= null) {
            mListview.setAdapter(null);
            mListview = null;
        }

        if (mCardAdapter != null) {
            mCardAdapter.clear();
            mCardAdapter = null;
        }

        if(mCards != null && !mCards.isEmpty()){
            mCards.clear();
            mCards = null;
        }

        if(storeTypes != null && !storeTypes.isEmpty()){
            storeTypes.clear();
            storeTypes = null;
        }

        storageReference = null;
        storeTypeText = null;
        selector = null;

        pref = null;

    }

    public void RightButton(View v){
        if(storeTypes.size() == 0){return;}
        storeTypeIndex = (storeTypeIndex + 1) % (storeTypes.size() + 1);

        if(storeTypeIndex != 0) {
            StoreType st = storeTypes.get(storeTypeIndex - 1);
            Collections.sort(mCards, new StoreInformationCardComparator(st.code));
            mCardAdapter.setStoreType(st.code);
            storeTypeText.setText(st.name);
        }else{
            Collections.sort(mCards, new StoreInformationCardComparator(0));
            mCardAdapter.setStoreType(0);
            storeTypeText.setText("オール");
        }
        mCardAdapter.notifyDataSetChanged();
    }

    public void LeftButton(View v){
        if(storeTypes.size() == 0){return;}
        storeTypeIndex = (storeTypeIndex + storeTypes.size()) % (storeTypes.size() + 1);
        if(storeTypeIndex != 0) {
            StoreType st = storeTypes.get(storeTypeIndex - 1);
            Collections.sort(mCards, new StoreInformationCardComparator(st.code));
            mCardAdapter.setStoreType(st.code);
            storeTypeText.setText(st.name);
        }else{
            Collections.sort(mCards, new StoreInformationCardComparator(0));
            mCardAdapter.setStoreType(0);
            storeTypeText.setText("オール");
        }
        mCardAdapter.notifyDataSetChanged();
    }
}


//ストアデータの構造体
class StoreType{
    public String name;
    public int code;
}
