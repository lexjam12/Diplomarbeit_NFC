package lexjam12.htlkaindorf.at.diplomarbeit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by jakob on 06.10.16.
 */
public class InformationActivity extends AppCompatActivity
{
    //--------------------------------------------------------------------------------//
    //----------------Setzt alles was beim öffenen der Activity sein soll-------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("info");
        setContentView(R.layout.activity_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    //--------------------------------------------------------------------------------//
    //----------------Erzeugt das Menü------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    //--------------------------------------------------------------------------------//
    //----------------Pfeil zu MainActivity-------------------------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //--------------------------------------------------------------------------------//
    //----------------Button "Versionsverlauf"----------------------------------------//
    //--------------------------------------------------------------------------------//
    public void versionHistory(View view) throws Exception
    {
//        DialogHelper versionHistory = new DialogHelper(this);
//        versionHistory.versionHistory();
    }


    //--------------------------------------------------------------------------------//
    //----------------Button "Über"---------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void about(View view) throws Exception
    {
//        DialogHelper versionHistory = new DialogHelper(this);
//        versionHistory.versionHistory();
    }
}
