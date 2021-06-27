package com.eleksploded.lavadynamics.storage;

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
