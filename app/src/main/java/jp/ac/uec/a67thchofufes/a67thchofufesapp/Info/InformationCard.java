package jp.ac.uec.a67thchofufes.a67thchofufesapp.Info;

public class InformationCard {
    //int imageId;
    private String Day = "読み込みエラー";
    private String Title = "読み込みエラー";
    private String Content;

    public InformationCard(){
        //firebase用の空コンストラクタ
    }

    public InformationCard(String Day, String Title, String Content){
        this.Day = Day;
        this.Title = Title;
        this.Content = Content;

        //Log.d("STICard", String.format("HP = %s", HP));
    }

    public String getDay(){return this.Day;}
    public String getTitle(){return this.Title;}
    public String getContent(){return this.Content;}

}
