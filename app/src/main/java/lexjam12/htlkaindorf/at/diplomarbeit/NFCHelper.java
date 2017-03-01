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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NFCHelper extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    private static final String TAG = NFCHelper.class.getSimpleName();
    Intent setIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


    }


    //--------------------------------------------------------------------------------//
    //----------------Schließt, bzw. zerstört die Activity----------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "DESTROY: NFCHelper");
        Log.i(TAG, "DESTROY: -------------------------------------------------");
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
    //----------------Scheibt auf NFC-Karte-------------------------------------------//
    //--------------------------------------------------------------------------------//
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Intent getintent = getIntent();
            final String password = getintent.getStringExtra("password");
            final String status = getintent.getStringExtra("status");
            final String name = getintent.getStringExtra("name");

            setIntent = new Intent(this, MainActivity.class);
            TextView detected = (TextView)findViewById(R.id.detected);
            TextView writen = (TextView)findViewById(R.id.writen);
            TextView formatable = (TextView)findViewById(R.id.formatable);
            detected.setText("detected");
            setIntent.putExtra("detected", detected.getText());

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage(""+password + status);

            writeNdefMessage(tag, ndefMessage);
            setIntent.putExtra("writen", writen.getText());
            setIntent.putExtra("formatabe", formatable.getText());

            Log.i(TAG, "CONNECT: Daten auf NFC Karte geschrieben");
            Log.i(TAG, "CONNECT: Passwort: "+password);
            Log.i(TAG, "CONNECT: Status:   "+status);
            Log.i(TAG, "CONNECT: -------------------------------------------------");


            Door door;
            DoorPrefs doorPrefs = new DoorPrefs(this);
            int i;
            for (i = 0; i <= 9; i++)
            {
                door = doorPrefs.getDoor(i);
                if (door.getDoorName().equals(name))
                {
                    Log.i(TAG, "STATUS: NFCHELPER: name: " + door.getDoorName());
                    door.setDoorStatus(status);
                    Log.i(TAG, "STATUS: NFCHELPER: status: " + door.getDoorStatus());
                    doorPrefs.setDoor(door);
                    break;
                }
                else
                    Log.i(TAG, "STATUS: NFCHELPER: noname: " + door.getDoorName());
            }
            Log.i(TAG, "STATUS: NFCHELPER: Daten übergeben");
            startActivity(setIntent);
            finish();
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
                TextView textView = (TextView)findViewById(R.id.formatable);
                textView.setText("not_formatable");
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
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
                    TextView textView = (TextView)findViewById(R.id.writen);
                    textView.setText("not_writen");

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                TextView textView = (TextView)findViewById(R.id.writen);
                textView.setText("writen");
            }

        }
        catch (Exception e)
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
