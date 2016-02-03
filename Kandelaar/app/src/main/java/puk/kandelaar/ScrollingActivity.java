package puk.kandelaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Script;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

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

    public final String link_ext_youtube = "https://www.youtube.com/user/PukkeKandelaar";
    public final String link_ext_facebook = "https://www.facebook.com/PUK-Kandelaar-336930246395625/timeline/";


    private ArrayList<String> linksOnPage = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        boolean hasHarwareMenu = ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(getApplicationContext()));
        if (!hasHarwareMenu) setSupportActionBar(toolbar);

        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.share_msg);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PUK Kandelaar App!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

               makeLongToast("Dankie dat jy hierdie app versprei!");
            }
        });

        webView = (WebView) findViewById(R.id.webView);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        loadWebViewLoad(webView, link_tuis);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    private void loadWebViewLoad(final WebView webview, String url) {

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        if (!isNetworkAvailable()) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            makeLongToast("Geen internet konneksie!\n\nOu inligting mag vertoon word!");
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        webView.setWebViewClient(new WebViewClient() {

            ProgressDialog pd = ProgressDialog.show(ScrollingActivity.this, "", "Net 'n oomblik...", true);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (isNetworkAvailable()) {
                    pd.show();
                    webview.setVisibility(View.GONE);
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("ngpukkandelaar.co.za"))
                {
                    view.loadUrl(url);
                    //makeShortToast("Internal URL");
                } else if (url.contains("youtu"))
                {
                    //makeShortToast("Youtube url");
                    if(isAppInstalled("com.google.android.youtube")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.google.android.youtube");
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                    else
                    {
                        makeLongToast("Youtube is nie op jou toestel nie!\nLaai dit af vir 'n beter ervaring.");
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }
                } else if (url.contains("facebook"))
                {
                   // makeShortToast("Facebook url");
                    if(!isAppInstalled("com.facebook.katana")) {
                        makeLongToast("Facebook is nie op jou toestel nie!\nLaai dit af vir 'n beter ervaring.");
                    }

                    Intent i = newFacebookIntent(ScrollingActivity.this.getPackageManager(),url);
                    startActivity(i);
                }else
                {
                   // makeShortToast("External url");
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
        /*switch (url) {
            case link_tuis : {Toast.makeText(getBaseContext(), "Tuis",Toast.LENGTH_SHORT).show(); break;}
            case link_eredienste : {Toast.makeText(getBaseContext(), "Eredienste",Toast.LENGTH_SHORT).show(); break;}
            case link_uitreike : {Toast.makeText(getBaseContext(), "Uitreike",Toast.LENGTH_SHORT).show(); break;}
            case link_events : {Toast.makeText(getBaseContext(), "Events",Toast.LENGTH_SHORT).show(); break;}
            case link_kleingroepe : {Toast.makeText(getBaseContext(), "Kleingroepe",Toast.LENGTH_SHORT).show(); break;}
            case link_warm_gesprekke : {Toast.makeText(getBaseContext(), "Warm Gesprekke",Toast.LENGTH_SHORT).show(); break;}
            case link_eerstejaarskampe : {Toast.makeText(getBaseContext(), "Eerstejaars Kampe",Toast.LENGTH_SHORT).show(); break;}
            case link_kontak_ons : {Toast.makeText(getBaseContext(), "Kontak Ons",Toast.LENGTH_SHORT).show(); break;}
        }*/

                //hide social buttons and menu
                // view.loadUrl("javascript:document.getElementById(\"social_icons\").setAttribute(\"style\",\"display:none;\");");
                //view.loadUrl("javascript:document.getElementById(\"site-navigation\").setAttribute(\"style\",\"display:none;\");");

              /*  try {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("a[href]");

                    linksOnPage.clear();

                    for (Element link : links) {
                        String tmp = link.attr("abs:href");
                        String text = link.text().trim();

                        if(tmp.contains("youtu"))
                        {
                            linksOnPage.add(tmp+"###"+text);
                        }
                    }

                makeLongToast(String.valueOf(linksOnPage.size()));
                }
                catch (Exception ex)
                {

                }*/


                webview.setVisibility(View.VISIBLE);
                try {
                    pd.dismiss();
                } catch (java.lang.IllegalArgumentException ex) {
                    Log.d("EXC", "Exception caught\n" + ex.getMessage());
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("about:blank");
                Toast.makeText(getBaseContext(), "Geen internet konneksie!",
                        Toast.LENGTH_LONG).show();
            }
        });

        webview.loadUrl(url);
    }

    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void makeShortToast(String msg)
    {
        Toast.makeText(getBaseContext(), msg,
                Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String msg)
    {
        Toast.makeText(getBaseContext(), msg,
                Toast.LENGTH_LONG).show();
    }

    public boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri;
        try {
            pm.getPackageInfo("com.facebook.katana", 0);
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}