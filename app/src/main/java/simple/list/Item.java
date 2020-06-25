package simple.list;

public class Item
{
    String name;
    public boolean isChecked;

    public Item(String name)
    {
        this.name = name;
    }
    public String toString()
    {
        return name;
    }
	public void setChecked(boolean check)
	{
		
		this.isChecked=check;
	}
	public boolean check()
	{
		return this.isChecked;
	}
}

