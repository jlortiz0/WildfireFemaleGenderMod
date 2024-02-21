package com.wildfire.render.armor;

import com.wildfire.api.IGenderArmor;
import com.wildfire.render.WildfireModelRenderer;
import net.minecraft.core.Direction;

import java.util.Arrays;

public record UnderlayGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int dx, int dy, int dz, int underU, int underV) implements IGenderArmor {
    public UnderlayGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int underU, int underV) {
        this(physicsResistance, tW, tH, texU, texV, leftSub, 4, 5, 3, underU, underV);
    }

    @Override
    public WildfireModelRenderer.ModelBox getLeftModel() {
        return new UnderlayModelBox(tW, tH, texU - leftSub, texV, -4, 0, 0, dx, dy, dz, 0, false);
    }

    @Override
    public WildfireModelRenderer.ModelBox getRightModel() {
        return new UnderlayModelBox(tW, tH, texU, texV, 0, 0, 0, dx, dy, dz, 0, false);
    }

    private class UnderlayModelBox extends WildfireModelRenderer.ModelBox {

        public UnderlayModelBox(int tW, int tH, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
            super(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror, 7);
        }

        @Override
        protected void initQuads(int tW, int tH, int texU, int texV, int dx, int dy, int dz, boolean mirror, boolean extra, WildfireModelRenderer.PositionTextureVertex vertex, WildfireModelRenderer.PositionTextureVertex vertex1, WildfireModelRenderer.PositionTextureVertex vertex2, WildfireModelRenderer.PositionTextureVertex vertex3, WildfireModelRenderer.PositionTextureVertex vertex4, WildfireModelRenderer.PositionTextureVertex vertex5, WildfireModelRenderer.PositionTextureVertex vertex6, WildfireModelRenderer.PositionTextureVertex vertex7) {
            this.quads[0] = new WildfireModelRenderer.TexturedQuad(texU + 4 + dx, texV + 4, texU + 4 + dx + 4, texV + 4 + dy, tW, tH, mirror, Direction.EAST,
                    vertex4, vertex, vertex1, vertex5);
            this.quads[1] = new WildfireModelRenderer.TexturedQuad(texU, texV + 4, texU + 4, texV + 4 + dy, tW, tH, mirror, Direction.WEST,
                    vertex7, vertex3, vertex6, vertex2);
            this.quads[2] = new WildfireModelRenderer.TexturedQuad(texU + 4, texV, texU + 4 + dx, texV + 4, tW, tH, mirror, Direction.DOWN,
                    vertex4, vertex3, vertex7, vertex);
            this.quads[3] = new WildfireModelRenderer.TexturedQuad(texU + 4, texV + 4 + 4, texU + 4 + dx, texV + 1 + 4 + dy, tW, tH - 1, mirror, Direction.UP,
                    vertex1, vertex2, vertex6, vertex5);
            this.quads[4] = new WildfireModelRenderer.TexturedQuad(texU + 4, texV + 4, texU + 4 + dx, texV + 4 + dy, tW, tH, mirror, Direction.NORTH,
                    vertex, vertex7, vertex2, vertex1);

            this.quads[5] = this.quads[3];
            this.quads[3] = new WildfireModelRenderer.TexturedQuad(underU, underV, underU + dx, underV + dy, tW, tH, mirror, Direction.UP,
                    vertex1, vertex2, vertex6, vertex5);
            this.quads[6] = this.quads[4];
            this.quads[4] = new WildfireModelRenderer.TexturedQuad(underU - 4, underV, underU - 4 + dx, underV + dy, tW, tH, mirror, Direction.NORTH,
                    vertex, vertex7, vertex2, vertex1);
        }
    }
}