package com.example.quranicmanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class traffic extends AppCompatActivity {
    ArrayList<trafficModel> models;
    trafficModel trafficModel;
    trafficAdapter adapter;
    ListView view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        view =  findViewById(R.id.trafficclist);
        models = new ArrayList<>();
        handletraffic();


    }
    void handletraffic()
    {
        FirebaseFirestore fs =  FirebaseFirestore.getInstance();
        fs.collection("apptraffic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    trafficModel = new trafficModel(documentSnapshot.getId().toString(), documentSnapshot.getString("users").toString(), R.mipmap.calenderimg_round);
                    models.add(trafficModel);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter =new trafficAdapter(traffic.this,models);
                view.setAdapter(adapter);
                Log.d("print",String.valueOf(models.size()));

            }
        });
    }
}
