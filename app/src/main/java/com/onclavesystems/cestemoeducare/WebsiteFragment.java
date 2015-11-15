package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebsiteFragment extends Fragment {
    protected WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_website, container, false);

        webview = (WebView)view.findViewById(R.id.webview);
        FloatingActionButton refresh = (FloatingActionButton)view.findViewById(R.id.button_refresh);
        FloatingActionButton forward = (FloatingActionButton)view.findViewById(R.id.button_forward);
        FloatingActionButton backward = (FloatingActionButton)view.findViewById(R.id.button_backward);

        webview.setWebViewClient(new CestemoBrowser());
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);

        if(savedInstanceState == null) {
            webview.loadUrl("http://www.cestemo.com");
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing", Snackbar.LENGTH_SHORT).show();
                webview.reload();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webview.canGoForward()) {
                    webview.goForward();
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webview.canGoBack()) {
                    webview.goBack();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        webview.saveState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webview.restoreState(savedInstanceState);
    }

    private class CestemoBrowser extends WebViewClient {
        private ProgressDialog progressDialog = null;

        @Override
        public  boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return  true;
        }

        public void onLoadResource(WebView webView, String url) {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
            }
        }

        public void onPageFinished(WebView webView, String url) {
            try {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            catch (Exception e) {
                if(getView() != null) {
                    Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity().getBaseContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
