package com.wildfire.render.armor;

import com.wildfire.api.IGenderArmor;
import com.wildfire.render.WildfireModelRenderer;
import net.minecraft.core.Direction;

public record OverlayGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, float olW, int underU, int underV) implements IGenderArmor {
    public OverlayGenderArmor(float physicsResistance, int tW, int tH, int texU, int texV, int leftSub, int underU, int underV) {
        this(physicsResistance, tW, tH, texU, texV, leftSub, 4 , underU, underV);
    }

    @Override
    public WildfireModelRenderer.ModelBox getLeftModel() {
        return new OverlayModelBox(tW, tH, texU - leftSub, texV, -4, 0, 0, 4, 5, 3, 0, true);
    }

    @Override
    public WildfireModelRenderer.ModelBox getRightModel() {
        return new OverlayModelBox(tW, tH, texU, texV, 0, 0, 0, 4, 5, 3, 0, false);
    }

    private class OverlayModelBox extends WildfireModelRenderer.ModelBox {

        public OverlayModelBox(int tW, int tH, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
            super(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, false, 7, mirror);
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

            if (olW != 4) {
                if (!extra) {
                    WildfireModelRenderer.PositionTextureVertex temp = vertex1;
                    vertex1 = new WildfireModelRenderer.PositionTextureVertex(vertex1.x() - olW, vertex2.y(), vertex2.z() - 0.25f, 0, 8);
                    vertex2 = new WildfireModelRenderer.PositionTextureVertex(temp.x(), temp.y() + 0.1f, vertex1.z(), 0, 8);
                    temp = vertex;
                    vertex = new WildfireModelRenderer.PositionTextureVertex(vertex.x() - olW, vertex7.y(), vertex7.z() - 0.25f, 0, 8);
                    vertex7 = temp;
                    temp = vertex5;
                    vertex5 = new WildfireModelRenderer.PositionTextureVertex(vertex5.x() - olW, vertex6.y() + 0.25f, vertex6.z(), 0, 8);
                    vertex6 = temp;
                } else {
                    vertex1 = new WildfireModelRenderer.PositionTextureVertex(vertex2.x() + olW, vertex1.y(), vertex1.z() - 0.25f, 0, 8);
                    vertex2 = new WildfireModelRenderer.PositionTextureVertex(vertex2.x(), vertex2.y() + 0.1f, vertex1.z(), 0, 8);
                    vertex = new WildfireModelRenderer.PositionTextureVertex(vertex7.x() + olW, vertex.y(), vertex.z() - 0.25f, 0, 8);
                    vertex5 = new WildfireModelRenderer.PositionTextureVertex(vertex6.x() + olW, vertex5.y() + 0.25f, vertex5.z(), 0, 8);
                }
            }
            float uU = olW == 4 ? (extra ? underU - leftSub : underU) : underU - 0.5f;
            this.quads[5] = new WildfireModelRenderer.TexturedQuad(uU, underV, uU + olW, underV + dy, tW, tH, mirror, Direction.NORTH,
                    vertex, vertex7, vertex2, vertex1);
            this.quads[6] = new WildfireModelRenderer.TexturedQuad(uU, underV + 4, uU + olW, underV + 4 + dy, tW, tH, mirror, Direction.UP,
                    vertex1, vertex2, vertex6, vertex5);
        }
    }
}