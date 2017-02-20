package lexjam12.htlkaindorf.at.diplomarbeit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jakob on 19.10.16.
 */
public class DialogHelper extends AppCompatActivity
{
    private final Context context;
    private static final String TAG = DialogHelper.class.getSimpleName();

    //--------------------------------------------------------------------------------//
    //----------------Konstruktor-----------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public DialogHelper(Context context) throws Exception
    {
        this.context = context;

        try
        {
            onAddDoorsListener = (OnAddDoorsListener) context;
            onEditDoorsListener = (OnEditDoorsListener) context;
            onDeleteDoorsListener = (OnDeleteDoorsListener) context;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException(context.toString() + " must Implement Listener");
        }
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Zustand der Tür anzeigt------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog stateRequest()
    {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        builder.setTitle("state");
        builder.setMessage("*Zustand der Tür");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
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
        builder.setTitle("verionhistory");
        builder.setMessage(
                "Version 0.0.1: \n\t\t\tGUI Aufbauen\n\n"
                        + "Version 0.0.2: \n\t\t\tMehrsprachigkeit implementieren\n\n"
                        + "Version 0.0.3: \n\t\t\tToggleButton hinzufügen\n\n"
                        + "Version 0.0.4: \n\t\t\tDialogs implementieren\n\n"
                        + "Version 0.0.5: \n\t\t\tNFC Zustand anzeigen\n");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
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
    //----------------Listener von addDoor--------------------------------------------//
    //--------------------------------------------------------------------------------//
    private final OnAddDoorsListener onAddDoorsListener;

    public interface OnAddDoorsListener
    {
        void onAddDoorsListener(String editTextName, String editTextPass);
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Türen hinzufügt--------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog addDoors()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_adddoor, null);
        final EditText editText_name = (EditText) view.findViewById(R.id.doorName_add);
        final EditText editText_pass = (EditText) view.findViewById(R.id.doorPassword_add);

        builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.button_add));
        builder.setView(view);
        builder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onAddDoorsListener.onAddDoorsListener(editText_name.getText().toString(), editText_pass.getText().toString());
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
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

    private final OnDeleteDoorsListener onDeleteDoorsListener;

    public interface OnDeleteDoorsListener
    {
        void onDeleteDoorsListener(String editTextName, String editTextPass);
    }


    //--------------------------------------------------------------------------------//
    //----------------Dialog der Türen bearbeitet-------------------------------------//
    //--------------------------------------------------------------------------------//
    public Dialog editDoors()
    {
        AlertDialog.Builder builder;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_editdoor, null);
        final EditText editText_name = (EditText) view.findViewById(R.id.doorName_edit);
        final EditText editText_pass = (EditText) view.findViewById(R.id.doorPassword_edit);
        builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.button_edit));
        builder.setView(view);
        builder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onEditDoorsListener.onEditDoorsListener(editText_name.getText().toString(), editText_pass.getText().toString());
            }
        });
        builder.setNeutralButton(context.getString(R.string.delete), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onDeleteDoorsListener.onDeleteDoorsListener(editText_name.getText().toString(), editText_pass.getText().toString());
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.i(TAG, "EDIT: Bearbeiten abgebrochen");
                Log.i(TAG, "EDIT: ----------------------------------------------------");
                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder.create();
    }
}