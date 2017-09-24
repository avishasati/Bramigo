package project.reaper.mapper;

/**
 * Created by Reaper on 16/9/17.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// This class here builds the Dialog for the user to select the Doctor/Patient.
public class Dialog extends DialogFragment {

    int index;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.dialog_marker, null);

        // Get the index of the profile being viewed.
        index =  ((AppValues) getActivity().getApplication()).getIndex();
        final Profile px =  ((AppValues) getActivity().getApplication()).getProfile(index);

        // Set the name of the profile being viewed.
        TextView name =  dialogView.findViewById(R.id.text_dialog_name);
        name.setText(px.getName());

        // Set the value of tracker rate.
        TextView trackerRate = dialogView.findViewById(R.id.text_dialog_tracker_rate);
        trackerRate.append("" + px.getTrackerRate());

        // Sets the status of the Patient/Doctor in the Dialog
        TextView status = dialogView.findViewById(R.id.text_dialog_status);
        status.append(getStatus(px.getTrackerRate()));

        LinearLayout clickProfile = dialogView.findViewById(R.id.layout_dialog_profile);
        clickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),  ProfileActivity.class);
                intent.putExtra("Profile Index", index);
                startActivity(intent);
            }
        });

        // Chat button press method.
        Button chatBtn = dialogView.findViewById(R.id.btn_dialog_chat);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),  ChatActivity.class);
                intent.putExtra("Profile Index", index);
                startActivity(intent);
            }
        });

        Button callBtn = dialogView.findViewById(R.id.btn_dialog_call);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(px.getPhoneNumber());
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }

    // Phone call method.
    void makePhoneCall (String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phoneNumber));

        if (ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }

    // Gets the status based on the tracker rate.
    String getStatus (int trackerRate) {
        Profile px =  ((AppValues) getActivity().getApplication()).getProfile(index);

        if (px.isDoctor) {
            return "Doctor";
        }
        else {
            if (trackerRate > 7) {
                return "Critical!";
            }
            else if (trackerRate > 4) {
                return "Unwell";
            }
            else {
                return "Healthy";
            }
        }
    }
}
