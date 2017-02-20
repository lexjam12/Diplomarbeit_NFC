package lexjam12.htlkaindorf.at.diplomarbeit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lexjam12 on 13.02.17.
 */

//--------------------------------------------------------------------------------//
//----------------Geht noch nicht, da nicht ActivityKombatibel--------------------//
//--------------------------------------------------------------------------------//
public class SpinnerHelper extends AppCompatActivity
{
    public void addInSpinner()
    {
        //--------------------------------------------------------------------------------//
        //----------------Türen in den Spinner per Liste eintragen------------------------//
        //--------------------------------------------------------------------------------//

        int i;
        Door door;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        DoorPrefs doorPrefs = new DoorPrefs(this);

        for (i = 0; i <= 10; i++)
        {
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

        //----------------Do-Schleife schlecht, da ein Feld zu viel-----------------------//

//        do
//        {
//            door = doorPrefs.getDoor(i);
//            String temp = "Tür: "+door.getDoorName();
//            System.out.println(""+door.getDoorName());
//            list.add(temp);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                    R.layout.spinner_item, list);
//            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//            spinner.setAdapter(adapter);
//            if(door.getDoorName().isEmpty())
//            {
//                System.out.println("---empty---");
//                break;
//            }
//            else
//                i++;
//        }
//        while(!door.getDoorName().isEmpty());
    }
}
