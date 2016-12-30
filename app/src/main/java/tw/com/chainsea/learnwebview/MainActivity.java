package tw.com.chainsea.learnwebview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://www.baidu.com";
    private WebViewFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment = WebViewFragment.newInstance(URL);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragment.tryToGoBack()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
