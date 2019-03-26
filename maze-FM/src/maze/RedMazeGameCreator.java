package maze;

import java.util.ArrayList;


public class RedMazeGameCreator extends MazeGameCreator {
	public Maze createMaze()
	{	
		Maze maze = new Maze();
		//System.out.println("The maze does not have any rooms yet!");
		Room firstRoom = makeRoom(0);
		Room secondRoom = makeRoom(1);
		Room thirdRoom = makeRoom(2);
		Wall wall = makeWall();
		Door firstDoor = makeDoor(firstRoom, secondRoom);
		Door secondDoor = makeDoor(firstRoom, thirdRoom);
		firstRoom.setSide(Direction.East, secondDoor);
		firstRoom.setSide(Direction.West, wall);
		firstRoom.setSide(Direction.South, wall);
		firstRoom.setSide(Direction.North, firstDoor);
		secondRoom.setSide(Direction.East, wall);
		secondRoom.setSide(Direction.West, wall);
		secondRoom.setSide(Direction.South, firstRoom);
		secondRoom.setSide(Direction.North, wall);
		thirdRoom.setSide(Direction.East, wall);
		thirdRoom.setSide(Direction.West, firstRoom);
		thirdRoom.setSide(Direction.South, wall);
		thirdRoom.setSide(Direction.North, wall);
		
		maze.addRoom(firstRoom);
		maze.addRoom(secondRoom);
		maze.addRoom(thirdRoom);
		maze.setCurrentRoom(0);
		return maze;
	}
	public Maze makeMaze(final String path, MazeGameCreator mgc) {
		Maze maze = new Maze();
		ArrayList<String> lines = getLines(path);
		ArrayList<Room> rooms = getRooms(lines, mgc);
		ArrayList<Door> doors = getDoors(lines, rooms, mgc);
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			constructRoom(currentLine, rooms, doors, mgc);
		}
		for (int i = 0; i < rooms.size(); i++) {
			maze.addRoom(rooms.get(i));
		}
		maze.setCurrentRoom(0);
		System.out.println("Load maze from file!");
		return maze;
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
