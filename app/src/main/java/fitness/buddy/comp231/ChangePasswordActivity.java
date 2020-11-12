package fitness.buddy.comp231;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePasswordActivity extends AppCompatActivity {

    private MaterialEditText oldPsw, newPsw, confirmPsw;
    private Button changePsw;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView userName;
    private CircleImageView circleImageView;
    private List<ImagesList> imagesList;
    private StorageReference storageReference;
    private UsersData usersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this,StartActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        oldPsw = findViewById(R.id.oldPassword);
        newPsw = findViewById(R.id.newPassword);
        confirmPsw = findViewById(R.id.confirmPassword);
        changePsw = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBar);
        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtOldPsw = oldPsw.getText().toString();
                String txtNewPsw = newPsw.getText().toString();
                String txtConfirmPsw = confirmPsw.getText().toString();
                if (txtOldPsw.isEmpty() || txtNewPsw.isEmpty() || txtConfirmPsw.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txtNewPsw.length()<6){
                    Toast.makeText(ChangePasswordActivity.this, "The new password length should be more than 6 character", Toast.LENGTH_SHORT).show();
                } else if (!txtConfirmPsw.equals(txtNewPsw)){
                    Toast.makeText(ChangePasswordActivity.this, "Confirm password does not match new password", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(txtOldPsw,txtNewPsw);
                }
            }
        });
        imagesList = new ArrayList<>();
        userName = findViewById(R.id.username);
        circleImageView = findViewById(R.id.profileImage);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                userName.setText(usersData.getUsername());
                if (usersData.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(getApplicationContext()).load(usersData.getImageURL()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangePasswordActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword(String txtOldPsw, String txtNewPsw) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),txtOldPsw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(txtNewPsw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}