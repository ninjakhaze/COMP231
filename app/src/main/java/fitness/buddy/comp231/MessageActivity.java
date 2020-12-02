package fitness.buddy.comp231;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fitness.buddy.comp231.Adapter.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    private TextView username;
    ImageView imageView;

    EditText msg_editText;
    ImageButton sendBtn;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    String userId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Widgets
        imageView = findViewById(R.id.imageView_profile);
        username = findViewById(R.id.username1);
        sendBtn = findViewById(R.id.btn_send);
        msg_editText = findViewById(R.id.text_send);

        //RecyclerView
        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                username.setText(usersData.getUsername());

                if (usersData.getImageURL().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(usersData.getImageURL()).into(imageView);
                }
                readMessages(firebaseUser.getUid(), userId, usersData.getImageURL());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_editText.getText().toString();
                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), userId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Please send a non empty message", Toast.LENGTH_SHORT).show();
                }
                msg_editText.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", message);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

        //Adding User to chat fragment: Latest Chats with contacts
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userId);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages(String myid, String userid, String imageurl){

        mChat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}