package com.example.danie.smartclosetapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("ValidFragment")
public class ShowChosenImages extends Fragment {
     private GridView mGridView;
     private Button saveToFavoriteBtn , noThanksBtn;
     private ArrayList<String> chosenImagegs;
     private Frame frame;
     private FirebaseFirestore firebaseFirestore;
     private  String user_id;
     private FirebaseAuth mAuth;
     private ArrayList<String> favoriteImage;
     private StorageReference storageReference;
     private ArrayList<String> imagesPathShirt, imagesPathShoes, imagesPathPants;
     private ProgressBar progressBar;
     private int count=0;



    public ShowChosenImages(ArrayList<String> chosenImagegs) {
        this.chosenImagegs = chosenImagegs;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show_chosen_items, container, false);
        mGridView = view.findViewById(R.id.chosenImages);
        noThanksBtn = view.findViewById(R.id.noThanks);
        saveToFavoriteBtn = view.findViewById(R.id.saveToFavorite);
        mGridView.setNumColumns(3);
        frame = new Frame(3);
        count=0;
        mGridView.setAdapter(new CustomGridViewAdapter(chosenImagegs,getContext() , frame));

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = view.findViewById(R.id.progressBarSaveToFavorite);

        user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            favoriteImage= (ArrayList<String>) task.getResult().get("favorite");
                            if(favoriteImage==null){
                                favoriteImage = new ArrayList<>();

                            }
                            imagesPathShirt = (ArrayList<String>) task.getResult().get("imageShirt");
                            imagesPathPants = (ArrayList<String>) task.getResult().get("imagePants");
                            imagesPathShoes = (ArrayList<String>) task.getResult().get("imageShoes");

                        }
                    }
                });


        saveToFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count==0){
                    count=1;
                    favoriteImage.add(chosenImagegs.get(0));
                    favoriteImage.add(chosenImagegs.get(1));
                    favoriteImage.add(chosenImagegs.get(2));

                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("favorite" , favoriteImage);
                    userMap.put("imageShirt", imagesPathShirt);
                    userMap.put("imagePants", imagesPathPants);
                    userMap.put("imageShoes", imagesPathShoes);


                    firebaseFirestore.collection("users").document(user_id).set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.INVISIBLE);

                                    } else {
                                        String e = task.getException().getMessage();
                                        Toast.makeText(getContext(), " firestore Error: " + e, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }
                else {
                    Toast.makeText(getContext(),
                            R.string.Already_save, Toast.LENGTH_SHORT).show();

                }




            }
        });

        noThanksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
        });


        return view;
    }



}
