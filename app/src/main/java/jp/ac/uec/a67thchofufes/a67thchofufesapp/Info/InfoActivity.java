package jp.ac.uec.a67thchofufes.a67thchofufesapp.Info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.ac.uec.a67thchofufes.a67thchofufesapp.R;


public class InfoActivity extends AppCompatActivity {
    List<InformationCard> mCards;
    InfoCardAdapter mCardAdapter;
    ListView mListview;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refMsg = database.getReference("Info");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setTitle( "お知らせ" );

        mListview = (ListView)findViewById(R.id.InfoList);
        mCards = new ArrayList<InformationCard>();
        mCardAdapter = new InfoCardAdapter(this, 0, mCards);
        mListview.setAdapter(mCardAdapter);

        /*デバッグ用
        InformationCard value = new InformationCard("10/10", "テスト表示", "fdngjsngflksfgnlkslkklsgklsjkbjglsfgkls");
        mCards.add(value);
        mCardAdapter.notifyDataSetChanged();
        */


        refMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final InformationCard value = dataSnapshot.getValue(InformationCard.class);

                mCards.add(value);
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(refMsg != null){refMsg.onDisconnect();  refMsg = null;}
        if (mListview!= null) {mListview.setAdapter(null); mListview = null;}
        if (mCardAdapter != null) {mCardAdapter.clear(); mCardAdapter = null;}
        if(mCards != null){mCards.clear(); mCards = null;}
    }
}

