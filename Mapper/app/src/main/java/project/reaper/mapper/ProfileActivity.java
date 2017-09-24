package project.reaper.mapper;

/**
 * Created by Reaper on 17/9/17.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    Profile viewProfile;

    Button callBtn;
    Button chatBtn;

    int profileIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get the profile index
        Intent intent = getIntent();
        profileIndex = intent.getIntExtra("Profile Index", -1);

        // If its the user profile then the buttons for calling and chatting are removed.
        if (profileIndex == -1) {
            viewProfile = ((AppValues) this.getApplication()).getUserProfile();
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_profile_btn);
            layout.removeAllViews();
        }
        else {
            viewProfile = ((AppValues) this.getApplication()).getProfileList().get(profileIndex);

            // Call button method
            callBtn = (Button) findViewById(R.id.btn_profile_call);
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+ viewProfile.getPhoneNumber()));
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this,
                            android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                }
            });

            // Chat button method
            chatBtn = (Button) findViewById(R.id.btn_profile_chat);
            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if (profileIndex != -1) {
                        Intent intent = new Intent(getApplication(), ChatActivity.class);
                        ((AppValues) getApplication()).setIndex(profileIndex);
                        intent.putExtra("Profile Index", profileIndex);
                        startActivity(intent);
                    }
                }
            });
        }

        // Set profile name
        TextView name = (TextView) findViewById(R.id.text_profile_name);
        name.setText(viewProfile.getName());

        // Append age
        TextView age = (TextView) findViewById(R.id.text_profile_age);
        age.append("" + viewProfile.getAge());

        // Append weight
        TextView weight = (TextView) findViewById(R.id.text_profile_weight);
        weight.append("" + viewProfile.getWeight());

        List<Integer> trackerRates = viewProfile.getTrackerRateHistory();
        GraphView graph = (GraphView) findViewById(R.id.graph_tracker_data);
        LineGraphSeries<DataPoint> data = new LineGraphSeries<>();

        // Add some random data.
        Random random = new Random();
        for (int i = 0; i < 75; i++) {
            // DataPoint dataPoint = new DataPoint (i, trackerRates.get(i));
            DataPoint dataPoint = new DataPoint (i, random.nextFloat()*10);
            data.appendData(dataPoint, true, 100);
        }

        // Setup the Graph View
        graph.addSeries(data);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);

        // Check for the Doctor's profile.
        // TODO: If the graph is not required.
        if (viewProfile.isDoctor) {
            TextView infoLabel = (TextView) findViewById(R.id.text_profile_info_label);
            infoLabel.setText("Qualifications");
            String[][] val = {{"Some Qualification"}, {"A detailed description of what the qualification is!"}};
            addHealthHistory(val);
            addHealthHistory(val);
            addHealthHistory(val);
        }
        else {
            String[][] val = {{"Medical History"}, {"A detailed medical description of the user!"}};
            addHealthHistory(val);
            String[][] val2 = {{"Prescribed Medicines"}, {"Prescribed medicines for the user!"}};
            addHealthHistory(val2);
            String[][] val3 = {{"Therapy Notes"}, {"Doctors notes on users past behavior!"}};
            addHealthHistory(val3);
        }
    }

    // Self explanatory, just adds the Health History details in the fields.
    void addHealthHistory(String[][] value) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_profile);
        LayoutInflater inflater = getLayoutInflater();
        View viewHealthHistory =inflater.inflate(R.layout.health_history_info, null);
        TextView topic = viewHealthHistory.findViewById(R.id.text_profile_history_topic);
        TextView data = viewHealthHistory.findViewById(R.id.text_profile_history_data);
        topic.setText(value[0][0]);
        data.setText(value[1][0]);
        linearLayout.addView(viewHealthHistory);
    }
}
