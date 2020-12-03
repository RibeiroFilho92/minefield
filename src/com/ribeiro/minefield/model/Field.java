package com.ribeiro.minefield.model;

import java.util.ArrayList;
import java.util.List;

import com.ribeiro.minefield.exceptions.ExplosionException;

public class Field {
	
	private Boolean open = false;
	private Boolean landMine = false;
	private Boolean marked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	
	private final Integer row;
	private final Integer column;
	
	public Field(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean openOrNot) {
		this.open = openOrNot;
	}

	public Boolean getMine() {
		return landMine;
	}

	public void setMine(Boolean landMine) {
		this.landMine = landMine;
	}

	public Boolean getMarked() {
		return marked;
	}

	public void setMarked(Boolean marked) {
		this.marked = marked;
	}

	public List<Field> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Field> neighbors) {
		this.neighbors = neighbors;
	}

	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}
	
	public boolean addNeighbor(Field neighbor) {
		boolean differentRow = this.row != neighbor.getRow();
		boolean differentColumn = this.column != neighbor.getColumn();
		boolean diagonal = differentRow && differentColumn;
		
		int rowDifferential = Math.abs(this.row - neighbor.getRow());
		int columnDifferential = Math.abs(this.column - neighbor.getColumn());
		int sumDifferential = rowDifferential + columnDifferential;
		
		if (sumDifferential == 1 && !diagonal) {
			this.neighbors.add(neighbor);
			return true;
		} else if (sumDifferential == 2 && diagonal) {
			this.neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}
	
	public void asMarkedOrNot() {
		if (!open) {
			marked = !marked;
		}
	}
	
	public boolean openUpField() {
		if (!open && !marked) {
			open = true;
			
			if (landMine) {
				throw new ExplosionException();
			}
			
			if (safeNeighborhood()) {
				this.neighbors.forEach(n -> n.openUpField());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean safeNeighborhood() {
		return this.neighbors.stream().noneMatch(n -> n.getMine());
	}
	
	public void putLandMine() {
		landMine = true;
	}
	
	public boolean hasBeenUsed() {
		boolean safe = !landMine && open;
		boolean protectedSquare = landMine && marked;
		return safe || protectedSquare;
	}
	
	public long landMineOnNeighborhod() {
		return neighbors.stream().filter(n -> n.getMine()).count();
	}
	
	public void reset() {
		open = false;
		landMine = false;
		marked = false;
	}
	
	public boolean isMined() {
		if (landMine) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		if (marked) {
			return "x";
		} else if (open && landMine) {
			return "*";
		} else if (open && landMineOnNeighborhod() > 0) {
			return Long.toString(landMineOnNeighborhod());
		} else if (open) {
			return " ";
		} else 
			return "?";
	}
}
