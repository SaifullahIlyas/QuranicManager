package com.example.quranicmanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class feedBack extends AppCompatActivity {
ListView listView;
ArrayList<feedBackAdapterModel> feedBackAdapterModels;
feedBackAdapterModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        
        listView = findViewById(R.id.userfeedbacklist);
        feedBackAdapterModels = new ArrayList<feedBackAdapterModel>();
        setUplistData();
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               feedBackAdapterModel model = feedBackAdapterModels.get(position);
               Intent intent = new Intent(feedBack.this,feedbackdetail.class);
               intent.putExtra("userdata",model.getUsername().toString());
               startActivity(intent);
           }
       });

        
    }
  void   setUplistData()
  {
      FirebaseFirestore fs  = FirebaseFirestore.getInstance();
      fs.collection("Feedback").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

              feedBackAdapter adapter = new feedBackAdapter(getApplicationContext(),feedBackAdapterModels);
              listView.setAdapter(adapter);

          }
      }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
              for(DocumentSnapshot document:task.getResult().getDocuments())
              {
                  Log.d("doc id",document.getId()+document.getString("status")) ;
                  model = new feedBackAdapterModel(document.getId().toString(),document.getString("status").toString(),R.drawable.message);
                  feedBackAdapterModels.add(model);
              }
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Toast.makeText(feedBack.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
          }
      });
  }
}
