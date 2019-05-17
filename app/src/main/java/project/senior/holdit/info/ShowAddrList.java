package project.senior.holdit.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.model.AddrList;

public class ShowAddrList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_addr_list);
        final ListView listView = (ListView)findViewById(R.id.listview_show_addr);

        final AddrList addr = new AddrList();

        final Intent getIntent = getIntent();
        final Integer req = getIntent.getIntExtra("requestCode",-1);
        final ArrayList<String> list;
        if(req == 1){
            list = addr.getProvinceList();
        }else if(req == 2){
            String province = getIntent.getStringExtra("province");
            list = addr.getDistrictList(province);
        }else{
            String district = getIntent.getStringExtra("district");
            list = addr.getPostcodeList(district);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",(String)listView.getItemAtPosition(i));
                if(req == 2){
                    String district = (String)listView.getItemAtPosition(i);
                    ArrayList<String> lists = addr.getPostcodeList(district);
                    returnIntent.putExtra("postcode",lists.get(0));
                }
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }
}
