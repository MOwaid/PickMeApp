package com.ais.pickmecab.ui.About;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ais.pickmecab.MainActivity;
import com.ais.pickmecab.R;
import com.ais.pickmecab.SplashActivity;
import com.ais.pickmecab.ui.login.LoginActivity;

import static com.ais.pickmecab.SplashActivity.editor;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.about_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_about);
        aboutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editor = SplashActivity.sh.edit();
        editor.clear();
        editor.commit();

        getActivity().finish();
        Intent homepage = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        startActivity(homepage);
    }
}
