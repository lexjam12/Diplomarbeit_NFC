package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jakob on 06.10.16.
 */
public class InformationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.info);
        setContentView(R.layout.activity_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    public void versionHistory(View view) throws Exception
    {
//        DialogHelper versionHistory = new DialogHelper(this);
//        versionHistory.versionHistory();
    }

//    public void about(View view) throws Exception
//    {
//        DialogHelper versionHistory = new DialogHelper(this);
//        versionHistory.versionHistory();
//    }
}
