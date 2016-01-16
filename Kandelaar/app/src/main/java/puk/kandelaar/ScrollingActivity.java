package puk.kandelaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {
    WebView webView;
    public final String home = "http://www.ngpukkandelaar.co.za";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.share_msg);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PUK Kandelaar App!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

               /* Snackbar.make(view, "Dankie dat jy hierdie app versprei!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Toast.makeText(getBaseContext(), "Dankie dat jy hierdie app versprei!",
                        Toast.LENGTH_LONG).show();
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        loadWebViewLoad(webView,home);
    }

    private void loadWebViewLoad(WebView webview,String url) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.nav_Tuis:
                loadWebViewLoad(webView, "http://www.ngpukkandelaar.co.za/tuis/");
                break;

            case R.id.nav_Eredienste:
                loadWebViewLoad(webView, "http://www.ngpukkandelaar.co.za/eredienste/");

                break;
            case R.id.nav_Uitreike:
                loadWebViewLoad(webView,"http://www.ngpukkandelaar.co.za/uitreike/");

                break;

            case R.id.nav_Events:
                loadWebViewLoad(webView, "http://www.ngpukkandelaar.co.za/events/");

                break;
            case R.id.nav_Kleingroepe:
                loadWebViewLoad(webView, "http://www.ngpukkandelaar.co.za/kleingroepe/");

                break;
            case R.id.nav_Eerstejaarskampe:
                loadWebViewLoad(webView,"http://www.ngpukkandelaar.co.za/eerstejaarskampe/");

                break;
            case R.id.nav_Inskrywingsvorm:
                loadWebViewLoad(webView, "http://www.ngpukkandelaar.co.za/inskrywingsvorm/");

                break;
            case R.id.nav_Kontak_Ons:
                loadWebViewLoad(webView,"http://www.ngpukkandelaar.co.za/kontak-ons/");

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    
}
