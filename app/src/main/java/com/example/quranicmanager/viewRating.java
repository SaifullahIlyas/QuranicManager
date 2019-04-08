package com.example.quranicmanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class viewRating extends AppCompatActivity {
    RatingBar ratingBar;
    String usercount;
    int count = 0;
    double sumrating = 0.0;
    String Total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rating);
        ratingBar = findViewById(R.id.ratingbaruser);
        ratingBar.setClickable(false);
        getRating();


    }
void getRating()
{
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
     fs.collection("Rating").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            Log.d("count","Tak complted");
            List<DocumentSnapshot> document = task.getResult().getDocuments();
            for(DocumentSnapshot documentSnapshot:document)
            {
                count++;
                sumrating += Double.valueOf(documentSnapshot.getString("Rating"));
                Log.d("count",String.valueOf(count));

            }
            usercount = String.valueOf(count);
          Total =   String.valueOf(sumrating);
        }
    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
             /*for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments())
             {
                 count++;
                 sumrating += Double.valueOf(documentSnapshot.getString("Rating"));
                 Log.d("count",documentSnapshot.getId());
             }*/
             Log.d("complted","task completed");
             TextView user = findViewById(R.id.numerofUser);
             TextView rating = findViewById(R.id.Averagerating);
             user.setText(String.valueOf(count)+" "+user.getText());

             Float ratong = Float.valueOf(Total)/Float.valueOf(count);
             rating.setText(rating.getText()+" "+String.valueOf(Math.round(ratong*100.0)/100.0));
             ratingBar.setRating(ratong);

         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Log.d("Rating Error",e.getLocalizedMessage());
         }
     });
}
}
