package com.eleksploded.lavadynamics.cap;

import javax.annotation.Nullable;

public class CheckedHandler implements IChecked {
	boolean checked = false;
	boolean volcano = false;
	int top;
	int cooldown;
	
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

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(int c) {
		cooldown = c;
	}
}
