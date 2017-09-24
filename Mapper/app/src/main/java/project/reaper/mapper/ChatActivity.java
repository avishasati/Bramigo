package project.reaper.mapper;

/**
 * Created by Reaper on 17/9/17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    // For profile data.
    Profile px;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        int index = intent.getIntExtra("Profile Index", -1);
        px = ((AppValues) this.getApplication()).getProfile(index);

        // Setting the name at the top, on the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        toolbar.setTitle(px.getName());

        final TextView message = (TextView) findViewById(R.id.text_send_message);

        // This block here is used to send messages.
        ImageButton sendBtn = (ImageButton) findViewById(R.id.btn_chat_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(message.getText().toString());
                message.setText("");
            }
        });

        // This methos is used to send the messages.
        receiveMessage("Hello, How can i help you ?");

    }

    // Self explanatory.
    void sendMessage (String message) {
        LinearLayout chatMessagesView = (LinearLayout) findViewById(R.id.layout_chat_message_view);
        LinearLayout chatMessageHolder = (LinearLayout) View.inflate(this, R.layout.chat_cards_sender, null);
        TextView chatMessage = chatMessageHolder.findViewById(R.id.text_chat_sender_message);
        chatMessage.setText(message);
        // The lines below add the time.
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMin = rightNow.get(Calendar.MINUTE);
        TextView messageTime = chatMessageHolder.findViewById(R.id.text_chat_sender_time);
        messageTime.setText("" + currentHour + ":" + currentMin);
        chatMessagesView.addView(chatMessageHolder);
    }

    // Self explanatory.
    void receiveMessage (String message) {
        LinearLayout chatMessagesView = (LinearLayout) findViewById(R.id.layout_chat_message_view);
        LinearLayout chatMessageHolder = (LinearLayout) View.inflate(this, R.layout.chat_cards_receiver, null);
        TextView chatMessage = chatMessageHolder.findViewById(R.id.text_chat_receiver_message);
        chatMessage.setText(message);
        // The lines below add the time.
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMin = rightNow.get(Calendar.MINUTE);
        TextView messageTime = chatMessageHolder.findViewById(R.id.text_chat_receiver_time);
        messageTime.setText("" + currentHour + ":" + currentMin);
        chatMessagesView.addView(chatMessageHolder);
    }
}
