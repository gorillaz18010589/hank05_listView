package tw.org.iii.appps.hank05;
//ListView <--> adator <--> 資料
//手上有一堆資料透過條便器,灌到ListView
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    private ListView  listView; //藥丸listview宣告
    private  SimpleAdapter adapter; //宣告條便器處理
    private  int[] to ={R.id.item_title}; //宣告參數to,他是從整數陣列而來(裡面是來源位置),to到我要配置的版面
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private  String[] from ={"brad"}; //知道是第0元素,yo到那個titole第0個元素,把brad這個名字裡的value灌進去裡面
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          input = findViewById(R.id.input); //add按鈕按下後
          listView =  findViewById(R.id.listview); //抓取listView的id
         initListView(); //因為要透過條便器取得資料灌入到listview另外寫方法
    }

    //設置條便器方法
    private void initListView(){
        //因為data沒有資料,我先故意做資料上去才能完from,跑10迴圈印出資料,處理data資料
        for(int i=0; i<=10; i++) {
            HashMap<String,String> map = new HashMap<>();
            map.put("brad", "Item:" + (int) (Math.random() * 36 + 1)); //name:value 這個亂數的值放到brad裡去代表陣列第幾個
            data.add(map); //把裝好的屬性放上去Linklist
        }

        adapter = new SimpleAdapter(this,data,R.layout.item,from,to); //1.(Context) =>acivity跟service都是,所以我就用this,2.(List<? extends Map<String, ?>>)用java的泛型ket:String value:物件
        //3.resource:你這條的版面配置是誰設計版面n,另外再layout底下寫新的版面回傳(int)因為已經配出位置4.(from):裡面是字串陣列,要知道你的key字串,取得value在連接到to5.(to)裡面放的是整數陣列
        listView.setAdapter(adapter); //條便器搞定放進去

            //點擊時出現提醒項目
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //點擊某個item被案,
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//(i):代表第幾個項目被按了
//                Log.v("brad","item" + i); //確認有無抓到i的項目
                //toast.maketext =>(1.context:這個物件外的this,2.取得這個資料第i個元素裡面的key叫brad/ 3.秀出的時間短短的就好).show(嗅出訊息)
                Toast.makeText(MainActivity.this, data.get(i).get("brad"),Toast.LENGTH_SHORT).show();
            }
        });

        //設置長按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) { //(int i代表項目)
                Log.v("brad","lomg="+ i);
                //                return false; //如果flase常按會抓到,放開時也會跑創造字事件
                showConfrimDialog(i); //源頭參數帶進方法
//                removeItem();
                return  true; //如果true常按時會抓到,放開時不會跑創造字事件裡
            }
        });

    }
        //按下按鈕時新增一個輸入的項目上去
    public void addItem(View view) {
        HashMap<String,String> map = new HashMap<>();
        map.put("brad" , input.getText().toString()); //你輸入的資料掛上去
//        data.add(map); //把裝好的屬性放上去Linklist
        data.add(0,map); //你輸入的放在第一筆
        adapter.notifyDataSetChanged();//資料有更新異動要值行才會新增上去

    }
        //搜尋撈資料
    public void search(View view) {
       String key =  input.getText().toString(); //查詢的項目存入key李
        LinkedList<HashMap<String,String>> temp = new LinkedList<>();
        for(HashMap<String,String> row : data){ //尋訪我們的DATA資料
            String brad = row.get("brad"); //找到裡面有brad的name名資料陣列
            if(brad.contains(key)){//如果這個資料裡包含了我所查詢的值
               temp.add(row); //把這些我所查詢的值新增上去
            }
        }
        data.clear(); //陣列尋訪完,清空本來的值
        for(HashMap<String,String> x:temp){ //尋訪那些我所查詢的值
            data.add(x);  //把我所查詢的資料掛上去
        }
        adapter.notifyDataSetChanged(); //更新資料

                }

                //常按時出現確認對話框再看是否要刪除
                private  void showConfrimDialog(final int index){ //從常按事件取得項目參數i,設置為index
                    AlertDialog dialog = null;

                    AlertDialog.Builder builder = new AlertDialog.Builder(this); //顯示訊息物件實體
                    builder.setTitle("確認") //顯示標題
                            .setCancelable(false)//按鈕是否可以取消(可以/不可以)
                            .setMessage("你是否要刪除這個項目") //顯示訊息
                            .setNegativeButton("不要", null) //因為按下不要的按鈕沒有要執行什麼東西,直接null就好
                            .setPositiveButton("是的", new DialogInterface.OnClickListener() {//設置確認正向的按念("訊息字串",事件觸發)
                                @Override //當點選是後
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.v("brad","ok");
                                    removeItem(index);
                                }
                            });
                    dialog =  builder.create(); //這個bulider創造出dialog

                    dialog.show(); //訊息秀出來

                }

                //常按時觸發刪除資訊的方法
                private void removeItem(int index) { //取得常按點選是後index(i)第幾個項目
                    data.remove(index); //刪除資料(index筆)
                    adapter.notifyDataSetChanged(); // 更新資料
                }
    }


