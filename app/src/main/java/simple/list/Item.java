package simple.list;

public class Item
{
    public String name;
    public boolean isChecked;

    public Item(String name)
    {
        this.name = name;
    }
    public String toString()
    {
        return this.name;
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

