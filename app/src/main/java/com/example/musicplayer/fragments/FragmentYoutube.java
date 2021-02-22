package com.example.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;

public class FragmentYoutube extends Fragment {
    private static final String HOME_PAGE = "youtube.com";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        WebView webViewYoutube = view.findViewById(R.id.webViewYoutube);
        webViewYoutube.setWebViewClient(new WebViewClient());
        webViewYoutube.loadUrl("https://" + HOME_PAGE);
        WebSettings webSettings = webViewYoutube.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return view;
    }
}
