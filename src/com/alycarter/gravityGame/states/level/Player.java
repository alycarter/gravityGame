package com.alycarter.gravityGame.states.level;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.alycarter.crabClawEngine.Game;
import com.alycarter.gravityGame.states.level.entity.Entity;
import com.alycarter.gravityGame.states.level.entity.Ship;

public class Player {
	public Game game;
	public Level level;
	public Socket commandSocket;
	public ServerSocket server;
	public BufferedReader commandInput;
	public BufferedWriter commandOutput;
	public Socket entitySocket;
	public ObjectInputStream entityInput;
	public ObjectOutputStream entityOutput;
	
	public Player(Level level, Game game) {
		this.level=level;
		this.game=game;
		try {
			commandSocket = new Socket("192.168.1.2", 1234);
			entitySocket = new Socket("192.168.1.2", 1234);
			System.out.println("connected to server");
			commandOutput = new BufferedWriter(new PrintWriter(commandSocket.getOutputStream()));
			entityInput= new ObjectInputStream(entitySocket.getInputStream());
		} catch (Exception e) {
			System.out.println("no server found setting up new server");
			level.host=true;
			try {
				server = new ServerSocket(1234);
				commandSocket = server.accept();
				entitySocket = server.accept();
				commandInput=new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
				entityOutput= new ObjectOutputStream(entitySocket.getOutputStream());
				
				System.out.println("server has connected to a client");
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("failed to set up a server");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void update(){
		if(level.host){
			try {
				entityOutput.writeObject(level.entities);
				entityOutput.flush();
				System.out.println("sent master");
				while(commandInput.ready()){
					level.sendCommand(commandInput.read(),commandInput.read());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				Object o =entityInput.readObject();
				System.out.println("new master");
				level.entities=(ArrayList<Entity>) o;
				for (int i=0;i<level.entities.size();i++){
					level.entities.get(i).level=level;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		checkControls();
	}
	
	public void issueCommand(int id, int command){
		level.sendCommand(id,command);
		if(!level.host){
			try {
				commandOutput.write(id);
				commandOutput.write(command);
				commandOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void checkControls(){
		if(level.selectedShip !=null){
			if(game.getControls().isPressed(KeyEvent.VK_G)){
				issueCommand(level.selectedShip,Ship.TURNPROGRADE);
			}
			if(game.getControls().isPressed(KeyEvent.VK_H)){
				issueCommand(level.selectedShip,Ship.TURNRETROGRADE);
			}
			if(game.getControls().isPressed(KeyEvent.VK_SPACE)){
				issueCommand(level.selectedShip,Ship.BURN);
			}
			if(game.getControls().isTyped(KeyEvent.VK_SPACE)){
				issueCommand(level.selectedShip,Ship.STOPBURN);
			}
		}
	}

}
