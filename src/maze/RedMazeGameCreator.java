package maze;

//import java.util.ArrayList;


public class RedMazeGameCreator extends MazeGameCreator {
	public Maze createMaze() {
		return super.createMaze();
	}
	public Maze makeMaze(final String path) {
		return super.makeMaze(path);
	}
	public Wall makeWall() {
		Wall wall = new RedWall();
		return wall;
	}
	public Door makeDoor(Room firstRoom, Room secondRoom) {
		Door door = new GreenDoor(firstRoom, secondRoom);
		return door;
	}
	public Room makeRoom(int roomNumber) {
		Room room = new PinkRoom(roomNumber);
		return room;
	}
	


}
