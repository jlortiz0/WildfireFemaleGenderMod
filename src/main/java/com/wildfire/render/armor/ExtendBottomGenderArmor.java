package com.wildfire.render.armor;

import com.wildfire.api.IGenderArmor;
import com.wildfire.render.WildfireModelRenderer;
import net.minecraft.core.Direction;

public record ExtendBottomGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int dx, int dy, int dz, int extend) implements IGenderArmor {
    public ExtendBottomGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int extend) {
        this(physicsResistance, tW, tH, texU, texV, leftSub, 4, 5, 3, extend);
    }

    @Override
    public WildfireModelRenderer.ModelBox getLeftModel() {
        return new ExtendBottomModelBox(tW, tH, texU - leftSub, texV, -4, 0, 0, dx, dy, dz, 0, false);
    }

    @Override
    public WildfireModelRenderer.ModelBox getRightModel() {
        return new ExtendBottomModelBox(tW, tH, texU, texV, 0, 0, 0, dx, dy, dz, 0, false);
    }

    private class ExtendBottomModelBox extends WildfireModelRenderer.BreastModelBox {

        public ExtendBottomModelBox(int tW, int tH, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
            super(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror);
        }

        @Override
        protected void initQuads(int tW, int tH, int texU, int texV, int dx, int dy, int dz, boolean mirror, boolean extra, WildfireModelRenderer.PositionTextureVertex vertex, WildfireModelRenderer.PositionTextureVertex vertex1, WildfireModelRenderer.PositionTextureVertex vertex2, WildfireModelRenderer.PositionTextureVertex vertex3, WildfireModelRenderer.PositionTextureVertex vertex4, WildfireModelRenderer.PositionTextureVertex vertex5, WildfireModelRenderer.PositionTextureVertex vertex6, WildfireModelRenderer.PositionTextureVertex vertex7) {
            super.initQuads(tW, tH, texU, texV, dx, dy, dz, mirror, extra, vertex, vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7);
            this.quads[3] = new WildfireModelRenderer.TexturedQuad(texU + 4, texV + 4 + 4, texU + 4 + dx, texV + 8 + extend, tW, tH, mirror, Direction.UP,
                    vertex1, vertex2, vertex6, vertex5);
        }
    }
}