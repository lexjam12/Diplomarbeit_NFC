package lexjam12.htlkaindorf.at.diplomarbeit;

/**
 * Created by lexjam12 on 27.01.17.
 */


//--------------------------------------------------------------------------------//
//----------------DatenKlasse von der Tür-----------------------------------------//
//--------------------------------------------------------------------------------//
public class Door
{
    private int id; // used for object storage
    private String doorName;
    private String doorPassword;
    private String doorStatus;

    public Door(int id, String userName, String doorPassword, String doorStatus)
    {
        this.id = id;
        this.doorName = userName;
        this.doorPassword = doorPassword;
        this.doorStatus = doorStatus;
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

    public String getDoorStatus()
    {
        return doorStatus;
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

    public void setDoorStatus(String doorStatus)
    {
        this.doorStatus = doorStatus;
    }
}
