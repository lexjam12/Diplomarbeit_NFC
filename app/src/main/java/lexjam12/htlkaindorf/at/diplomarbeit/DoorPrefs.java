package lexjam12.htlkaindorf.at.diplomarbeit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lexjam12 on 03.02.17.
 */


class DoorPrefs
{

    /** This application's preferences label */
    private static final String PREFS_NAME = "com.our.package.DoorPrefs";

    /** This application's preferences */
    private static SharedPreferences settings;

    /** This application's settings editor*/
    private static SharedPreferences.Editor editor;

    /** Constructor takes an android.content.Context argument*/
    public DoorPrefs(Context context)
    {
        if(settings == null)
        {
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );
        }
       /*
        * Get a SharedPreferences editor instance.
        * SharedPreferences ensures that updates are atomic
        * and non-concurrent
        */
        editor = settings.edit();
    }

    /** The prefix for flattened user keys */
    public static final String KEY_PREFIX = "com.our.package.KEY";

    /** Method to return a unique key for any field belonging to a given object
     * @param id of the object
     * @param fieldKey of a particular field belonging to that object
     * @return key String uniquely identifying the object's field
     */
    private String getFieldKey(int id, String fieldKey)
    {
        return  KEY_PREFIX + id + "_" + fieldKey;
    }

    private static final String KEY_DOORNAME = "com.our.package.KEY_DOORNAME";
    private static final String KEY_DOORPASSWORD = "com.our.package.KEY_DOORPASSWORD";

    /** Store or Update */
    public void setDoor(Door door)
    {
        if(door == null)
            return; // don't bother

        int id = door.getId();
        editor.putString(getFieldKey(id, KEY_DOORNAME),door.getDoorName());
        editor.putString(getFieldKey(id, KEY_DOORPASSWORD),door.getDoorPassword());

        editor.commit();
    }

    /** Retrieve */
    public Door getDoor(int id)
    {
        String name = settings.getString(getFieldKey(id, KEY_DOORNAME), "" ); // default value
        String password = settings.getString(getFieldKey(id, KEY_DOORPASSWORD), ""); // default value

        return new Door(id, name, password);
    }

    /** Delete */
    public void deleteDoor(Door door)
    {
        if(door == null)
            return; // don't bother

        int id = door.getId();
        editor.remove( getFieldKey(id, KEY_DOORNAME));
        editor.remove( getFieldKey(id, KEY_DOORPASSWORD));

        editor.commit();
    }
}