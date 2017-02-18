package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

public class MainActivity extends AppCompatActivity implements  DialogHelper.OnAddDoorsListener,
                                                                DialogHelper.OnEditDoorsListener,
                                                                DialogHelper.OnDeleteDoorsListener
{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();
    Door door;
    DoorPrefs doorPrefs;

    //--------------------------------------------------------------------------------//
    //----------------Setzt alles was beim öffenen der App sein soll------------------//
    //--------------------------------------------------------------------------------//
    ArrayAdapter adapter;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate ausfühen");


        //--------------------------------------------------------------------------------//
        //----------------Setzt provesorisch den Status vom NFC---------------------------//
        //--------------------------------------------------------------------------------//

        TextView mTextView = (TextView) findViewById(R.id.things);

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null)
        {
            //----------------Falls Gerät NFC nicht unterstützt -> Abbruch--------------------//
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
        if (!mNfcAdapter.isEnabled())
        {
            setValue(R.id.things, "NFC is diabled");
        }
        else
        {
            mTextView.setText(R.string.connect);
        }


        Button button = (Button)findViewById(R.id.door);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor_grey, 0, 0);
        button.setText("Drücken um Zustand einzustellen");
        Button connect = (Button)findViewById(R.id.connect);
        connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect_grey, 0, 0);
        connect.setClickable(false);


        //--------------------------------------------------------------------------------//
        //------------Mit erster Tür verbinden wenn neu gestartet-------------------------//
        //--------------------------------------------------------------------------------//

        doorPrefs = new DoorPrefs(this);
        Door door;
        for(int i=0; i<=10; i++)
        {
            door = doorPrefs.getDoor(i);
            if (door.getDoorName().isEmpty())
            {

            }
            else
            {
                setValue(R.id.name, door.getDoorName());
                setValue(R.id.pass, door.getDoorPassword());
                break;
            }
        }


        //--------------------------------------------------------------------------------//
        //----------------Türen in den Spinner per Liste eintragen------------------------//
        //--------------------------------------------------------------------------------//

        int i;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<>();
        doorPrefs = new DoorPrefs(this);

        for(i=0; i<=10; i++)
        {
            door = doorPrefs.getDoor(i);

            if(door.getDoorName().isEmpty())
            {
                Log.i(TAG, "SPINNER: Doorname is empty");
            }
            else
            {
                String temp = "Tür: "+door.getDoorName();
                Log.i(TAG, "SPINNER: Door eingetragen");
                list.add(temp);
                Collections.sort(list);
                adapter = new ArrayAdapter(this, R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
            }
        }

    }

    //--------------------------------------------------------------------------------//
    //----------------Schließt, bzw. zerstört die Activity----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    //--------------------------------------------------------------------------------//
    //----------------Check i no ned ganz---------------------------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null)
            {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++)
                {
                    messages[i] = (NdefMessage) rawMessages[i];
                }
            }
        }
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
    //----------------Setzt was die Menüpunkte machen sollen--------------------------//
    //--------------------------------------------------------------------------------//
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Door door;
        switch (item.getItemId())
        {
            case R.id.button_getNFC:

                return true;

            case R.id.menu_openInfo:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Informationen geöffnet", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_list:
                int i;
                doorPrefs = new DoorPrefs(this);
                for(i=0; i<=10; i++)
                {
                    door = doorPrefs.getDoor(i);
                    Log.i(TAG, "LIST: id: "+door.getId() +" | name: "+door.getDoorName()
                            +" | status: "+door.getDoorStatus());
                }
                return true;

            case R.id.menu_delete:
                doorPrefs = new DoorPrefs(this);
                for(i=0; i<=10; i++)
                {
                    door = doorPrefs.getDoor(i);
                    doorPrefs.deleteDoor(door);
                }
                recreate();
                return true;

            case R.id.menu_status:
                doorPrefs = new DoorPrefs(this);
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                for(i=0; i<=9; i++)
                {
                    door = doorPrefs.getDoor(i);
                    String spinnertext = spinner.getSelectedItem().toString();
                    String text = spinnertext.replace("Tür: ", "");
                    if(door.getDoorName().equals(text))
                    {
                        Log.i(TAG, "STATUS: MAINACTIVITY: name: "+door.getDoorName());
                        if(door.getDoorStatus().isEmpty())
                        {
                            Toast.makeText(this, "Tür bitte einaml öffene oder schließen", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "STATUS: MAINACTIVITY: Tür einmal öffnen oder schließen");
                            break;
                        }
                        else if(door.getDoorStatus().contentEquals("door_close"))
                        {
                            Toast.makeText(this, ""+spinner.getSelectedItem().toString() +" geschlossen",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(door.getDoorStatus().contentEquals("door_open"))
                        {
                            Toast.makeText(this, ""+spinner.getSelectedItem().toString() +" offen",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "STATUS: MAINACTIVITY: status: "+door.getDoorStatus());
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Die beiden RadioButtons "Öffnen", "Schließen"-------------------//
    //--------------------------------------------------------------------------------//
    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        try
        {
            switch (view.getId())
            {
                case R.id.radio_button1:
                    if (checked)
                    {
                        setValue(R.id.bye, "door_open");
                        break;
                    }
                case R.id.radio_button2:
                    if (checked)
                    {
                        setValue(R.id.bye, "door_close");
                        break;
                    }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der "ToggleButton" Door-----------------------------------------//
    //--------------------------------------------------------------------------------//
    public void door(View view) throws Exception
    {
        Button button = (Button)findViewById(R.id.door);
        TextView textView = (TextView)findViewById(R.id.doorstate);
        Button connect = (Button)findViewById(R.id.connect);
        if(button.getText().equals("Drücken um Zustand einzustellen"))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor, 0, 0);
            button.setText("Schließen");
            textView.setText("door_close");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);
        }
        else if(button.getText().equals("Öffnen"))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor, 0, 0);
            button.setText("Schließen");
            textView.setText("door_close");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);
        }
        else if(button.getText().equals("Schließen"))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_openeddoor, 0, 0);
            button.setText("Öffnen");
            textView.setText("door_open");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);
        }
    }

    //--------------------------------------------------------------------------------//
    //----------------Der Button "Verbinden"------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void connect(View view) throws Exception
    {
        int i;
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String spinnertext = spinner.getSelectedItem().toString();
        String text = spinnertext.replace("Tür: ", "");
        System.out.println("Ausgewählt: "+text);
        doorPrefs = new DoorPrefs(this);
        Door door;

        for(i=0; i<=10; i++)
        {
            door = doorPrefs.getDoor(i);
            if(text.equals(door.getDoorName()))
            {
                System.out.println("" + door.getId());
                System.out.println(door.getDoorName());
                setValue(R.id.name, door.getDoorName());
                setValue(R.id.pass, door.getDoorPassword());
                break;
            }
            else
            {
                System.out.println("Falscher Name");
            }
        }

        try
        {
            Intent intent = new Intent(this, NFCHelper.class);
            intent.putExtra("password", getValue(R.id.pass));
            intent.putExtra("status", getValue(R.id.doorstate));
            intent.putExtra("name", getValue(R.id.name));
            startActivity(intent);
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Tür Auswählen"---------------------------------------//
    //--------------------------------------------------------------------------------//
    public void list(View view) throws Exception
    {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.performClick();
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Tür Bearbeiten"-------------------------------------//
    //--------------------------------------------------------------------------------//
    public void edit(View view) throws Exception
    {
        int i;
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String spinnertext = spinner.getSelectedItem().toString();
        String text = spinnertext.replace("Tür: ", "");
        System.out.println("Ausgewählt: "+text);
        doorPrefs = new DoorPrefs(this);
        Door door;

        for(i=0; i<=10; i++)
        {
            door = doorPrefs.getDoor(i);
            if(text.equals(door.getDoorName()))
            {
                System.out.println("" + door.getId());
                System.out.println(door.getDoorName());
                setValue(R.id.name, door.getDoorName());
                setValue(R.id.pass, door.getDoorPassword());
                Log.i(TAG, "EDIT: name of door: "+door.getDoorName());
                break;
            }
            else
            {
                Log.i(TAG, "EDIT: wrong name");
            }
        }

        DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.editDoors();
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Tür Hinzufügen"-------------------------------------//
    //--------------------------------------------------------------------------------//
    public void add(View view) throws Exception
    {
        DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.addDoors();
    }


    //--------------------------------------------------------------------------------//
    //----------------Inhalt von TextViews bekommen (Helfermethode)-------------------//
    //--------------------------------------------------------------------------------//
    private String getValue(int id)
    {
        TextView textView = (TextView)findViewById(id);
        String text = textView.getText().toString();
        return text;
    }


    //--------------------------------------------------------------------------------//
    //----------------Inhalt von TextViews beschreiben (Helfermethode)----------------//
    //--------------------------------------------------------------------------------//
    private void setValue(int id, String text)
    {
        TextView textView = (TextView)findViewById(id);
        textView.setText(text);
    }


    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog addDoors----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onAddDoorsListener(String editTextName, String editTextPass)
    {
        int i=0;

        if(editTextName.isEmpty()||editTextPass.isEmpty())
        {
            Toast.makeText(this, "Eingabe fehlerhaft", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ADD: Eingabe fehlerhaft");
        }
        else if(editTextName.length()<=15 || editTextPass.length()<=15)
        {
            Toast.makeText(this, "Eingabe über 15 Zeichen nicht gestattet", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ADD: Eingabe zu lang");
            return;
        }
        else
        {
            do
            {
                door = doorPrefs.getDoor(i);
                if(i>=9)
                {
                    System.out.println("ID >= 10");
                    Toast.makeText(this, "Nur 10 Türen möglich", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ADD: Zu viele Türen eingegeben");
                    break;
                }
                else
                {
                    i++;
                    Log.i(TAG, "ADD: Bereits hinzugefügte Id: "+i);
                }
            }
            while(!door.getDoorName().isEmpty());

            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            Log.i(TAG, "ADD: ID der hinzugefügten Tür:       "+door.getId());
            Log.i(TAG, "ADD: Name der hinzugefügten Tür:     "+door.getDoorName());
            Log.i(TAG, "ADD: Passwort der hinzugefügten Tür: "+door.getDoorPassword());
            Log.i(TAG, "ADD: Hinzufügen erfolgreich");
            recreate();
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog editDoors---------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onEditDoorsListener(String editTextName, String editTextPass)
    {
        Intent intent = new Intent(this, DialogHelper.class);
        int i;
        int id=0;
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        intent.putExtra("spinner", spinner.getSelectedItem().toString());
        recreate();

        if(editTextName.isEmpty()||editTextPass.isEmpty())
        {
            Toast.makeText(this, "Eingabe fehlerhaft", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "EDIT: Eingabe fehlerhaft");
        }
        else if(editTextName.length()<=15 || editTextPass.length()<=15)
        {
            Toast.makeText(this, "Eingabe über 15 Zeichen nicht gestattet", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ADD: Eingabe zu lang");
            return;
        }
        else
        {
            for(i=0; i<=9; i++)
            {
                door = doorPrefs.getDoor(i);
                int doorid = door.getId();
                int listid = spinner.getSelectedItemPosition();
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace("Tür: ", "");

                if(door.getDoorName().equals(text))
                {
                    id = door.getId();
                    Log.i(TAG, "EDIT: Zu bearbeitente Id:   doorid:   "+doorid +" | listid: "+listid);
                    Log.i(TAG, "EDIT: Zu bearbeitente Tür:  doorname: "+door.getDoorName() + " | listname: "+text);
                    break;
                }
                else
                {
                    Log.i(TAG, "EDIT: Bleibende Id:    doorid:   "+doorid +" | listid: "+listid);
                    Log.i(TAG, "EDIT: Bleibende Tür:   doorname: "+door.getDoorName() + " | listname: "+text);
                }
            }

            door = doorPrefs.getDoor(id);
            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            Log.i(TAG, "EDIT: Neuer Name:     "+door.getDoorName());
            Log.i(TAG, "EDIT: Neues Passwort: "+door.getDoorPassword());
            Log.i(TAG, "EDIT: Bearbeiten erfolgreich");
            recreate();
        }
    }

    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog editDoors, der Button delete--------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onDeleteDoorsListener(String editTextName, String editTextPass)
    {
        DoorPrefs doorPrefs = new DoorPrefs(this);
        Door door;
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int i;

        for(i=0; i<=9; i++)
        {
            door = doorPrefs.getDoor(i);
            int listid = spinner.getSelectedItemPosition();
            String spinnertext = spinner.getSelectedItem().toString();
            String text = spinnertext.replace("Tür: ", "");
            if(door.getDoorName().equals(text))
            {
                doorPrefs.deleteDoor(door);
                list.remove(listid);

                Log.i(TAG, "DELETE: Zu löschende Id:   doorid:            "+door.getId() +" equals with: listid "+listid);
                Log.i(TAG, "DELETE: Zu löschender Name:   dorname:        "+door.getDoorName());
                Log.i(TAG, "DELETE: Zu löschendes Passwort:   doorpass:   "+door.getDoorPassword());
                Log.i(TAG, "DELETE: Löschen erfolgreich");
                recreate();

                break;

            }
            else
            {
                Log.i(TAG, "DELETE: Soll nicht gelöscht werden: id: "+door.getDoorName());
            }
        }
    }
}
