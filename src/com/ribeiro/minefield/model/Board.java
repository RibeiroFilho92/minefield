package com.ribeiro.minefield.model;

import java.util.ArrayList;
import java.util.List;

import com.ribeiro.minefield.exceptions.ExplosionException;

public class Board {

	private Integer rows;
	private Integer columns;
	private Integer landMines;
	
	private List<Field> fields = new ArrayList<>();
	
	public Board(Integer rows, Integer columns, Integer landMines) {
		this.rows = rows;
		this.columns = columns;
		this.landMines = landMines;
		
		createFields();
		neighborsAssociation();
		landMineRandomizing();
	}

	public void createFields() {
		for (int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				fields.add(new Field(i, j));
			}
		}
	}
	
	public void neighborsAssociation() {
		for (Field f : fields) {
			for (Field fi : fields) {
				f.addNeighbor(fi);
			}
		}
	}

	private void landMineRandomizing() {
		long mineLandsActivates = 0;
		do {
			int randomizing = (int) (Math.random() * fields.size());
			fields.get(randomizing).putLandMine();
			mineLandsActivates = fields.stream().filter(f -> f.getMine()).count();
		} while (mineLandsActivates < this.landMines);
	}
	
	public boolean hasBeenUsed() {
		return fields.stream().allMatch(f -> f.hasBeenUsed());
	}

	public void reset() {
		fields.stream().forEach(f -> f.reset());
		this.landMineRandomizing();
	}
	
	public void openUp(int row, int column) {
		try {
			fields.parallelStream()
			.filter(f -> f.getRow() == row && f.getColumn() == column)
			.findFirst().ifPresent(f -> f.openUpField());
		} catch (ExplosionException e) {
			fields.forEach(f -> f.setOpen(true));
			throw e;
		}
	}
	
	public void puttingASymbol(int row, int column) {
		fields.parallelStream().filter(f -> f.getRow() == row && f.getColumn() == column)
		.findFirst().ifPresent(f -> f.asMarkedOrNot());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  ");
		for (int column = 0; column < columns; column++) {
			sb.append(" ");
			sb.append(column);
			sb.append(" ");
		}
			sb.append("\n");
			
		int aux = 0;
		
		for (int line = 0; line < rows; line++) {
			sb.append(line);
			sb.append(" ");
			for(int column = 0; column < columns; column++) {
				sb.append(" ");
				sb.append(fields.get(aux));
				sb.append(" ");
				aux++;
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
