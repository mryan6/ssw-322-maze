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
public class SimpleMazeGame
{
	/**
	 * Creates a small maze.
	 */
	public static Maze createMaze()
	{
		
		Maze maze = new Maze();
		//System.out.println("The maze does not have any rooms yet!");
		Room firstRoom = new Room(0);
		Room secondRoom = new Room(1);
		Wall wall = new Wall();
		Door firstDoor = new Door(firstRoom, secondRoom);
		firstRoom.setSide(Direction.East, wall);
		firstRoom.setSide(Direction.West, wall);
		firstRoom.setSide(Direction.South, wall);
		firstRoom.setSide(Direction.North, firstDoor);
		secondRoom.setSide(Direction.East, wall);
		secondRoom.setSide(Direction.West, wall);
		secondRoom.setSide(Direction.South, firstRoom);
		secondRoom.setSide(Direction.North, wall);
		maze.addRoom(firstRoom);
		maze.addRoom(secondRoom);
		maze.setCurrentRoom(0);
		return maze;
		

	}
	/*
	 * Will return an ArrayList where each element in the list is a line of the object.
	 */
	public static ArrayList<String> getLines(String path) {
		File file = new File(path);
		ArrayList<String> lines = new ArrayList<String>();
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
	public static ArrayList<Room> getRooms(ArrayList<String> lines) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			String[] roomInformation= currentLine.split(" ");
			if (roomInformation[0].equals("room")) {
				int roomNumber = Integer.parseInt(roomInformation[1]);
				Room newRoom = new Room(roomNumber);
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
	public static ArrayList<Door> getDoors(ArrayList<String> lines, ArrayList<Room> rooms) {
		ArrayList<Door> doors = new ArrayList<Door>();
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			String[] doorInformation= currentLine.split(" ");
			if (doorInformation[0].equals("door")) {
				int firstRoomNumber = Integer.parseInt(doorInformation[2]);
				int secondRoomNumber = Integer.parseInt(doorInformation[3]);
				Door door = new Door(rooms.get(firstRoomNumber), rooms.get(secondRoomNumber));
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
	public static void constructSide(Direction dir, Room room, ArrayList<Room> rooms, ArrayList<Door> doors, String roomObject) {
		if (roomObject.equals("wall")) {
			Wall wall = new Wall();
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
			constructSide(Direction.North, room, rooms, doors, roomInformation[2]);
			constructSide(Direction.South, room, rooms, doors, roomInformation[3]);
			constructSide(Direction.East, room, rooms, doors, roomInformation[4]);
			constructSide(Direction.West, room, rooms, doors, roomInformation[5]);
		}
	}

	public static  Maze loadMaze(final String path)
	{
		Maze maze = new Maze();
		ArrayList<String> lines = getLines(path);
		ArrayList<Room> rooms = getRooms(lines);
		ArrayList<Door> doors = getDoors(lines, rooms);
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

	public static void main(String[] args)
	{
		Maze maze;
		if (args.length == 0) {
			maze = createMaze();
		} else {
			maze = loadMaze(args[0]);
		}
	    MazeViewer viewer = new MazeViewer(maze);
	    viewer.run();
		
		
	}
}
