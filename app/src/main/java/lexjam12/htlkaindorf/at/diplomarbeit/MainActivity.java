package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)

public class MainActivity extends AppCompatActivity implements DialogHelper.OnAddDoorsListener, DialogHelper.OnEditDoorsListener, DialogHelper.OnDeleteDoorsListener
{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();
    Door door;
    DoorPrefs doorPrefs;
    NfcAdapter mNfcAdapter;
    ArrayAdapter adapter;
    List<String> list;


    //--------------------------------------------------------------------------------//
    //----------------Setzt alles was beim öffenen der App sein soll------------------//
    //--------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "ONCREATE: MainActivity gestartet");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        //--------------------------------------------------------------------------------//
        //----------------Setzt provesorisch den Status vom NFC---------------------------//
        //--------------------------------------------------------------------------------//

        if (mNfcAdapter == null)
        {
            //----------------Falls Gerät NFC nicht unterstützt -> Abbruch--------------------//
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), getResources().getString(R.string.no_nfc),
                    Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.close),
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            onDestroy();
                        }
                    });
            View sbView = snackbar.getView();
            snackbar.setActionTextColor(getResources().getColor(R.color.white, null));
            sbView.setBackgroundColor(getResources().getColor(R.color.red, null));
            snackbar.show();

            Log.i(TAG, "ONCREATE: NFC: Gerät besitzt kein NFC");
        }
        if (!mNfcAdapter.isEnabled())
        {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), getResources().getString(R.string.nfc_off),
                    Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.retry),
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            recreate();
                        }
                    });
            View sbView = snackbar.getView();
            snackbar.setActionTextColor(getResources().getColor(R.color.white, null));
            sbView.setBackgroundColor(getResources().getColor(R.color.red, null));
            snackbar.show();

            Log.i(TAG, "ONCREATE: NFC: NFC ist ausgeschlatet");
        }
        else
        {
            Log.i(TAG, "ONCREATE: NFC: NFC ist eingeschaltet");
        }


        //--------------------------------------------------------------------------------//
        //------------Setzt die Buttons wie sie am Anfang eingsestellt sein sollen--------//
        //--------------------------------------------------------------------------------//
        Button button = (Button) findViewById(R.id.door);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor_grey, 0, 0);
        button.setText(R.string.button_state_undefined);
        Button connect = (Button) findViewById(R.id.connect);
        connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect_grey, 0, 0);


        //--------------------------------------------------------------------------------//
        //------------Mit erster Tür verbinden wenn neu gestartet-------------------------//
        //--------------------------------------------------------------------------------//
        doorPrefs = new DoorPrefs(this);
        Door door;
        for (int i = 0; i <= 10; i++)
        {
            door = doorPrefs.getDoor(i);
            if (!door.getDoorName().isEmpty())
            {
                setValue(R.id.name, door.getDoorName());
                setValue(R.id.pass, door.getDoorPassword());
                Log.i(TAG, "ONCREATE: Mit Tür "+door.getDoorName() +" verbunden");
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

        for (i = 0; i <= 10; i++)
        {
            door = doorPrefs.getDoor(i);

            if (!door.getDoorName().isEmpty())
            {
                String temp = getResources().getString(R.string.textview_doorname)+" "+door.getDoorName();
                list.add(temp);
                Collections.sort(list);
                adapter = new ArrayAdapter(this, R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
            }
        }
        Log.i(TAG, "ONCREATE: SPINNER: Türen in Spinner eingetragen");
        Log.i(TAG, "ONCREATE: ------------------------------------------------");

    }

    //--------------------------------------------------------------------------------//
    //----------------Schließt, bzw. zerstört die Activity----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "DESTROY: MainAcivity");
        Log.i(TAG, "DESTROY: -------------------------------------------------");
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
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        switch (item.getItemId())
        {
            case R.id.button_getNFC:
                if(mNfcAdapter.isEnabled())
                {
                    Log.i(TAG, "MENU: NFC ist eingeschalten");
                    snackbarHelper(getResources().getString(R.string.nfc_on),
                            getResources().getColor(R.color.colorPrimary, null));
                }
                else 
                {
                    Log.i(TAG, "MENU: NFC ist ausgeschalten");
                    snackbarHelper(getResources().getString(R.string.nfc_off),
                            getResources().getColor(R.color.colorPrimary, null));
                }
                return true;

            case R.id.menu_openInfo:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                Log.i(TAG, "MENU: Informationen geöffnet");
                Log.i(TAG, "MENU: ----------------------------------------------------");
                return true;

            case R.id.menu_list:
                int i;
                doorPrefs = new DoorPrefs(this);
                for (i = 0; i <= 9; i++)
                {
                    door = doorPrefs.getDoor(i);
                    Log.i(TAG, "MENU: LIST: id: " + door.getId() + " | name: " + door.getDoorName() + " | status: " + door.getDoorStatus());
                }
                Log.i(TAG, "MENU: LIST: ----------------------------------------------");
                return true;

            case R.id.menu_delete:
                doorPrefs = new DoorPrefs(this);
                for (i = 0; i <= 10; i++)
                {
                    door = doorPrefs.getDoor(i);
                    doorPrefs.deleteDoor(door);
                }
                toastHelper(getResources().getString(R.string.deleted_all), R.drawable.toast_blue);

                Log.i(TAG, "MENU: Alle Türen gelöscht");
                Log.i(TAG, "MENU: ----------------------------------------------------");
                recreate();
                return true;

            case R.id.menu_status:
                doorPrefs = new DoorPrefs(this);
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                for (i = 0; i <= 9; i++)
                {
                    door = doorPrefs.getDoor(i);
                    if(spinner.getCount()==0)
                    {
                        snackbarHelper(getResources().getString(R.string.no_door_chosen),
                                getResources().getColor(R.color.red, null));

                        Log.i(TAG, "MENU: STATUS: Keine Tür ausgewählt");
                        Log.i(TAG, "MENU: STATUS: --------------------------------------------");
                        break;
                    }
                    else
                    {
                        String spinnertext = spinner.getSelectedItem().toString();
                        String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");

                        if (door.getDoorName().equals(text))
                        {
                            Log.i(TAG, "MENU: STATUS: name: " + door.getDoorName());

                            if (door.getDoorStatus().isEmpty())
                            {
                                snackbarHelper(getResources().getString(R.string.door_open_or_close),
                                        getResources().getColor(R.color.red, null));

                                Log.i(TAG, "MENU: STATUS: Tür einmal öffnen oder schließen");
                                Log.i(TAG, "MENU: STATUS: --------------------------------------------");
                                break;
                            }
                            else if (door.getDoorStatus().contentEquals("door_close"))
                            {
                                snackbarHelper(spinner.getSelectedItem().toString()
                                        + " "+getResources().getString(R.string.closed),
                                        getResources().getColor(R.color.colorPrimary, null));
                            }
                            else if (door.getDoorStatus().contentEquals("door_open"))
                            {
                                snackbarHelper(spinner.getSelectedItem().toString()
                                                + " "+getResources().getString(R.string.opened),
                                        getResources().getColor(R.color.colorPrimary, null));
                            }
                            Log.i(TAG, "MENU: STATUS: status: " + door.getDoorStatus());
                            Log.i(TAG, "MENU: STATUS: --------------------------------------------");
                        }
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der "ToggleButton" Door-----------------------------------------//
    //--------------------------------------------------------------------------------//
    public void door(View view) throws Exception
    {
        Button button = (Button) findViewById(R.id.door);
        TextView textView = (TextView) findViewById(R.id.doorstate);
        Button connect = (Button) findViewById(R.id.connect);
        if (button.getText().equals(getResources().getString(R.string.button_state_undefined)))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor, 0, 0);
            button.setText(getResources().getString(R.string.button_state_close));
            textView.setText("door_close");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);

            Log.i(TAG, "DOORBUTTON: Tür schließen");
            Log.i(TAG, "DOORBUTTON: ----------------------------------------------");
        }
        else if (button.getText().equals(getResources().getString(R.string.button_state_open)))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_closeddoor, 0, 0);
            button.setText(getResources().getString(R.string.button_state_close));
            textView.setText("door_close");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);

            Log.i(TAG, "DOORBUTTON: Tür schließen");
            Log.i(TAG, "DOORBUTTON: ----------------------------------------------");
        }
        else if (button.getText().equals(getResources().getString(R.string.button_state_close)))
        {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_openeddoor, 0, 0);
            button.setText(getResources().getString(R.string.button_state_open));
            textView.setText("door_open");
            connect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icon_connect, 0, 0);
            connect.setClickable(true);

            Log.i(TAG, "DOORBUTTON: Tür öffnen");
            Log.i(TAG, "DOORBUTTON: ----------------------------------------------");
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Verbinden"------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void connect(View view) throws Exception
    {
        Log.i(TAG, "CONNECT: gestartet");
        int i;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        doorPrefs = new DoorPrefs(this);
        Door door;

        if(getValue(R.id.doorstate).isEmpty())
        {
            snackbarHelper(getResources().getString(R.string.select_state),
                    getResources().getColor(R.color.red, null));

            Log.i(TAG, "CONNECT: Zustand der Tür auswählen");
            return;
        }

        for (i = 0; i <= 9; i++)
        {
            door = doorPrefs.getDoor(i);

            if(door.getDoorName().isEmpty() && getValue(R.id.name).isEmpty())
            {
                snackbarHelper(getResources().getString(R.string.no_door_connect),
                        getResources().getColor(R.color.red, null));

                Log.i(TAG, "CONNECT: Keine Tür zum Verbinden");
                Log.i(TAG, "CONNECT:--------------------------------------------------");
                return;
            }
            else
            {
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");
                Log.i(TAG, "CONNECT: Ausgewählt: "+text);

                if (text.equals(door.getDoorName()))
                {
                    Log.i(TAG, "CONNECT: name: "+door.getDoorName());
                    Log.i(TAG, "CONNECT: pass: "+door.getDoorPassword());
                    setValue(R.id.name, door.getDoorName());
                    setValue(R.id.pass, door.getDoorPassword());
                    break;
                }
                else
                {
                    Log.i(TAG, "CONNECT: Falscher Name");
                    Log.i(TAG, "CONNECT:--------------------------------------------------");
                }
            }
        }

        try
        {
            Intent intent = new Intent(this, NFCHelper.class);
            intent.putExtra("password", getValue(R.id.pass));
            intent.putExtra("status", getValue(R.id.doorstate));
            intent.putExtra("name", getValue(R.id.name));
            Log.i(TAG, "CONNECT: Daten an NFCHelper gesendet");
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Tür Auswählen"---------------------------------------//
    //--------------------------------------------------------------------------------//
    public void list(View view) throws Exception
    {
        Log.i(TAG, "LIST: gestartet");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Log.i(TAG, "LIST: Anzahl an Türen: " + spinner.getCount());
        if (spinner.getCount() == 0)
        {
            snackbarHelper(getResources().getString(R.string.no_doors_saved),
                    getResources().getColor(R.color.red, null));

            Log.i(TAG, "LIST: Keine Türen gespeichert");
            Log.i(TAG, "LIST: ----------------------------------------------------");
        }
        else
        {
            Log.i(TAG, "LIST: ----------------------------------------------------");
            spinner.performClick();
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Tür Bearbeiten"-------------------------------------//
    //--------------------------------------------------------------------------------//
    public void edit(View view) throws Exception
    {
        Log.i(TAG, "EDIT: gestartet");
        int i;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        doorPrefs = new DoorPrefs(this);
        Door door;

        for (i = 0; i <= 9; i++)
        {
            door = doorPrefs.getDoor(i);
            if(door.getDoorName().isEmpty() && getValue(R.id.name).isEmpty())
            {
                snackbarHelper(getResources().getString(R.string.no_door_edit),
                        getResources().getColor(R.color.red, null));

                Log.i(TAG, "EDIT: Keine Tür zum Bearbeiten ausgewählt");
                Log.i(TAG, "EDIT: ----------------------------------------------------");
                return;
            }
            else
            {
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");
                if (door.getDoorName().equals(text))
                {
                    setValue(R.id.name, door.getDoorName());
                    setValue(R.id.pass, door.getDoorPassword());
                    Log.i(TAG, "EDIT: name: " + door.getDoorName() + " | id: " + door.getId());
                    break;
                }
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
        Log.i(TAG, "ADD: gestartet");
        int i=0;
        doorPrefs = new DoorPrefs(this);

        do
        {
            door = doorPrefs.getDoor(i);
            if(door.getId() >=10)
            {
                snackbarHelper(getResources().getString(R.string.ten_doors),
                        getResources().getColor(R.color.red, null));

                Log.i(TAG, "ADD: Zu viele Türen id: "+door.getId());
                return;
            }
            else
            {
                i++;
            }
        }
        while (!door.getDoorName().isEmpty());

        DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.addDoors();
    }


    //--------------------------------------------------------------------------------//
    //----------------Inhalt von TextViews bekommen (Helfermethode)-------------------//
    //--------------------------------------------------------------------------------//
    private String getValue(int id)
    {
        TextView textView = (TextView) findViewById(id);
        String text = textView.getText().toString();
        return text;
    }


    //--------------------------------------------------------------------------------//
    //----------------Inhalt von TextViews beschreiben (Helfermethode)----------------//
    //--------------------------------------------------------------------------------//
    private void setValue(int id, String text)
    {
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }


    //--------------------------------------------------------------------------------//
    //----------------------Erzeugt Snackbars (Helfermethode)-------------------------//
    //--------------------------------------------------------------------------------//
    public void snackbarHelper(String text, int color)
    {
        View mainview = findViewById(R.id.activity_main);
        Snackbar snackbar = Snackbar.make(mainview, text, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        view.setBackgroundColor(color);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();

    }

    //--------------------------------------------------------------------------------//
    //----------------------Erzeugt Toasts (Helfermethode)----------------------------//
    //--------------------------------------------------------------------------------//
    public void toastHelper(String text, int backgroud)
    {
        Toast toast = Toast.makeText(this, text,
                Toast.LENGTH_LONG);
        View toastview = toast.getView();
        toastview.setBackgroundResource(backgroud);
        toast.setGravity(Gravity.BOTTOM, 0 , 0);
        toast.setView(toastview);
        toast.show();
    }


    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog addDoors----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onAddDoorsListener(String editTextName, String editTextPass)
    {
        int i;
        if (editTextName.isEmpty() || editTextPass.isEmpty())
        {
            snackbarHelper(getResources().getString(R.string.wrong_input),
                    getResources().getColor(R.color.red, null));
            Log.i(TAG, "ADD: Eingabe fehlerhaft");
        }
        else if (editTextName.length() >= 15 || editTextPass.length() >= 15)
        {
            snackbarHelper(getResources().getString(R.string.too_long),
                    getResources().getColor(R.color.red, null));

            Log.i(TAG, "ADD: Eingabe zu lang");
            return;
        }
        else
        {
            for(i=0; i<=9; i++)
            {
                door = doorPrefs.getDoor(i);

                if(door.getDoorName().isEmpty())
                {
                    setValue(R.id.name, editTextName);
                    setValue(R.id.pass, editTextPass);
                    door.setDoorName(getValue(R.id.name));
                    door.setDoorPassword(getValue(R.id.pass));
                    doorPrefs.setDoor(door);
                    Log.i(TAG, "ADD: ID der hinzugefügten Tür:       " + door.getId());
                    Log.i(TAG, "ADD: Name der hinzugefügten Tür:     " + door.getDoorName());
                    Log.i(TAG, "ADD: Passwort der hinzugefügten Tür: " + door.getDoorPassword());
                    break;
                }
                else
                {
                    Log.i(TAG, "ADD: Bereits hinzugefügte Id: "+door.getId());
                }
            }
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
        int i;
        int id = 0;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

//        recreate();

        if (editTextName.length() >= 15 || editTextPass.length() >= 15)
        {
            snackbarHelper(getResources().getString(R.string.too_long),
                    getResources().getColor(R.color.red, null));

            Log.i(TAG, "ADD: Eingabe zu lang");
            Log.i(TAG, "EDIT: ----------------------------------------------------");
            return;
        }
        else if (editTextName.isEmpty() && editTextPass.isEmpty())
        {
            editTextName = getValue(R.id.name);
            editTextPass = getValue(R.id.pass);

            door = doorPrefs.getDoor(id);
            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            snackbarHelper(getResources().getString(R.string.no_change),
                    getResources().getColor(R.color.colorPrimary, null));

            Log.i(TAG, "EDIT: Keine Veränderung");
            Log.i(TAG, "EDIT: Bearbeiten erfolgreich");
            Log.i(TAG, "EDIT: ----------------------------------------------------");
        }
        else if (editTextName.isEmpty() && !editTextPass.isEmpty())
        {
            editTextName = getValue(R.id.name);

            for (i = 0; i <= 9; i++)
            {
                door = doorPrefs.getDoor(i);
                int doorid = door.getId();
                int listid = spinner.getSelectedItemPosition();
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");

                if (door.getDoorName().equals(text))
                {
                    Log.i(TAG, "EDIT: Zu bearbeitente Id:   doorid:   " + doorid + " | listid: " + listid);
                    Log.i(TAG, "EDIT: Zu bearbeitente Tür:  doorname: " + door.getDoorName() + " | listname: " + text);
                    break;
                }
            }

            door = doorPrefs.getDoor(id);
            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            toastHelper(getResources().getString(R.string.only_password), R.drawable.toast_blue);

            Log.i(TAG, "EDIT: Nur Passwort bearbeitet");
            Log.i(TAG, "EDIT: Name:           " + door.getDoorName());
            Log.i(TAG, "EDIT: Neues Passwort: " + door.getDoorPassword());
            Log.i(TAG, "EDIT: Bearbeiten erfolgreich");
            Log.i(TAG, "EDIT: ----------------------------------------------------");
            recreate();
        }
        else if (!editTextName.isEmpty() && editTextPass.isEmpty())
        {
            editTextPass = getValue(R.id.pass);

            for (i = 0; i <= 9; i++)
            {
                door = doorPrefs.getDoor(i);
                int doorid = door.getId();
                int listid = spinner.getSelectedItemPosition();
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");

                if (door.getDoorName().equals(text))
                {
                    Log.i(TAG, "EDIT: Zu bearbeitente Id:   doorid:   " + doorid + " | listid: " + listid);
                    Log.i(TAG, "EDIT: Zu bearbeitente Tür:  doorname: " + door.getDoorName() + " | listname: " + text);
                    break;
                }
            }

            door = doorPrefs.getDoor(id);
            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            toastHelper(getResources().getString(R.string.only_name), R.drawable.toast_blue);

            Log.i(TAG, "EDIT: Nur Name bearbeitet");
            Log.i(TAG, "EDIT: Neuer Name: " + door.getDoorName());
            Log.i(TAG, "EDIT: Passwort:   " + door.getDoorPassword());
            Log.i(TAG, "EDIT: Bearbeiten erfolgreich");
            Log.i(TAG, "EDIT: ----------------------------------------------------");
            recreate();
        }
        else
        {
            for (i = 0; i <= 9; i++)
            {
                door = doorPrefs.getDoor(i);
                int doorid = door.getId();
                int listid = spinner.getSelectedItemPosition();
                String spinnertext = spinner.getSelectedItem().toString();
                String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");

                if (door.getDoorName().equals(text))
                {
                    id = door.getId();
                    Log.i(TAG, "EDIT: Zu bearbeitente Id:   doorid:   " + doorid + " | listid: " + listid);
                    Log.i(TAG, "EDIT: Zu bearbeitente Tür:  doorname: " + door.getDoorName() + " | listname: " + text);
                    break;
                }
            }

            door = doorPrefs.getDoor(id);
            setValue(R.id.name, editTextName);
            setValue(R.id.pass, editTextPass);
            door.setDoorName(getValue(R.id.name));
            door.setDoorPassword(getValue(R.id.pass));
            doorPrefs.setDoor(door);

            toastHelper(getResources().getString(R.string.edited), R.drawable.toast_blue);

            Log.i(TAG, "EDIT: Neuer Name:     " + door.getDoorName());
            Log.i(TAG, "EDIT: Neues Passwort: " + door.getDoorPassword());
            Log.i(TAG, "EDIT: Bearbeiten erfolgreich");
            Log.i(TAG, "EDIT: ----------------------------------------------------");
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
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        int i;

        for (i = 0; i <= 9; i++)
        {
            door = doorPrefs.getDoor(i);
            int listid = spinner.getSelectedItemPosition();
            String spinnertext = spinner.getSelectedItem().toString();
            String text = spinnertext.replace(getResources().getString(R.string.textview_doorname)+" ", "");
            if (door.getDoorName().equals(text))
            {
                doorPrefs.deleteDoor(door);
                list.remove(listid);

                toastHelper(getResources().getString(R.string.deleted), R.drawable.toast_blue);


                Log.i(TAG, "DELETE:        Zu löschende Id:   doorid: " + door.getId() + " equals with: listid " + listid);
                Log.i(TAG, "DELETE:     Zu löschender Name:  dorname: " + door.getDoorName());
                Log.i(TAG, "DELETE: Zu löschendes Passwort: doorpass: " + door.getDoorPassword());
                Log.i(TAG, "DELETE: Löschen erfolgreich");
                Log.i(TAG, "DELETE: --------------------------------------------------");

                break;

            }
            else
            {
                Log.i(TAG, "DELETE: Soll nicht gelöscht werden: id: " + door.getDoorName());
                Log.i(TAG, "DELETE: --------------------------------------------------");
            }
        }
        recreate();
    }
}
