package com.ribeiro.minefield.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import com.ribeiro.minefield.exceptions.ExitException;
import com.ribeiro.minefield.exceptions.ExplosionException;
import com.ribeiro.minefield.model.Board;

public class BoardProjection {

	private Board board;
	private Scanner sc = new Scanner(System.in);
	
	public BoardProjection(Board board) {
		this.board = board;
		playGame();
	}
	
	public void playGame() {
		try {
			boolean continuing = true;
			
			while(continuing) {
				gameLifeCycle();
				System.out.println("Do you want to play again? (Y/n) ");
				String answer = sc.nextLine();
				
				if ("n".equalsIgnoreCase(answer)) {
					continuing = false;
				} else {
					board.reset();
				}
			}
			
		} catch (ExitException e) {
			System.out.println("Bye");
		} finally {
			sc.close();
		}
	}
	
	public void gameLifeCycle() {

		try {
			while (!board.hasBeenUsed()) {
				System.out.println(board);
				
				String call = capturatorOfStrings("Choose the position (x, y): ");
				
				Iterator<Integer> xy = Arrays.stream(call.split(",")).map(e -> Integer.parseInt(e.trim())).iterator();
				
				call = capturatorOfStrings("1 to open or 2 to (un)mark ");
				
				if ("1".equals(call)) {
					board.openUp(xy.next(), xy.next());
				} else if ("2".equals(call)) {
					board.puttingASymbol(xy.next(), xy.next());
				}
			}
			
			System.out.println(board);
			System.out.println("You won!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("You lost!");
		}
	}
	
	public String capturatorOfStrings(String string) {
		System.out.print(string);
		String call = sc.nextLine();
		
		if ("exit".equalsIgnoreCase(call)) {
			throw new ExitException();
		}
		
		return call;
	}
}
