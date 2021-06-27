package com.example.musiccafe;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    private static final int RC_SIGN_IN =101 ;
    private EditText email;
    private EditText password;
    private Uri imageUri;
    private EditText address;
    private Button signUPButton;
    private EditText fullName;
    private EditText phoneNumber;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseFirestore firebaseFirestore;
    private TextView google;
    private CallbackManager callbackManager;
    private TextView textViewLogin;
    private CircleImageView crimageView;private TextView uptextView;
    private static final String EMAIL = "email";
    private TextView facebookLogin;
    GoogleSignInClient mGoogleSignInClient;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textViewLogin= findViewById(R.id.textviewLogin);
        email = findViewById(R.id.signUpEmailAddress);
        password = findViewById(R.id.signUpNumberPassword);
        address= findViewById(R.id.editTextAddress);
        signUPButton = findViewById(R.id.signUpButtonId);
        fullName=findViewById(R.id.editTextPersonName);
        phoneNumber= findViewById(R.id.editTextPhone);
        google= findViewById(R.id.googleTextView);
        uptextView= findViewById(R.id.uploadimg);
        crimageView= findViewById(R.id.propic);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        callbackManager= CallbackManager.Factory.create();
        storageReference= FirebaseStorage.getInstance().getReference("Profilepic");
        FacebookSdk.getApplicationContext();
        //facebookLogin= findViewById(R.id.facebookLogin);
        callbackManager = CallbackManager.Factory.create();
        //facebookLogin = findViewById(R.id.facebookLogin);
        uptextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(signup.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(signup.this,LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        signUPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmail();
                UploadImage();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void UploadImage() {
        if (imageUri != null) {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(signup.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<Object, String> propic = new HashMap<>();
                            propic.put("picture",uri.toString());
                            firebaseFirestore.collection("profilepic").document(firebaseAuth.getCurrentUser().getUid())
                                    .set(propic);
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




    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
         imageUri= data.getData();
        crimageView.setImageURI(imageUri);
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        final FirebaseAuth mAuth;
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //Toast.makeText(signup.this, user.getEmail()+user.getDisplayName(),Toast.LENGTH_SHORT).show();
                            Map<Object, String> userData2 = new HashMap<>();
                            userData2.put("email",user.getEmail());
                            userData2.put("fullName",user.getDisplayName());
                            userData2.put("phoneNumber",user.getPhoneNumber());
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                                    .set(userData2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(signup.this,"Successfull",Toast.LENGTH_SHORT).show();
                                }
                            });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           Toast.makeText(signup.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent= new Intent(signup.this,StartActivity.class);
        startActivity(intent);
    }

    public void checkInput() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                signUPButton.setEnabled(true);
                signUPButton.setTextColor(Color.rgb(255, 255, 255));
            } else {
                signUPButton.setEnabled(false);
                signUPButton.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signUPButton.setEnabled(false);
            signUPButton.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    public void checkEmail() {
        if (email.getText().toString().matches(emailPattern)) {
            signUPButton.setEnabled(false);
            signUPButton.setTextColor(Color.argb(50, 255, 255, 255));
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Map<Object, String> userData = new HashMap<>();
                            userData.put("email",email.getText().toString());
                            userData.put("fullName", fullName.getText().toString());
                            userData.put("address",address.getText().toString());
                            userData.put("PhoneNumber", phoneNumber.getText().toString());
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                                    .set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Intent intent = new Intent(signup.this, StartActivity.class);
                                    startActivity(intent);
                                    //Toast.makeText(signup.this, "JAmi", Toast.LENGTH_SHORT).show();
                                    signup.this.finish();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {

                                    String error = e.getMessage().toString();
                                    Toast.makeText(signup.this, error, Toast.LENGTH_SHORT).show();
                                    signUPButton.setEnabled(true);
                                    signUPButton.setTextColor(Color.rgb(255, 255, 255));



                                }
                            });
                        }
                    });

        }
        else {
            email.setError("Invalid Email");
        }
    }
}