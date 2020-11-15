package fitness.buddy.comp231;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TrainerList extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        listView=(ListView)findViewById(R.id.listview);

        ArrayList<String> arrayList= new ArrayList<>();

        arrayList.add("George   For Weight Loss");
        arrayList.add("Ralph    For Weight Gain");
        arrayList.add("Sarjil   For Cardio");
        arrayList.add("Samarth  For Body Building");
        arrayList.add("Zhenzhou  For Physical Injury");
        arrayList.add("Haseena  For Weight Gain");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
    }
}