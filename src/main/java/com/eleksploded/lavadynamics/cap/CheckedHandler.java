package com.eleksploded.lavadynamics.cap;

import javax.annotation.Nullable;

public class CheckedHandler implements IChecked {
	static CheckedHandler instance = new CheckedHandler();

	boolean checked = false;
	boolean volcano = false;
	int top;
	
	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public boolean isVolcano() {
		return volcano;
	}

	@Override
	@Nullable
	public int getTop() {
		return volcano ? top : null;
	}

	@Override
	public void setVolcano(int top) {
		volcano = true;
		this.top = top;
	}

	@Override
	public void check() {
		checked = true;
	}

	@Override
	public void removeCheck() {
		checked = false;
	}
}
