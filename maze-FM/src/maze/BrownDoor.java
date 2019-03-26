package maze;

import java.awt.Color;

public class BrownDoor extends Door {
	public BrownDoor(final Room r1, final Room r2) {
		super(r1,r2);
	}
	
	@Override
	public Color getColor()
	{
		Color BROWN = new Color(156, 93, 82);
		return BROWN;
	}
}
