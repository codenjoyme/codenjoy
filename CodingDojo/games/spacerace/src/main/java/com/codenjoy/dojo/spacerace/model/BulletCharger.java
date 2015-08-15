package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Tickable;

public class BulletCharger implements Tickable {
	private final int ticksToRecharge;
	private final int bulletsCount;
	private int timer = 0;
	private int bullets = 0;
	private boolean toRecharge = false;

	public BulletCharger(int ticksToRecharge, int bulletsCount) {
		this.ticksToRecharge = ticksToRecharge;
		this.bulletsCount = bulletsCount;
	}

	public void setToRecharge(boolean toRecharge) {
		this.toRecharge = toRecharge;
	}

	@Override
	public void tick() {
		if (toRecharge) {
			recharge();
			toRecharge = false;
		}
		// if (timer == 0) { // TODO доделать, если нам понадобится перезарядка
		// по времени
		// recharge();
		// }
		// timer--;
	}

	private void recharge() {
		timer = ticksToRecharge;
		bullets = bulletsCount;
	}

	public boolean canShoot() {
		boolean result = bullets > 0;
		if (result) {
			bullets--;
		}
		return result;
	}

	public int getTicksToRecharge() {
		return ticksToRecharge;
	}

	public int getBulletsCount() {
		return bulletsCount;
	}
}
