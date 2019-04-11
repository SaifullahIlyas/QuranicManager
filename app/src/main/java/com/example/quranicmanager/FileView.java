package com.example.quranicmanager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class FileView extends AppCompatActivity {
    ArrayList<ManageFileModel> surahlist;
    ArrayList<ManageFileModel> quranlist;
    ManageFileModel model;
    ListView listView;
    ManageFileAdapter adapter;
    Switch surahSwithb;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar =(Toolbar)findViewById(R.id.mangetoolbar);
        setSupportActionBar(toolbar);
        quranlist =  new ArrayList<>();
        surahlist =  new ArrayList<>();
        listView = findViewById(R.id.allFileList);

         surahSwithb= findViewById(R.id.switchsurah);
        handleSwitch();
        listView.setLongClickable(true);
        getAllDoc();
        getSurahlistDoc();
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
            if(surahSwithb.isChecked())
            surahlist.remove(info.position);
            else
                quranlist.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void operationContextDelete(int itemId) {
    }

    private void getAllDoc() {

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("parah").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> querySnap = task.getResult().getDocuments();
                for(DocumentSnapshot documentSnapshot:querySnap)
                {
                    model  = new ManageFileModel(documentSnapshot.getId(),R.mipmap.applequran,R.drawable.menucontext);
                    quranlist.add(model);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter = new ManageFileAdapter(FileView.this,quranlist);
                listView.setAdapter(adapter);
            }
        });

    }
    private  void getSurahlistDoc()
    {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("surah").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> querySnap = task.getResult().getDocuments();
                for(DocumentSnapshot documentSnapshot:querySnap)
                {
                    model  = new ManageFileModel(documentSnapshot.getId(),R.mipmap.applequran,R.drawable.menucontext);
                    surahlist.add(model);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
        });
    }
    void handleSwitch()
    {
        surahSwithb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked())
                {
                    adapter.setAdapterchande(surahlist);
                    adapter.notifyDataSetChanged();
                    Log.d("Switch","Clicked");
                }
                else {
                    adapter.setAdapterchande(quranlist);
                    adapter.notifyDataSetChanged();
                    Log.d("Switch","Clicked of");
                }

            }
        });

    }
}
