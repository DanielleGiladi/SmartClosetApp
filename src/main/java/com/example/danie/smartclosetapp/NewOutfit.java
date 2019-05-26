package com.example.danie.smartclosetapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;



public class NewOutfit extends Fragment {



    private static final String TAG = "FirstFragment";


    private Button newOutfitBtn;
    ArrayList<String> imagesPathShirt;
    ArrayList <String> imagesPathPants;
    ArrayList <String> imagesPathShoes;

    private FirebaseFirestore firebaseFirestore;
    private  String user_id;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_newoutfit, container, false);
        newOutfitBtn = view.findViewById(R.id.newOutfitBtn);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.progressBarNewOutfit);

        user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            progressBar.setVisibility(View.VISIBLE);


                            imagesPathShirt = (ArrayList<String>) task.getResult().get("imageShirt");
                            imagesPathPants = (ArrayList<String>) task.getResult().get("imagePants");
                            imagesPathShoes = (ArrayList<String>) task.getResult().get("imageShoes");
                            progressBar.setVisibility(View.INVISIBLE);


                        }
                        else {
                            progressBar.setVisibility(View.VISIBLE);
                            imagesPathShirt = new ArrayList<>();
                            imagesPathShoes = new ArrayList<>();
                            imagesPathPants = new ArrayList<>();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                                    }

                });
        newOutfitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(imagesPathShirt.isEmpty() && imagesPathPants.isEmpty()  && imagesPathShoes.isEmpty()){
                    Toast.makeText(getContext(), R.string.empty, Toast.LENGTH_SHORT).show();
                }
                else if(imagesPathShirt.isEmpty()  || imagesPathPants.isEmpty()  || imagesPathShoes.isEmpty() ){
                    Toast.makeText(getContext(),
                            R.string.not_enough, Toast.LENGTH_SHORT).show();
                }

                else {

                    FragmentTransaction trans = getFragmentManager()
                            .beginTransaction();
                    trans.replace(R.id.root_frame, new ShowShirt());
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                    }
                }
        });

        return view;
    }




    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
