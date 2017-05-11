package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lexjam12 on 03.02.17.
 */

//--------------------------------------------------------------------------------//
//----------------Klasse zum Bearbeiten der Türen die gespeichert werden----------//
//--------------------------------------------------------------------------------//
class DoorPrefs
{
    //--------------------------------------------------------------------------------//
    //----------------------------Der Label der Preference----------------------------//
    //--------------------------------------------------------------------------------//
    private static final String PREFS_NAME = "com.our.package.DoorPrefs";


    //--------------------------------------------------------------------------------//
    //----------------------Die Preference der App------------------------------------//
    //--------------------------------------------------------------------------------//
    private static SharedPreferences settings;


    //--------------------------------------------------------------------------------//
    //--------------------------Einstellungs EDITOR der App---------------------------//
    //--------------------------------------------------------------------------------//
    private static SharedPreferences.Editor editor;


    //--------------------------------------------------------------------------------//
    //--------------------------------Konstruktor-------------------------------------//
    //--------------------------------------------------------------------------------//
    public DoorPrefs(Context context)
    {
        if (settings == null)
        {
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
       /*
        * Get a SharedPreferences editor instance.
        * SharedPreferences ensures that updates are atomic
        * and non-concurrent
        */
        editor = settings.edit();
    }

    /**
     * The prefix for flattened user keys
     */
    public static final String KEY_PREFIX = "com.our.package.KEY";


    //--------------------------------------------------------------------------------//
    //----------------Gibt einen Key zurück-------------------------------------------//
    //--------------------------------------------------------------------------------//
    private String getFieldKey(int id, String fieldKey)
    {
        return KEY_PREFIX + id + "_" + fieldKey;
    }

    private static final String KEY_DOORNAME = "com.our.package.KEY_DOORNAME";
    private static final String KEY_DOORPASSWORD = "com.our.package.KEY_DOORPASSWORD";
    private static final String KEY_DOORSTATUS = "com.our.package.KEY_DOORSTATUS";


    //--------------------------------------------------------------------------------//
    //----------------Speichert oder updated die Tür----------------------------------//
    //--------------------------------------------------------------------------------//
    public void setDoor(Door door)
    {
        if (door == null)
            return; // don't bother

        int id = door.getId();
        editor.putString(getFieldKey(id, KEY_DOORNAME), door.getDoorName());
        editor.putString(getFieldKey(id, KEY_DOORPASSWORD), door.getDoorPassword());
        editor.putString(getFieldKey(id, KEY_DOORSTATUS), door.getDoorStatus());

        editor.commit();
    }


    //--------------------------------------------------------------------------------//
    //----------------Bekommt die Tür-------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public Door getDoor(int id)
    {
        String name = settings.getString(getFieldKey(id, KEY_DOORNAME), ""); // default value
        String password = settings.getString(getFieldKey(id, KEY_DOORPASSWORD), ""); // default value
        String status = settings.getString(getFieldKey(id, KEY_DOORSTATUS), ""); // default value

        return new Door(id, name, password, status);
    }


    //--------------------------------------------------------------------------------//
    //----------------Löscht die Tür--------------------------------------------------//
    //--------------------------------------------------------------------------------//
    public void deleteDoor(Door door)
    {
        if (door == null)
            return;

        int id = door.getId();
        editor.remove(getFieldKey(id, KEY_DOORNAME));
        editor.remove(getFieldKey(id, KEY_DOORPASSWORD));
        editor.remove(getFieldKey(id, KEY_DOORSTATUS));

        editor.commit();
    }
}