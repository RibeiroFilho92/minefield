package com.ribeiro.minefield;

import com.ribeiro.minefield.model.Board;
import com.ribeiro.minefield.view.BoardProjection;

public class Application {

	public static void main(String[] args) {
		Board board = new Board(6, 6, 6);
		
		new BoardProjection(board);

	}

}
