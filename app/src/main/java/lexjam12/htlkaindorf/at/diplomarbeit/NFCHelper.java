package lexjam12.htlkaindorf.at.diplomarbeit;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class NFCHelper extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    private static final String TAG = NFCHelper.class.getSimpleName();

    //--------------------------------------------------------------------------------//
    //----------------Inhalt von TextViews beschreiben (Helfermethode)----------------//
    //--------------------------------------------------------------------------------//
    private void setValue(int id, String text)
    {
        try
        {
            TextView textView = (TextView)findViewById(id);
            textView.setText(text);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private String getValue(int id)
    {
        TextView textView = (TextView)findViewById(id);
        String text = textView.getText().toString();
        return text;
    }

    //--------------------------------------------------------------------------------//
    //----------------Setzt alles was beim öffnen der App sein soll------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = getIntent();
        final String password = intent.getStringExtra("password");
        final String status = intent.getStringExtra("status");
        final String name = intent.getStringExtra("name");
        setValue(R.id.nfc_pass, password);
        setValue(R.id.nfc_status, status);
        setValue(R.id.nfc_name, name);
    }


    //--------------------------------------------------------------------------------//
    //----------------Schließt, bzw. zerstört die Activity----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        disableForegroundDispatchSystem();
    }


    //--------------------------------------------------------------------------------//
    //----------------Scheibt auf NFC-Karte-------------------------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Toast.makeText(this, "NFC erkannt!", Toast.LENGTH_SHORT).show();

            String pass = getValue(R.id.nfc_pass);
            String status = getValue(R.id.nfc_status);
            String name = getValue(R.id.nfc_name);

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage(""+pass+status);

            writeNdefMessage(tag, ndefMessage);

            Intent intentset = new Intent(this, MainActivity.class);

            Door door;
            DoorPrefs doorPrefs = new DoorPrefs(this);
            int i;
            for(i=0; i<=9; i++)
            {
                door = doorPrefs.getDoor(i);
                if(door.getDoorName().equals(name))
                {
                    Log.i(TAG, "STATUS: NFCHELPER: name: "+door.getDoorName());
                    door.setDoorStatus(status);
                    Log.i(TAG, "STATUS: NFCHELPER: status: "+door.getDoorStatus());
                    doorPrefs.setDoor(door);
                    break;
                }
                else
                    Log.i(TAG, "STATUS: NFCHELPER: noname: "+door.getDoorName());
            }
            Log.i(TAG, "STATUS: NFCHELPER: Daten übergeben");
            startActivity(intentset);
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Falls NFC erkannt wird, wird nicht Karte gelesen----------------//
    //--------------------------------------------------------------------------------//
    private void enableForegroundDispatchSystem()
    {
        Intent intent = new Intent(this, NFCHelper.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }


    //--------------------------------------------------------------------------------//
    //----------------Karte wird gelesen----------------//
    //--------------------------------------------------------------------------------//
    private void disableForegroundDispatchSystem()
    {
        nfcAdapter.disableForegroundDispatch(this);
    }


    //--------------------------------------------------------------------------------//
    //----------------Formatiert die NFC-Karte----------------------------------------//
    //--------------------------------------------------------------------------------//
    private void formatTag(Tag tag, NdefMessage ndefMessage)
    {
        try
        {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);  //ndef = legt Datenvormat für Übertragung fest

            if (ndefFormatable == null)
            {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag writen!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage)
    {
        try
        {
            if (tag == null)
            {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);      //Provides access to NDEF content and operations on a Tag

            if (ndef == null)
            {
                formatTag(tag, ndefMessage);
            }
            else
            {
                ndef.connect();

                if (!ndef.isWritable())
                {
                    Toast.makeText(this, "NFC-Karte kann nicht beschreiben werden!", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "NFC-Karte beschrieben!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e)
        {
            Log.e("writeNdefMessage", e.getMessage());
        }
    }

    private NdefRecord createTextRecord(String content)
    {
        try
        {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content)
    {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

}
