package com.example.musiccafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateprofile extends AppCompatActivity {
    private EditText upedit1,upedit2;
    private CircleImageView upimageView;
    private  Button  upButton,confirmUpdate;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Uri imgUri;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        upedit1=findViewById(R.id.UpdateProfileEdit1);
        upedit2= findViewById(R.id.UpdateProfileEdit2);
        upimageView= findViewById(R.id.UpdateProfileImage);
        upButton =findViewById(R.id.updateProfilebtn);
        confirmUpdate=findViewById(R.id.updateProfilebtn2);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("Profilepic");

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(updateprofile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        confirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
                UpdateData();

            }

        });





    }

    private void UpdateData() {
        String fullname,address;
        fullname=upedit1.getText().toString();
        address=upedit2.getText().toString();
        Map<String,Object> user=new HashMap<>();
        user.put("fullName",fullname);
        user.put("address",address);
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent= new Intent(updateprofile.this,StartActivity.class);
                startActivity(intent);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateprofile.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgUri=data.getData();
        upimageView.setImageURI(imgUri);

    }
    private void UploadImage() {
        if (imgUri != null) {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imgUri));
            fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(updateprofile.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String,Object> propic1 = new HashMap<>();
                            propic1.put("picture",uri.toString());
                            firebaseFirestore.collection("profilepic").document(firebaseAuth.getCurrentUser().getUid())
                                    .update(propic1);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private  String getFileExtension(Uri uri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
}