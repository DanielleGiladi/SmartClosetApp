package com.example.danie.smartclosetapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AddItem extends Fragment
        {


            private FirebaseAuth mAuth;
            private Uri newImageURI = null;
            private ImageView imageView ;
            private Spinner spinner;
            private Button saveImageBtn;
            private StorageReference storageReference;
            private FirebaseFirestore firebaseFirestore;
            private  String user_id;
            private ArrayList <String> imagesPathShirt;
            private ArrayList <String> imagesPathPants;
            private ArrayList <String> imagesPathShoes;
            private ArrayList <String> imagesFavorite;
            private ProgressBar progressBar;
            private int count=0;




            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_additem, container, false);
        mAuth = FirebaseAuth.getInstance();
        count=0;

        user_id = mAuth.getCurrentUser().getUid();
        progressBar= view.findViewById(R.id.add_item_progress);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){

                            imagesPathShirt = (ArrayList<String>) task.getResult().get("imageShirt");
                            imagesPathPants = (ArrayList<String>) task.getResult().get("imagePants");
                            imagesPathShoes = (ArrayList<String>) task.getResult().get("imageShoes");
                            imagesFavorite = (ArrayList<String>) task.getResult().get("favorite");


                        }
                        else {
                            imagesPathShirt = new ArrayList<>();
                            imagesPathShoes = new ArrayList<>();
                            imagesPathPants = new ArrayList<>();
                            imagesFavorite = new ArrayList<>();
                        }
                    }
                });


        progressBar.setVisibility(View.INVISIBLE);
        imageView = view.findViewById(R.id.takeImage);
        spinner = view.findViewById(R.id.spinner);
        saveImageBtn = view.findViewById(R.id.saveImageBtn);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_list_item_1 , getResources().getStringArray(R.array.closet));

        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);

        saveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count ==0){

                    final String type = spinner.getSelectedItem().toString();

                    if(!type.equals("Item type") && newImageURI !=null) {
                        count=1;
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "save " + type , Toast.LENGTH_SHORT).show();
                        StorageReference image_path = storageReference.child("item")
                                .child(user_id).child(UUID.randomUUID() + ".jpg");
                        image_path.putFile(newImageURI).addOnCompleteListener
                                (new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            storeFirebase(task, type);
                                        }
                                        else {
                                            String e = task.getException().getMessage();
                                            Toast.makeText(getContext(), R.string.error + e, Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                    }
                    else if(type.equals("Item type") &&  newImageURI !=null){
                        Toast.makeText(getContext(), R.string.Select_an_item_type, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getContext(), R.string.Take_a_picture, Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getContext(), R.string.Already_save, Toast.LENGTH_SHORT).show();

                }


                }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(getContext() ,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(getContext(), R.string.permission_denied, Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity() ,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }
                    else{
                        bringImagePicker();
                    }
                }
                else{
                    bringImagePicker();
                }
            }
        });

        return view;
    }
    private void bringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512 , 512)
                .setAspectRatio(1 , 1)
                .start(getActivity() , AddItem.this);

        }

        private void storeFirebase(Task<UploadTask.TaskSnapshot> task , String type) {
                Uri download_uri;

            if(task != null) {
                    download_uri = task.getResult().getDownloadUrl();
                    if(type.equals("Shirt")){
                        imagesPathShirt.add(download_uri.toString());
                    }
                    else if(type.equals("Pants")){
                        imagesPathPants.add(download_uri.toString());
                    }
                    else if(type.equals("Shoes")){
                        imagesPathShoes.add(download_uri.toString());
                    }

                }
                else {
                    download_uri = newImageURI;
                    if(type.equals("Shirt")){
                        imagesPathShirt.add(download_uri.toString());
                    }
                    else if(type.equals("Pants")){
                        imagesPathPants.add(download_uri.toString());
                    }
                    else if(type.equals("Shoes")){
                        imagesPathShoes.add(download_uri.toString());
                    }


                }


            Map<String,Object> userMap = new HashMap<>();
                userMap.put("type ", type);
                userMap.put("imageShirt", imagesPathShirt);
                userMap.put("imagePants", imagesPathPants);
                userMap.put("imageShoes", imagesPathShoes);
                userMap.put("favorite", imagesFavorite);


                firebaseFirestore.collection("users").document(user_id).set(userMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    progressBar.setVisibility(View.INVISIBLE);

                                } else {
                                    String e = task.getException().getMessage();
                                    Toast.makeText(getContext(), R.string.error + e, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



            }


            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
               super.onActivityResult(requestCode, resultCode, data);
               if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == getActivity().RESULT_OK) {
                        newImageURI = result.getUri();
                        imageView.setImageURI(newImageURI);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();

                    }
                }

            }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
