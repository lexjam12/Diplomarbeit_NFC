package lexjam12.htlkaindorf.at.diplomarbeit;

/**
 * Created by lexjam12 on 27.01.17.
 */

public class Door
{

    private int id; // used for object storage
    private String doorName;
    private String doorPassword;

    public Door(int id, String userName, String doorPassword)
    {
        this.id = id;
        this.doorName = userName;
        this.doorPassword = doorPassword;
    }

    public int getId()
    {
        return id;
    }

    public String getDoorName()
    {
        return doorName;
    }

    public String getDoorPassword()
    {
        return doorPassword;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setDoorName(String userName)
    {
        this.doorName = userName;
    }

    public void setDoorPassword(String doorPassword)
    {
        this.doorPassword = doorPassword;
    }
}
