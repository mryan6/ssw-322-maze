package maze;


//import java.util.ArrayList;


public class BlueMazeGameCreator extends MazeGameCreator {
	public Maze createMaze() {
		return super.createMaze();
	}
	public Maze makeMaze(final String path) {
		return super.makeMaze(path);
	}
	public Wall makeWall() {
		Wall wall = new DarkBlueWall();
		return wall;
	}
	public Door makeDoor(Room firstRoom, Room secondRoom) {
		Door door = new BrownDoor(firstRoom, secondRoom);
		return door;
	}
	public Room makeRoom(int roomNumber) {
		Room room = new LightBlueRoom(roomNumber);
		return room;
	}

	

}
