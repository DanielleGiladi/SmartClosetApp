package com.example.danie.smartclosetapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.danie.smartclosetapp.Logic_new_outfit.Frame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class Favorite extends Fragment {
    private GridView mGridView;
    private ArrayList<String> imagesPath;
    private FirebaseFirestore firebaseFirestore;
    private  String user_id;
    private FirebaseAuth mAuth;
    private int size;
    private Frame frame;
    private TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mGridView = view.findViewById(R.id.showFavorite);
        txt = view.findViewById(R.id.favoriteData);
        mGridView.setNumColumns(3);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            imagesPath = (ArrayList<String>) task.getResult().get("favorite");
                           if(!imagesPath.isEmpty()){
                               size = imagesPath.size();
                               frame = new Frame(size);
                               mGridView.setAdapter(new CustomGridViewAdapter(imagesPath,getContext() , frame));
                           }
                           else{
                               txt.setText(R.string.empty_favorite);

                           }


                        }
                        else {
                            imagesPath = new ArrayList<>();
                            txt.setText(R.string.empty_favorite);



                        }
                    }
                });
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
