package jp.ac.uec.a67thchofufes.a67thchofufesapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MapOfLocateUecActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_of_locate_uec);

        setTitle("アクセス&マップ");
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        System.out.println("onMapReadyCalled");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            System.out.println("setMyLocation true");
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                35.656350f,
                139.542691f
        ), 16.4f));

        DatabaseReference refMsg = FirebaseDatabase.getInstance().getReference("MapLocate");

        refMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final MapLocateCard value = dataSnapshot.getValue(MapLocateCard.class);
                LatLng Pos = new LatLng(value.getLatitude(), value.getLongitude());
                switch(value.getType()){
                    case 0:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin0)));
                        break;
                    case 1:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1)));
                        break;
                    case 2:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin2)));
                        break;
                    case 3:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3)));
                        break;
                    case 4:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin4)));
                        break;
                    case 5:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin5)));
                        break;
                    case 6:
                        map.addMarker(new MarkerOptions()
                                .position(Pos)
                                .title(value.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin6)));
                        break;
                    default:
                        map.addMarker(new MarkerOptions().position(Pos).title(value.getTitle()));
                        break;
                }

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
        mapFragment = null;
    }
}

class MapLocateCard{
    private String Title = null;
    private float Latitude;
    private float Longitude;
    private int Type;

    public MapLocateCard(){}

    public MapLocateCard(String Title, float Latitude, float Longitude, int Type){
        this.Title = Title;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Type = Type;
    }

    public String getTitle(){
        return Title;
    }
    public float getLatitude(){
        return Latitude;
    }
    public float getLongitude(){
        return Longitude;
    }
    public int getType(){
        return Type;
    }
}
