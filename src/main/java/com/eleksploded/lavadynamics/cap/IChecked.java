package com.eleksploded.lavadynamics.cap;

public interface IChecked {
	boolean isChecked();
	
	boolean isVolcano();
	int getTop();
	
	void setVolcano(int top);
	void check();
	void removeCheck();
	
	int getCooldown();
	void setCooldown(int c);
}
