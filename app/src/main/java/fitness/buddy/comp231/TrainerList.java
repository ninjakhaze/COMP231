package fitness.buddy.comp231;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TrainerList extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Certified Trainers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainerList.this,StartActivity.class));
            }
        });

        listView=(ListView)findViewById(R.id.listview);

        ArrayList<String> arrayList= new ArrayList<>();

        arrayList.add("\nGeorge\n\t\tFor Weight Loss");
        arrayList.add("Ralph\n\t\tFor Weight Gain");
        arrayList.add("Sarjil\n\t\tFor Cardio");
        arrayList.add("Samarth\n\t\tFor Body Building");
        arrayList.add("Zhenzhou\n\t\tFor Physical Injury");
        arrayList.add("Haseena\n\t\tFor Weight Gain");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
    }
}