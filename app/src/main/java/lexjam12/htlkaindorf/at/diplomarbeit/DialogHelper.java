package lexjam12.htlkaindorf.at.diplomarbeit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by jakob on 19.10.16.
 */
public class DialogHelper extends AppCompatActivity
{
    private final Context context;

    //--------------------------------------------------------------------------------//
    //----------------Konstruktor-----------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public DialogHelper(Context context) throws Exception
    {
        this.context = context;

        try
        {
            onConnectButtonListener = (OnConnectButtonListener) context;
            onAddDoorsListener = (OnAddDoorsListener) context;
            onEditDoorsListener = (OnEditDoorsListener) context;
        }
        catch(ClassCastException ex)
        {
            throw new ClassCastException(context.toString()+ " must Implement Listener");
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Zustand der Tür anzeigt------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog stateRequest()
    {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.stateRequest);
        builder.setMessage("*Zustand der Tür");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der den Versionsverlauf anzeigt--------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog versionHistory()
    {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.version_history);
        builder.setMessage(
                 "Version 0.0.1: \n\t\t\tGUI Aufbauen\n\n"
                +"Version 0.0.2: \n\t\t\tMehrsprachigkeit implementieren\n\n"
                +"Version 0.0.3: \n\t\t\tToggleButton hinzufügen\n\n"
                +"Version 0.0.4: \n\t\t\tDialogs implementieren\n\n"
                +"Version 0.0.5: \n\t\t\tNFC Zustand anzeigen\n");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog fragt ob bearbeiten oder hinzufügen----------------------//
    //--------------------------------------------------------------------------------//
    public Dialog editAddDoors()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Tür hinzufügen oder bearbeiten?");
        builder.setPositiveButton("Tür hinzufügen", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                addDoors();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Tür bearbeiten", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                editDoors();
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Listener von addDoor--------------------------------------------//
    //--------------------------------------------------------------------------------//
    private final OnAddDoorsListener onAddDoorsListener;

    public interface OnAddDoorsListener
    {
        void onAddDoorsListener(String editTextName, String editTextPass);
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Türe hinzufügt--------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog addDoors()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_adddoor, null);
        final EditText editText_name = (EditText)view.findViewById(R.id.doorName_add);
        final EditText editText_pass = (EditText)view.findViewById(R.id.doorPassword_add);

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Tür hinzufügen");
        builder.setView(view);
        builder.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onAddDoorsListener.onAddDoorsListener(editText_name.getText().toString(), editText_pass.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Listener von editDoor-------------------------------------------//
    //--------------------------------------------------------------------------------//
    private final OnEditDoorsListener onEditDoorsListener;

    public interface OnEditDoorsListener
    {
        void onEditDoorsListener(String editTextName, String editTextPass);
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Türen bearbeitet-------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog editDoors()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_editdoor, null);
        final EditText editText_name = (EditText)view.findViewById(R.id.doorName_edit);
        final EditText editText_pass = (EditText)view.findViewById(R.id.doorPassword_edit);
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Tür bearbeiten");
        builder.setView(view);
        builder.setPositiveButton("Bearbeiten", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onEditDoorsListener.onEditDoorsListener(editText_name.getText().toString(), editText_pass.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Türen löscht-----------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog deleteDoors()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_deletedoor, null);
        final EditText editText_name = (EditText)view.findViewById(R.id.doorName_edit);
        final EditText editText_pass = (EditText)view.findViewById(R.id.doorPassword_add);
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Tür bearbeiten");
        builder.setView(view);
        builder.setPositiveButton("Bearbeiten", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------WTF!!!! Keine Ahnung--------------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog nfc()
    {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.activity_nfc);
        builder.setTitle(R.string.stateRequest);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                EditText editText = (EditText)findViewById(R.id.nfc_tag);
                String nfc = editText.getText().toString();
                Toast.makeText(context, ""+nfc, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }


    //--------------------------------------------------------------------------------//
    //----------------Listener von connectButton-------------------------------------------//
    //--------------------------------------------------------------------------------//
    private final OnConnectButtonListener onConnectButtonListener;

    public interface OnConnectButtonListener
    {
        void onConnectButtonListener(String editText);
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der nach Passwort fragt----------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog connectButton()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_connectbutton, null);
        final EditText editText = (EditText)view.findViewById(R.id.nfc_tag);

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Passwort");
        builder.setView(view);

        builder.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onConnectButtonListener.onConnectButtonListener(editText.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(context, R.string.connectCancel, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }
}