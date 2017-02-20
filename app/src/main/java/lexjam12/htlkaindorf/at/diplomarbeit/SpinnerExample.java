package lexjam12.htlkaindorf.at.diplomarbeit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lexjam12 on 10.02.17.
 */


//--------------------------------------------------------------------------------//
//----------------Nur zum Testen von Spinnern-------------------------------------//
//--------------------------------------------------------------------------------//
public class SpinnerExample extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectdoor);
        int i;
        Door door;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();

        for (i = 0; i <= 15; i++)
        {
            DoorPrefs doorPrefs = new DoorPrefs(this);
            door = doorPrefs.getDoor(i);

            if (door.getDoorName().isEmpty())
            {
                System.out.println("------empty-----");
                break;
            }
            else
            {
                String temp = "Tür: " + door.getDoorName();
                System.out.println("" + door.getDoorName());
                list.add(temp);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
            }
        }
    }
}
