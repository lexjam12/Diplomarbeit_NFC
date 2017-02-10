package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  DialogHelper.OnAddDoorsListener,
                                                                DialogHelper.OnEditDoorsListener,
                                                                DialogHelper.OnConnectButtonListener
{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    //--------------------------------------------------------------------------------//
    //----------------Setzt alles was beim öffenen der App sein soll------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DoorPrefs prefs = new DoorPrefs(this);


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


        //--------------------------------------------------------------------------------//
        //------------Mit letzter hinzugefügter Tür verbinden wenn neu gestartet----------//
        //--------------------------------------------------------------------------------//

        for(int i=15; i>=0; i--)
        {
            Door door = prefs.getDoor(i);
            if (door.getDoorName().isEmpty())
            {

            }
            else
            {
                Toast.makeText(this, "Name: " + door.getDoorName(), Toast.LENGTH_SHORT).show();
                setValue(R.id.name, door.getDoorName());
                setValue(R.id.pass, door.getDoorPassword());
                break;
            }
        }


        //--------------------------------------------------------------------------------//
        //----------------Türen in den Spinner per Liste eintragen------------------------//
        //--------------------------------------------------------------------------------//

        int i=0;
        Door door;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        DoorPrefs doorPrefs = new DoorPrefs(this);

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

        for(i=0; i<=10; i++)
        {
            door = doorPrefs.getDoor(i);

            if(door.getDoorName().isEmpty())
            {
                System.out.println("------empty-----");
                break;
            }
            else
            {
                String temp = "Tür: "+door.getDoorName();
                System.out.println(""+door.getDoorName());
                list.add(temp);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        R.layout.spinner_item, list);
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
        switch (item.getItemId())
        {
            case R.id.button_getNFC:
//                NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
//
//                if(nfc != null && nfc.isEnabled())
//                {
//                    Toast.makeText(this, R.string.nfcOn, Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(this, R.string.nfcOff, Toast.LENGTH_SHORT).show();
//                }
                DoorPrefs prefs = new DoorPrefs(this);

                Door door1 = prefs.getDoor(2);

                prefs.setDoor(door1);

                System.out.println(door1.getDoorName() +"\n"+door1.getDoorPassword());
                System.out.println();
                return true;

            case R.id.openInfo:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Informationen geöffnet", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.delete:
                try
                {
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return true;

            case R.id.addDoor:
                try
                {
//                    prefs = new DoorPrefs(this);
//
//                    Door door;
//                    Door door2 = prefs.getDoor(2);
//                    Door door3 = prefs.getDoor(3);
//                    System.out.println("zweiter :"+door2.getDoorName());
//                    System.out.println("dritter :"+door3.getDoorName());
//
//                    do
//                    {
//                        door = prefs.getDoor(i);
//                        i++;
//                    }
//                    while(!door.getDoorName().isEmpty());
//
//                    System.out.println(""+door.getId());
//
//                    prefs.setDoor(door);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                return true;

            case R.id.selectDoor:
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
    //----------------Der Button "Verbinden"------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void connect(View view) throws Exception
    {
//        Intent intent = new Intent(this, SpinnerExample.class);
//        startActivity(intent);
        DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.connectButton();
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button "Zustand"--------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void stateRequest(View view) throws Exception
    {
        DialogHelper connect = new DialogHelper(this);
        connect.addDoors();



        //Löschen

//        DoorPrefs prefs = new DoorPrefs(this);
//        int i;
//        for(i=0; i<=100; i++) {
//            Door door = prefs.getDoor(i);
//            prefs.deleteDoor(door);
//        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Der Button Tür "Hinzufügen"-------------------------------------//
    //--------------------------------------------------------------------------------//
    public void editDoors(View view) throws Exception
    {
        try
        {
            Intent intent = new Intent(this, NFCHelper.class);
            intent.putExtra("password", getValue(R.id.name));
            intent.putExtra("toggle", getValue(R.id.bye));
            if(getValue(R.id.name).isEmpty())
                Toast.makeText(this, "Fehler: Passort eingeben", Toast.LENGTH_SHORT).show();
            else if(getValue(R.id.bye).isEmpty())
                Toast.makeText(this, "Fehler: Öffnen oder Schließen auswählen", Toast.LENGTH_SHORT).show();
            else
                startActivity(intent);
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
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
    //----------------Ein Listener auf den Dialog connectButton-----------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onConnectButtonListener(String editText)
    {
        Toast.makeText(this, editText, Toast.LENGTH_LONG).show();
        TextView textView = (TextView)findViewById(R.id.pass);

        DoorPrefs prefs = new DoorPrefs(this);

        Door door = prefs.getDoor(0);

        textView.setText(editText);
        door.setDoorPassword(""+textView.getText());

        prefs.setDoor(door);
        System.out.println(door.getDoorPassword());
    }


    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog addDoors----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onAddDoorsListener(String editTextName, String editTextPass)
    {
        TextView textViewName = (TextView)findViewById(R.id.name);
        TextView textViewPass = (TextView)findViewById(R.id.pass);

        int i=0;
        System.out.println("Es kommt rein");
        DoorPrefs prefs = new DoorPrefs(this);

        Door door;

        if(editTextName.isEmpty() || editTextPass.isEmpty())
        {
            Toast.makeText(this, "Eingabe", Toast.LENGTH_SHORT).show();
            System.out.println("Bitte Eingabe");
            return;
        }
        else
        {
            do
            {
                door = prefs.getDoor(i);
                i++;
            }
            while(!door.getDoorName().isEmpty());

            System.out.println(""+door.getId());
            textViewName.setText(editTextName);
            textViewPass.setText(editTextPass);
            door.setDoorPassword("" + textViewPass.getText());
            door.setDoorName("" + textViewName.getText());
        }
        prefs.setDoor(door);
        recreate();
    }


    //--------------------------------------------------------------------------------//
    //----------------Ein Listener auf den Dialog editDoors---------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    public void onEditDoorsListener(String editTextName, String editTextPass)
    {
        TextView textViewName = (TextView)findViewById(R.id.name);
        TextView textViewPass = (TextView)findViewById(R.id.pass);

        DoorPrefs prefs = new DoorPrefs(this);

        Door door = prefs.getDoor(0);

        prefs.deleteDoor(door);

        if(editTextName.isEmpty())
            return;
        else
            textViewName.setText(editTextName);

        if(editTextPass.isEmpty())
            return;
        else
            textViewPass.setText(editTextPass);

        door.setDoorPassword(""+textViewPass.getText());
        door.setDoorName(""+textViewName.getText());

        prefs.setDoor(door);
    }
}
