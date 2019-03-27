/*
 * SimpleMazeGame.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import maze.ui.MazeViewer;


/**
 * 
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class MazeGameCreator
{
	/**
	 * Creates a small maze.
	 */
	public static ArrayList<String> lines = new ArrayList<String>();
	public static MazeGameCreator mgc;
	public Maze createMaze()
	{
		
		Maze maze = new Maze();
		//System.out.println("The maze does not have any rooms yet!");
		Room firstRoom = mgc.makeRoom(0);
		Room secondRoom = mgc.makeRoom(1);
		Room thirdRoom = mgc.makeRoom(2);
		Wall wall = mgc.makeWall();
		Door firstDoor = mgc.makeDoor(firstRoom, secondRoom);
		Door secondDoor = mgc.makeDoor(firstRoom, thirdRoom);
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
	public Maze makeMaze(final String path) {
		Maze maze = new Maze();
		getLines(path);
		ArrayList<Room> rooms = createRooms();
		ArrayList<Door> doors = createDoors(rooms);
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			constructRoom(currentLine, rooms, doors);
		}
		for (int i = 0; i < rooms.size(); i++) {
			maze.addRoom(rooms.get(i));
		}
		maze.setCurrentRoom(0);
		System.out.println("Load maze from file!");
		return maze;
	}
	public  Wall makeWall() {
		Wall wall = new Wall();
		return wall;
	}
	public Door makeDoor(Room firstRoom, Room secondRoom) {
		Door door = new Door(firstRoom, secondRoom);
		return door;
	}
	public Room makeRoom(int roomNumber) {
		Room room = new Room(roomNumber);
		return room;
	}
	/*
	 * Will return an ArrayList where each element in the list is a line of the text file.
	 */
	public static ArrayList<String> getLines(String path) {
		File file = new File(path);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line; 
			while ((line = br.readLine()) != null) {
				lines.add(line);	
			} 
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("File was not found in the directory!");
		} catch (IOException e) {
			System.out.println("File was not found in the directory!");
		}
		return lines;
	}
	/*
	 * Will return an ArrayList of all Rooms in the text file
	 * We open the file using the path, then search through it line by line
	 * We can get each piece of information by splitting the line into individual strings separated by a space, or " "
	 * Each line will be converted into an array with each index corresponding to a different piece of information about the room
	 * According to the prompt, for a room: 
	 * Index 0 - MapSite type (ie. room)
	 * Index 1 - Room number
	 * Index 2 - North Object
	 * Index 3 - South Object
	 * Index 4 - East Object 
	 * Index 5 - West Object
	 * For this method, we will use Index 0 to ensure that it is a room that we are creating
	 * We will also use Index 1 to ensure that we create a room with the intended room number
	 * The other indexes will not be used in this method
	 */
	public static ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			String[] roomInformation= currentLine.split(" ");
			if (roomInformation[0].equals("room")) {
				int roomNumber = Integer.parseInt(roomInformation[1]);
				Room newRoom = mgc.makeRoom(roomNumber);
				rooms.add(newRoom);
			}
		}
		return rooms;
	}
	/*
	 * Will return an ArrayList of all Doors in the text file
	 * We open the file using the path, then search through it line by line
	 * We can get each piece of information by splitting the line into individual strings separated by a space, or " "
	 * Each line will be converted into an array with each index corresponding to a different piece of information about the Door
	 * According to the prompt, for a door: 
	 * Index 0 - MapSite type (ie. door)
	 * Index 1 - Door Number
	 * Index 2 - Room number of Adjacent Room #1
	 * Index 3 - Room number of Adjacent Room #2
	 * Index 4 - Door Status (ie. open or closed)
	 * For this method, we will use all of the indexes to correctly construct our list of doors
	 */
	public static ArrayList<Door> createDoors(ArrayList<Room> rooms) {
		ArrayList<Door> doors = new ArrayList<Door>();
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			String[] doorInformation= currentLine.split(" ");
			if (doorInformation[0].equals("door")) {
				int firstRoomNumber = Integer.parseInt(doorInformation[2]);
				int secondRoomNumber = Integer.parseInt(doorInformation[3]);
				Door door = mgc.makeDoor(rooms.get(firstRoomNumber), rooms.get(secondRoomNumber));
				if (doorInformation[4].equals("open")) {
					door.setOpen(true);
				} else {
					door.setOpen(false);
				}
				doors.add(door);
			}
		}
		return doors;
	}
	/*
	 * This method will surround each room with the appropriate MapSites (ie. room, door, wall)
	 * We need the direction, or which side of the room we are setting
	 * We need the room that we are modifying
	 * We also need the list of rooms just in case we need to place two rooms adjacent to each other
	 * We also need the list of doors so that we can join rooms together 
	 */
	public static void constructSide(Direction dir, Room room, ArrayList<Room> rooms, 
			ArrayList<Door> doors, String roomObject) {
		if (roomObject.equals("wall")) {
			Wall wall = mgc.makeWall();
			room.setSide(dir, wall);
		} else if (roomObject.contains("d")) {
			Scanner scanner = new Scanner(roomObject);
			Scanner in = scanner.useDelimiter("[^0-9]+");
			int doorNumber = in.nextInt();
			room.setSide(dir, doors.get(doorNumber));
			scanner.close();
		} else {
			Scanner scanner = new Scanner(roomObject);
			Scanner in = scanner.useDelimiter("[^0-9]+");
			int nextRoomNumber = in.nextInt();
			room.setSide(dir, rooms.get(nextRoomNumber));
			scanner.close();
		}
	}
	/*
	 * Will construct a room using the last four Indices of the roomInformation array
	 * Recall: 
	 * Index 2 - North Object
	 * Index 3 - South Object
	 * Index 4 - East Object 
	 * Index 5 - West Object
	 * We need to pass in the line of information so that we know what to add
	 * We need to pass in the list of rooms so that we can determine which room to modify
	 * We need to pass in the list of doors so that we can add doors if necessary 
	 */
	public static void constructRoom(String line, ArrayList<Room> rooms, ArrayList<Door> doors) {
		String[] roomInformation = line.split(" ");
		if (roomInformation[0].equals("room")) {
			int roomNumber = Integer.parseInt(roomInformation[1]);
			Room room = rooms.get(roomNumber);
			for (Direction side: Direction.values()) {
				int index = side.ordinal();
				constructSide(side, room, rooms, doors, roomInformation[index+2]);
			}
		}
	}
	
	/*
	 * Expecting between 0 and 2 arguments.
	 * Order of arguments will not matter
	 * If there are 0 arguments, construct default maze.
	 * If there is only one Argument, it will either be a file path, or a color.
	 * 		If it is a file path, construct a black and white maze according to the given file
	 * 		If it is a color, construct a default maze according to the given color 
	 * If there are two arguments one will be a file path and one will be a color:
	 * 		Construct maze of the given color according to the given file 
	 */
	public static void main(String[] args)
	{
		Maze maze;
		//MazeGameCreator mgc;
		if (args.length == 0) {
			mgc = new MazeGameCreator();
			maze = mgc.createMaze();
		} else if (args.length == 1) {
			if (args[0].toLowerCase().equals("red")) {
				//System.out.println("***************");
				mgc = new RedMazeGameCreator();
				maze = mgc.createMaze();
			} else if (args[0].toLowerCase().equals("blue")) {
				//System.out.println("***************");
				mgc = new BlueMazeGameCreator();
				maze = mgc.createMaze();
			} else {
				mgc = new MazeGameCreator();
				maze = mgc.makeMaze(args[0]);
			}
		} else {
			if (args[0].toLowerCase().equals("red")) {
				//"Color first, File path second"
				mgc = new RedMazeGameCreator();
				maze = mgc.makeMaze(args[1]);
			} else if (args[0].toLowerCase().equals("blue")) {
				//"Color first, File path second"
				mgc = new BlueMazeGameCreator();
				maze = mgc.makeMaze(args[1]);
			} else if (args[1].toLowerCase().equals("red")){
				//"File path first, color second"
				mgc = new RedMazeGameCreator();
				maze = mgc.makeMaze(args[0]);
			} else if (args[1].toLowerCase().equals("blue")) {
				//"File path first, color second"
				mgc = new BlueMazeGameCreator();
				maze = mgc.makeMaze(args[0]);
			} else {
				//Should only reach this point if the arguments do not contain a file path or color
				mgc = new MazeGameCreator();
				System.out.println("BAD ARGUMENTS FOUND!! Valid arguments are a color (red or blue) and a file path.");
				System.out.println("Building a default maze in its place");
				maze = mgc.createMaze();
			}
		}
	    MazeViewer viewer = new MazeViewer(maze);
	    viewer.run();
	}
}
