package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogHelper.OnConnectButtonListener,
                                                                DialogHelper.OnAddDoorsListener,
                                                                DialogHelper.OnEditDoorsListener
{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DoorPrefs prefs = new DoorPrefs(this);

        int i=0;
        Door door = prefs.getDoor(i);


        TextView mTextView = (TextView) findViewById(R.id.things);

        TextView textname = (TextView)findViewById(R.id.name);
        TextView textpass = (TextView)findViewById(R.id.pass);
        textname.setText(""+door.getDoorName());
        textpass.setText(""+door.getDoorPassword());

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null)
        {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
        if (!mNfcAdapter.isEnabled())
        {
            mTextView.setText("NFC is disabled.");
        }
        else
        {
            mTextView.setText(R.string.connect);
        }
    }

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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

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
                        TextView t = (TextView)findViewById(R.id.bye);
                        t.setText("door_open");
                        break;
                    }
                case R.id.radio_button2:
                    if (checked)
                    {
                        TextView t = (TextView)findViewById(R.id.bye);
                        t.setText("door_close");
                        break;
                    }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void stateRequest(View view) throws Exception
    {
        DialogHelper connect = new DialogHelper(this);
        connect.addDoors();


        //Löschen

//        DoorPrefs prefs = new DoorPrefs(this);
//        int i;
//        for(i=0; i<=10; i++) {
//            Door door = prefs.getDoor(i);
//            prefs.deleteDoor(door);
//        }
    }

    public void connect(View view) throws Exception
    {
        DialogHelper doorList = new DialogHelper(this);
        doorList.doorList();


//        DoorPrefs prefs = new DoorPrefs(this);
//        Door door = prefs.getDoor(4);
//        System.out.println("Vierte: "+door.getDoorName());


        try
        {
//            DialogHelper connect = new DialogHelper(this);
//            connect.connectButton();
//            connect.addDoors();
//            connect.editDoors();
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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

    public void setPreference(String key, String name)
    {
        SharedPreferences sp = getSharedPreferences("key", 0);
        SharedPreferences.Editor sedt = sp.edit();
        sedt.putString(key, name);
        sedt.commit();
    }

    public String getPreference(String key, String valueIfNotExistend)
    {
        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString(key, valueIfNotExistend);
        return tValue;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

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
                    DialogHelper dialogHelper = new DialogHelper(this);
                    dialogHelper.deleteDoors();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return true;

            case R.id.addDoor:
                try
                {
                    DialogHelper doorList = new DialogHelper(this);
                    doorList.doorList();
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
                String name1 =getPreference("name2", "Not here");
                String pass1 =getPreference("pass1", "Not here");
                Toast.makeText(this, "Pass: "+pass1+" Name: "+name1, Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

    @Override
    public void onAddDoorsListener(String editTextName, String editTextPass)
    {
        TextView textViewName = (TextView)findViewById(R.id.name);
        TextView textViewPass = (TextView)findViewById(R.id.pass);

        DoorPrefs prefs = new DoorPrefs(this);
        Door door;
        int i=0;
        Door door2 = prefs.getDoor(3);

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
            System.out.println("Door 3: "+door2.getDoorName());
            textViewName.setText(editTextName);
            textViewPass.setText(editTextPass);
            door.setDoorPassword("" + textViewPass.getText());
            door.setDoorName("" + textViewName.getText());
        }

        prefs.setDoor(door);
    }

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

    private String getValue(int id)
    {
        TextView textView = (TextView)findViewById(id);
        String text = textView.getText().toString();
        return text;
    }
}
