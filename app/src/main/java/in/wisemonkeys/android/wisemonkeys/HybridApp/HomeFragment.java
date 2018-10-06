package in.wisemonkeys.android.wisemonkeys.HybridApp;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bhargavms.dotloader.DotLoader;

import in.wisemonkeys.android.wisemonkeys.Communicator;
import in.wisemonkeys.android.wisemonkeys.R;


public class HomeFragment extends Fragment {
    WebView webView;
    DotLoader dotLoader;
    Communicator communicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) getActivity().findViewById(R.id.webid);
        dotLoader = (DotLoader) getActivity().findViewById(R.id.text_dot_loader);
        communicator= (Communicator) getActivity();

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        //webView.getSettings().setUserAgentString(newUA);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSupportZoom(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadUrl("http://wisemonkeys.in/intro/");
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dotLoader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dotLoader.setVisibility(View.INVISIBLE);
                communicator.loadWebBlog();
            }
        });

    }

}
