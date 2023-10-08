/*
Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
Copyright (C) 2022 WildfireRomeo

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.wildfire.physics;

import com.wildfire.api.IGenderArmor;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BulgePhysics {

	private float bounceVel = 0;
	private float velocity = 0;
	private float wfg_bulge;
	private float wfg_preBounce;
	private float bounceRotVel = 0;
	private float rotVelocity = 0;
	private float wfg_bounceRotation;
	private float wfg_preBounceRotation;
	private float bounceVelX = 0;
	private float velocityX = 0;
	private float wfg_bulgeX;
	private float wfg_preBounceX;

	private boolean justSneaking = false, alreadySleeping = false;

	private float bulgeSize = 0, preBulgeSize = 0;

	private Vec3d prePos;
	private final GenderPlayer genderPlayer;
	public BulgePhysics(GenderPlayer genderPlayer) {
		this.genderPlayer = genderPlayer;
	}

	private int randomB = 1;
	private boolean alreadyFalling = false;
	public void update(PlayerEntity plr, IGenderArmor armor) {
		this.wfg_preBounce = this.wfg_bulge;
		this.wfg_preBounceX = this.wfg_bulgeX;
		this.wfg_preBounceRotation = this.wfg_bounceRotation;
		this.preBulgeSize = this.bulgeSize;

		if(this.prePos == null) {
			this.prePos = plr.getPos();
			return;
		}

		//float bulgeWeight = genderPlayer.getBulge().getSize() * 1.25f;
		float targetBulgeSize = genderPlayer.getBulge().getSize();

		float tightness = MathHelper.clamp(armor.tightness(), 0, 1);
		//Scale breast size by how tight the armor is, clamping at a max adjustment of shrinking by 0.15
		targetBulgeSize *= 1 - 0.15F * tightness;

		if(bulgeSize < targetBulgeSize) {
			bulgeSize += Math.abs(bulgeSize - targetBulgeSize) / 2f;
		} else {
			bulgeSize -= Math.abs(bulgeSize - targetBulgeSize) / 2f;
		}


		Vec3d motion = plr.getPos().subtract(this.prePos);
		this.prePos = plr.getPos();
		//System.out.println(motion);

		float bounceIntensity = (targetBulgeSize * 3f) * genderPlayer.getBounceMultiplier() / 8;
		float resistance = MathHelper.clamp(armor.physicsResistance(), 0, 1);
		//Adjust bounce intensity by physics resistance of the worn armor
		bounceIntensity *= 1 - resistance;

		bounceIntensity = bounceIntensity * WildfireHelper.randFloat(0.5f, 1.5f);
		if(plr.fallDistance > 0 && !alreadyFalling) {
			randomB = plr.world.random.nextBoolean() ? -1 : 1;
			alreadyFalling = true;
		}
		if(plr.fallDistance == 0) alreadyFalling = false;


		float targetBounce = (float) motion.y * bounceIntensity;
  		//targetBounce += bulgeWeight;
		//float horizVel = (float) Math.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.z, 2)) * (bounceIntensity);
		//float horizLocal = -horizVel * ((plr.getRotationYawHead()-plr.renderYawOffset)<0?-1:1);
		float targetRotVel = -((plr.bodyYaw - plr.prevBodyYaw) / 15f) * bounceIntensity;


		float f = 1.0F;
		if (true) {
			f = (float) plr.getVelocity().lengthSquared();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		targetBounce += MathHelper.cos(plr.limbAngle * 0.6662F + (float)Math.PI) * 0.5F * plr.limbDistance * 0.5F / f;
		//System.out.println(plr.rotationYaw);

		targetRotVel += (float) motion.y * bounceIntensity * randomB;


		if(plr.getPose() == EntityPose.CROUCHING && !this.justSneaking) {
			this.justSneaking = true;
			targetBounce += bounceIntensity;
		}
		if(plr.getPose() != EntityPose.CROUCHING && this.justSneaking) {
			this.justSneaking = false;
			targetBounce += bounceIntensity;
		}


		//button option for extra entities
		if(plr.getVehicle() != null) {
			if(plr.getVehicle() instanceof BoatEntity) {
				BoatEntity boat = (BoatEntity) plr.getVehicle();
				int rowTime = (int) boat.interpolatePaddlePhase(0, plr.limbAngle);
				int rowTime2 = (int) boat.interpolatePaddlePhase(1, plr.limbAngle);

				float rotationL = (float) MathHelper.clampedLerp(-(float)Math.PI / 3F, -0.2617994F, (double) ((MathHelper.sin(-rowTime2) + 1.0F) / 2.0F));
				float rotationR = (float) MathHelper.clampedLerp(-(float)Math.PI / 4F, (float)Math.PI / 4F, (double) ((MathHelper.sin(-rowTime + 1.0F) + 1.0F) / 2.0F));
				//System.out.println(rotationL + ", " + rotationR);
				if(rotationL < -1 || rotationR < -0.6f) {
					targetBounce = bounceIntensity / 3.25f;
				}
			}

			if(plr.getVehicle() instanceof MinecartEntity) {
				float speed = (float) plr.getVehicle().getVelocity().lengthSquared();
				if(Math.random() * speed < 0.5f && speed > 0.2f) {
					if(Math.random() > 0.5) {
						targetBounce = -bounceIntensity / 6f;
					} else {
						targetBounce = bounceIntensity / 6f;
					}
				}
				/*if(rotationL < -1 || rotationR < -1) {
					aPlr.targetBounce = bounceIntensity / 3.25f;
				}*/
			}
			if(plr.getVehicle() instanceof HorseBaseEntity) {
				float movement = (float) plr.getVehicle().getVelocity().lengthSquared();
				if(plr.getVehicle().age % clampMovement(movement) == 5 && movement > 0.1f) {
					targetBounce = bounceIntensity / 4f;
				}
				//horse
			}
			if(plr.getVehicle() instanceof PigEntity) {
				float movement = (float) plr.getVehicle().getVelocity().lengthSquared();
				//System.out.println(movement);
				if(plr.getVehicle().age % clampMovement(movement) == 5 && movement > 0.08f) {
					targetBounce = bounceIntensity / 4f;
				}
				//horse
			}
			if(plr.getVehicle() instanceof StriderEntity) {
				targetBounce += ((float) (plr.getVehicle().getMountedHeightOffset()*3f) - 4.5f) * bounceIntensity;
				//horse
			}
			//System.out.println("VEHICLE");
		}
		if(plr.getPose() == EntityPose.SLEEPING && !this.alreadySleeping) {
			targetBounce = bounceIntensity;
			this.alreadySleeping = true;
		}
		if(plr.getPose() != EntityPose.SLEEPING && this.alreadySleeping) {
			targetBounce = bounceIntensity;
			this.alreadySleeping = false;
		}
		/*if(plr.getPose() == EntityPose.SWIMMING) {
			//System.out.println(1 - plr.getRotationVec(tickDelta).getY());
			rotationMultiplier = 1 - (float) plr.getRotationVec(tickDelta).getY();
		}
		*/


		float percent =  genderPlayer.getFloppiness() / 4;
		float bounceAmount = 0.45f * (1f - percent) + 0.15f; //0.6f * percent - 0.15f;
		bounceAmount = MathHelper.clamp(bounceAmount, 0.15f, 0.6f);
		float delta = 2.25f - bounceAmount;
		//if(plr.isInWater()) delta = 0.75f - (1f * bounceAmount); //water resistance

		float distanceFromMin = Math.abs(bounceVel + 0.5f) * 0.5f;
		float distanceFromMax = Math.abs(bounceVel - 2.65f) * 0.5f;

		if(bounceVel < -0.5f) {
			targetBounce += distanceFromMin;
		}
		if(bounceVel > 2.5f) {
			targetBounce -= distanceFromMax;
		}
		if(targetBounce < -1.5f) targetBounce = -1.5f;
		if(targetBounce > 2.5f) targetBounce = 2.5f;
		if(targetRotVel < -25f) targetRotVel = -25f;
		if(targetRotVel > 25f) targetRotVel = 25f;

		this.velocity = MathHelper.lerp(bounceAmount, this.velocity, (targetBounce - this.bounceVel) * delta);
		//this.preY = MathHelper.lerp(0.5f, this.preY, (this.targetBounce - this.bounceVel) * 1.25f);
		this.bounceVel += this.velocity * percent * 1.1625f;

		//X
		float targetBounceX = 0;
		this.velocityX = MathHelper.lerp(bounceAmount, this.velocityX, (targetBounceX - this.bounceVelX) * delta);
		this.bounceVelX += this.velocityX * percent;

		this.rotVelocity = MathHelper.lerp(bounceAmount, this.rotVelocity, (targetRotVel - this.bounceRotVel) * delta);
		this.bounceRotVel += this.rotVelocity * percent;

		this.wfg_bounceRotation = this.bounceRotVel;
		this.wfg_bulgeX = this.bounceVelX;
		this.wfg_bulge = this.bounceVel;
	}

	public float getBulgeSize(float partialTicks) {
		return MathHelper.lerp(partialTicks, preBulgeSize, bulgeSize);
	}

	public float getPreBounceY() {
		return this.wfg_preBounce;
	}
	public float getBounceY() {
		return this.wfg_bulge;
	}

	public float getPreBounceX() {
		return this.wfg_preBounceX;
	}
	public float getBounceX() {
		return this.wfg_bulgeX;
	}

	public float getBounceRotation() {
		return this.wfg_bounceRotation;
	}
	public float getPreBounceRotation() {
		return this.wfg_preBounceRotation;
	}

	private int clampMovement(float movement) {
		int val = (int) (10 - movement*2f);
		if(val < 1) val = 1;
		return val;
	}
}
