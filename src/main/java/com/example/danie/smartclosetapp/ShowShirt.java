package com.example.danie.smartclosetapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.danie.smartclosetapp.Logic_new_outfit.Frame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowShirt extends Fragment {

    private GridView mGridView;
    private ArrayList<String> imagesPathShirt;
    private FirebaseFirestore firebaseFirestore;
    private  String user_id;
    private FirebaseAuth mAuth;
    private int size;
    private Frame frame;
    private Boolean mark = false;
    private int pos = -1;

    private ArrayList<String> chosenImage;
    private Button nextBtn;
    private ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_items, container, false);
        mGridView = view.findViewById(R.id.gridView);
        nextBtn = view.findViewById(R.id.button_next_item);
        nextBtn.setText(R.string.shirt_next);
        progressBar = view.findViewById(R.id.progressBarShow);
        mGridView.setNumColumns(2);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chosenImage = new ArrayList();



        user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.getResult().exists()){
                                            progressBar.setVisibility(View.VISIBLE);
                                            imagesPathShirt = (ArrayList<String>) task.getResult().get("imageShirt");
                                            size = imagesPathShirt.size();
                                            frame = new Frame(size);
                                            mGridView.setAdapter(new CustomGridViewAdapter(imagesPathShirt,getContext() , frame));
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }
                                        else {
                                            imagesPathShirt = new ArrayList<>();

                                        }
                                    }
                                });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mark) {
                    pos = position;
                    frame.setTile(position);
                    ((CustomGridViewAdapter) mGridView.getAdapter()).notifyDataSetChanged();
                    mark = true;
                    chosenImage.add(imagesPathShirt.get(position));
                }
                else if (pos == position){
                    frame.setTile(position);
                    ((CustomGridViewAdapter) mGridView.getAdapter()).notifyDataSetChanged();
                    chosenImage.clear();
                    mark = false;
                }

                else{
                    Toast.makeText(getContext() , R.string.marked , Toast.LENGTH_LONG).show();
                }



            }
        });


       nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chosenImage.isEmpty()) {
                    FragmentTransaction trans = getFragmentManager()
                            .beginTransaction();
                    trans.replace(R.id.root_frame, new ShowPants(chosenImage));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    trans.commit();
                }
                else {
                    Toast.makeText(getContext() , R.string.need_to_select, Toast.LENGTH_LONG).show();


                }
           }
      });





        return view;
    }
}
