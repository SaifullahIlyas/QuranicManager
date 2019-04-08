package com.example.quranicmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FileView extends AppCompatActivity {
    ArrayList<ManageFileModel> list;
    ManageFileModel model;
    ListView listView;
    ManageFileAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        list = new ArrayList<>();
        listView = findViewById(R.id.allFileList);
        listView.setLongClickable(true);
        getAllDoc();
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete");
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Object manageFileModel = adapter.getItem(adapterContextMenuInfo.position);
        menu.setHeaderTitle("Action");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


            if(item.getTitle()=="Delete")
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Log.d("print", "removing item pos=" + info.position);
            list.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void operationContextDelete(int itemId) {
    }

    private void getAllDoc() {

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("parah30").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> querySnap = task.getResult().getDocuments();
                for(DocumentSnapshot documentSnapshot:querySnap)
                {
                    model  = new ManageFileModel(documentSnapshot.getId(),R.drawable.mp3ic,R.drawable.menucontext);
                    list.add(model);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter = new ManageFileAdapter(FileView.this,list);
                listView.setAdapter(adapter);
            }
        });

    }
}
