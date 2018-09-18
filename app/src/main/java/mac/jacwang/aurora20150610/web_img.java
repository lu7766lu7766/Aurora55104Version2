package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import mac.jacwang.aurora20150610.Comm.Static_var;

public class web_img extends mActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_img);

        setTitle("Menu");

        Intent intent = getIntent();
        String menu = intent.getStringExtra("brand_menu");
        WebView wv = (WebView)findViewById(R.id.webview);
        WebSettings websettings = wv.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient());

        wv.loadUrl(Static_var.BRANDMENU_PATH+menu);
        Log.d("auroramenu:",Static_var.BRANDMENU_PATH+menu);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.requestFocus();
//        wv.loadUrl(url);
    }
}
