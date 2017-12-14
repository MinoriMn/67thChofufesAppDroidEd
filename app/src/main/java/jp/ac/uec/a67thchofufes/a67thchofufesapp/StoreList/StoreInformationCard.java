package jp.ac.uec.a67thchofufes.a67thchofufesapp.StoreList;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * Created by minorim_n on 2017/02/17.
 */

public class StoreInformationCard{
    //int imageId;
    private String storeOrga = "読み込みエラー";
    private String storeName = "読み込みエラー";
    private String Twitter;
    private String HP;
    private String introduction = "読み込みエラー";
    private String Icon;
    private Bitmap StoreImage;
    private int IconVer = 0;
    private int Type;
    private int id = -1;

    public StoreInformationCard(){
        //firebase用の空コンストラクタ
    }

    public StoreInformationCard(String storeOrga, String storeName, String introduction, String Twitter, String HP, String  Icon, int IconVer, int Type){
        this.storeOrga = storeOrga;
        this.storeName = storeName;
        this.introduction = introduction;
        this.Twitter = Twitter;
        this.HP = HP;
        this.Icon = Icon;
        this.IconVer = IconVer;
        this.Type = Type;

        //Log.d("STICard", String.format("HP = %s", HP));
    }

    public void setId(int id){ this.id = id; }
    public void setStoreImage(Bitmap StoreImage){
        this.StoreImage = StoreImage;
    }

    public String getStoreOrga(){return this.storeOrga;}
    public String getStoreName(){return this.storeName;}
    public String getIntroduction(){return this.introduction;}
    public String getTwitter(){return  this.Twitter;}
    public String getHP(){return this.HP;}
    public String getIcon(){return this.Icon;}
    public Bitmap getStoreImage(){return this.StoreImage;}
    public int getIconVer(){return this.IconVer;}
    public int getType(){return this.Type;}
    public int getId(){return this.id;}
}

class StoreInformationCardComparator implements Comparator<StoreInformationCard> {
    private int storeType = 0;

    public StoreInformationCardComparator(int storeType){
        this.storeType = storeType;
    }

    @Override
    public int compare(StoreInformationCard st0, StoreInformationCard  st1) {
        if(storeType == 0){ return st0.getId() - st1.getId(); }
        int st1Type = st0.getType();
        int st2Type = st1.getType();
        if(st1Type != st2Type){
            if(st1Type == storeType){ return -1;}
            if(st2Type == storeType){ return 1;}
            return 0;
        }else {
            return st0.getId() - st1.getId();
        }
    }
}