package com.example.musiccafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class OwnerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PICK_IMAGE_REQUEST = 1;
    private Button selectImage, uploadImage,showDetails;
    private EditText foodName, foodPrize;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
   // private FirebaseFirestore firebaseFirestore1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        selectImage = findViewById(R.id.selectImageButton);
        uploadImage = findViewById(R.id.uploadImageButton);
        imageView= findViewById(R.id.foodImageViewId);
        progressBar= findViewById(R.id.ownerProgressBar);
        foodName = findViewById(R.id.foodName);
        foodPrize = findViewById(R.id.foodPrice);
        showDetails= findViewById(R.id.showOrderdetails);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        showDetails.setOnClickListener(this);

        storageReference= FirebaseStorage.getInstance().getReference("Uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("Uploads");
        // firebaseFirestore1= FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectImageButton) {
            openFileChooser();
        }
        if (v.getId() == R.id.uploadImageButton) {
            uploadFile();

        }
        if(v.getId()==R.id.showOrderdetails){
            Intent intent4=new Intent(OwnerActivity.this,ShowOrder.class);
            startActivity(intent4);
        }

    }
    private  String getFileExtension(Uri uri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
      if (imageUri!= null){
          StorageReference fileRef= storageReference.child(System.currentTimeMillis()
          +"."+getFileExtension(imageUri) );
          fileRef.putFile(imageUri)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          Handler handler = new Handler();
                          handler.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                 progressBar.setProgress(0);
                              }
                          },5000);
                          Toast.makeText(OwnerActivity.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                          //Map<Object, String> post = new HashMap<>();
                          //post.put("foodName",foodName.getText().toString());
                          //post.put("foodPrice",foodPrize.getText().toString());
                          //post.put("image",taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                          //firebaseFirestore1.collection("posts")
                                 // .add(post);
                           fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   Upload upload= new Upload( uri.toString(),foodName.getText().toString().trim(),
                                          foodPrize.getText().toString().trim());
                                   String uploadId=databaseReference.push().getKey();
                                   databaseReference.child(uploadId).setValue(upload);

                               }
                           });
//                          Upload upload= new Upload(foodName.getText().toString().trim(),
//                                  taskSnapshot.toString(),foodPrize.getText().toString().trim());
//                          String uploadId=databaseReference.push().getKey();
//                          databaseReference.child(uploadId).setValue(upload);

                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull  Exception e) {
                          Toast.makeText(OwnerActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                      }
                  })
                  .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onProgress(@NonNull  UploadTask.TaskSnapshot snapshot) {
                          double progress= (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                          progressBar.setProgress((int)progress);
                      }
                  });


      }else{
          Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
      }
    }

    private void openFileChooser() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null
          && data.getData()!=null){
            imageUri= data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}