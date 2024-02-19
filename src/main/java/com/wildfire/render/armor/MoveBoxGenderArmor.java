package com.wildfire.render.armor;

import com.wildfire.api.IGenderArmor;
import com.wildfire.render.WildfireModelRenderer;

public record MoveBoxGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int dx, int dy, int dz) implements IGenderArmor {
    public MoveBoxGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub) {
        this(physicsResistance, tW, tH, texU, texV, leftSub, 4, 5, 3);
    }

    @Override
    public float physicsResistance() {
        return this.physicsResistance;
    }

    @Override
    public WildfireModelRenderer.ModelBox getLeftModel() {
        return new WildfireModelRenderer.BreastModelBox(tW, tH, texU - leftSub, texV, -4, 0, 0, dx, dy, dz, 0, false);
    }

    @Override
    public WildfireModelRenderer.ModelBox getRightModel() {
        return new WildfireModelRenderer.BreastModelBox(tW, tH, texU, texV, 0, 0, 0, dx, dy, dz, 0, false);
    }
}
