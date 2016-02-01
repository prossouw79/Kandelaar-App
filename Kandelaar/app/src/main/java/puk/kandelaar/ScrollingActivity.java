package puk.kandelaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ScrollingActivity extends AppCompatActivity {
    WebView webView;
    public final String link_tuis = "http://www.ngpukkandelaar.co.za/tuis/";
    public final String link_eredienste = "http://www.ngpukkandelaar.co.za/eredienste/";
    public final String link_uitreike = "http://www.ngpukkandelaar.co.za/uitreike/";
    public final String link_events = "http://www.ngpukkandelaar.co.za/events/";
    public final String link_kleingroepe = "http://www.ngpukkandelaar.co.za/kleingroepe/";
    public final String link_warm_gesprekke = "http://www.ngpukkandelaar.co.za/warm-gesprekke/";
    public final String link_eerstejaarskampe = "http://www.ngpukkandelaar.co.za/eerstejaarskampe/";
    public final String link_inskrywingsvorm = "http://www.ngpukkandelaar.co.za/inskrywingsvorm/";
    public final String link_kontak_ons = "http://www.ngpukkandelaar.co.za/kontak-ons/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        boolean hasHarwareMenu = ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(getApplicationContext()));
        if (!hasHarwareMenu) setSupportActionBar(toolbar);

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

                Toast.makeText(getBaseContext(), "Dankie dat jy hierdie app versprei!",
                        Toast.LENGTH_LONG).show();
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        loadWebViewLoad(webView, link_tuis);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if(webView.getUrl().equals(link_tuis))
        {

            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
        else {
            loadWebViewLoad(webView, link_tuis);
            Toast.makeText(getBaseContext(), "Druk nog 'n keer om die app toe te maak.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadWebViewLoad(WebView webview,String url)
    {
        switch (url) {
            case link_tuis : {Toast.makeText(getBaseContext(), "Tuis",Toast.LENGTH_SHORT).show(); break;}
            case link_eredienste : {Toast.makeText(getBaseContext(), "Eredienste",Toast.LENGTH_SHORT).show(); break;}
            case link_uitreike : {Toast.makeText(getBaseContext(), "Uitreike",Toast.LENGTH_SHORT).show(); break;}
            case link_events : {Toast.makeText(getBaseContext(), "Events",Toast.LENGTH_SHORT).show(); break;}
            case link_kleingroepe : {Toast.makeText(getBaseContext(), "Kleingroepe",Toast.LENGTH_SHORT).show(); break;}
            case link_warm_gesprekke : {Toast.makeText(getBaseContext(), "Warm Gesprekke",Toast.LENGTH_SHORT).show(); break;}
            case link_eerstejaarskampe : {Toast.makeText(getBaseContext(), "Eerstejaars Kampe",Toast.LENGTH_SHORT).show(); break;}
            case link_kontak_ons : {Toast.makeText(getBaseContext(), "Kontak Ons",Toast.LENGTH_SHORT).show(); break;}

        }


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);

        if (!isNetworkAvailable()) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Toast.makeText(getBaseContext(), "Geen internet konneksie!\nDie app hardloop (nog) nie op genade nie!",
                    Toast.LENGTH_LONG).show();
        } else
        {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {

            }
        });

        webview.loadUrl(url);
}

    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_scrolling, menu);
            return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String tmpLink = link_tuis;
        switch (item.getItemId()) {
            case R.id.nav_Tuis:
                tmpLink = link_tuis;            break;
            case R.id.nav_Eredienste:
                tmpLink =  link_eredienste;     break;
            case R.id.nav_Uitreike:
                tmpLink = link_uitreike;        break;
            case R.id.nav_Events:
                tmpLink = link_events;          break;
            case R.id.nav_Kleingroepe:
                tmpLink =  link_kleingroepe;    break;
            case R.id.nav_Warm_gesprekke:
                tmpLink = link_warm_gesprekke; break;
            case R.id.nav_Eerstejaarskampe:
                tmpLink = link_eerstejaarskampe;break;
            case R.id.nav_Inskrywingsvorm:
                tmpLink =  link_inskrywingsvorm;break;
            case R.id.nav_Kontak_Ons:
                tmpLink = link_kontak_ons;      break;
        }
        loadWebViewLoad(webView,tmpLink);
        return super.onOptionsItemSelected(item);
    }

    
}
