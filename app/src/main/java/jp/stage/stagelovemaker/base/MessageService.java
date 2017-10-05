package jp.stage.stagelovemaker.base;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static jp.stage.stagelovemaker.utils.Constants.ARG_CHAT_ROOMS;

/**
 * Created by congn on 8/30/2017.
 */

public class MessageService extends IntentService {
    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //String senderId = intent.getStringExtra("senderId");
        //String receiverId = intent.getStringExtra("receiverId");
        String chatRoomId = intent.getStringExtra("chat_room_id");
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference chatRoomRef = mDatabaseRef.child(ARG_CHAT_ROOMS)
                .child(chatRoomId);
        int senderId = UserPreferences.getCurrentUserId();
        chatRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (!String.valueOf(value.child("senderId").getValue()).equals(String.valueOf(senderId))) {
                        String keyId = value.getKey();
                        if (!(boolean) value.child("read").getValue()) {
                            chatRoomRef.child(keyId).child("read").setValue(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
