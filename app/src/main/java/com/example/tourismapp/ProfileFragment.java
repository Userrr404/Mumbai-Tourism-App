package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class ProfileFragment extends Fragment {

    private TextView txtProfilePro, txtFullNamePro, txtUsernamePro;
    private Button btnEditProfilePro, btnEditEmailAddPro, btnChangePassPro, btnLogoutPro;
    private ImageView imgProfilePro;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        txtProfilePro = rootView.findViewById(R.id.txtProfilePro);
        txtFullNamePro = rootView.findViewById(R.id.txtFullNamePro);
        txtUsernamePro = rootView.findViewById(R.id.txtUsernamePro);

        btnEditProfilePro = rootView.findViewById(R.id.btnEditProfilePro);
        btnEditEmailAddPro = rootView.findViewById(R.id.btnEditChangeEmailAddPro);
        btnChangePassPro = rootView.findViewById(R.id.btnEditChangePassPro);
        btnLogoutPro = rootView.findViewById(R.id.btnLogoutPro);

        imgProfilePro = rootView.findViewById(R.id.imgProfilePro);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id","N/A");
        String email = sharedPreferences.getString("email","N/A");
        String password = sharedPreferences.getString("password","N/A");

        txtProfilePro.setText("User ID: " +userId);
        txtFullNamePro.setText("Email: " +email);
        txtUsernamePro.setText("Password: " +password);

        btnLogoutPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent iLogin = new Intent(getActivity(),LoginActivity.class);
                startActivity(iLogin);
                requireActivity().finish();
            }
        });

        // OPEN EDIT PROFILE ACTIVITY
        btnEditProfilePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditProfile = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(iEditProfile);
            }
        });


        return rootView;
    }
}