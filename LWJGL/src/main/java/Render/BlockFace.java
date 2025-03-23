package Render;

public class BlockFace {
    public enum FaceType {
        FRONT,
        BACK,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    // CHANGE LATER
    public static final int[] INDICES_ORDER = {
            0, 1, 2,
            2, 3, 0,
    };

    public static final float[] VERTICES_ORDER = {
            // Positions          // Texture Coords
            // Front face
            -0.5f, -0.5f,  0.5f,   0.0f, 0.0f,  // Bottom-left
            0.5f, -0.5f,  0.5f,   1.0f, 0.0f,  // Bottom-right
            0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
            -0.5f,  0.5f,  0.5f,   0.0f, 1.0f,  // Top-left
            // Back face
            -0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
            0.5f, -0.5f, -0.5f,   1.0f, 0.0f,  // Bottom-right
            0.5f,  0.5f, -0.5f,   1.0f, 1.0f,  // Top-right
            -0.5f,  0.5f, -0.5f,   0.0f, 1.0f,  // Top-left
            // Left face
            -0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
            -0.5f, -0.5f,  0.5f,   1.0f, 0.0f,  // Bottom-right
            -0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
            -0.5f,  0.5f, -0.5f,   0.0f, 1.0f,  // Top-left
            // Right face
            0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
            0.5f, -0.5f,  0.5f,   1.0f, 0.0f,  // Bottom-right
            0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
            0.5f,  0.5f, -0.5f,   0.0f, 1.0f,  // Top-left
            // Top face
            -0.5f,  0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
            0.5f,  0.5f, -0.5f,   1.0f, 0.0f,  // Bottom-right
            0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
            -0.5f,  0.5f,  0.5f,   0.0f, 1.0f,  // Top-left
            // Bottom face
            -0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
            0.5f, -0.5f, -0.5f,   1.0f, 0.0f,  // Bottom-right
            0.5f, -0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
            -0.5f, -0.5f,  0.5f,   0.0f, 1.0f   // Top-left
    };

    public static final int INDICES_INCREASE = 4;

    private FaceType face;
    private String textureSource;
    private boolean isTransparent;

    public BlockFace(FaceType face, String textureSource, boolean isTransparent) {
        this.face = face;
        this.textureSource = textureSource;
        this.isTransparent = isTransparent;
    }

    public FaceType getFace() {
        return face;
    }

    public String getTextureSource() {
        return textureSource;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public float[] getVertices() {
        switch (face) {
            case FRONT -> {
                return new float[]{
                        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,  // Bottom-left
                        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,  // Bottom-right
                        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,  // Top-right
                        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f}; // Top-left
            }
            case BACK -> {
                return new float[] {
                        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,  // Bottom-left
                        0.5f, -0.5f, -0.5f, 1.0f, 0.0f,  // Bottom-right
                        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,  // Top-right
                        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f // Top-left
                };
            }
            case LEFT -> {
                return new float[] {
                        -0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
                        -0.5f, -0.5f,  0.5f,   1.0f, 0.0f,  // Bottom-right
                        -0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
                        -0.5f,  0.5f, -0.5f,   0.0f, 1.0f  // Top-left
                };
            }
            case RIGHT -> {
                return new float[] {
                        0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
                        0.5f, -0.5f,  0.5f,   1.0f, 0.0f,  // Bottom-right
                        0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
                        0.5f,  0.5f, -0.5f,   0.0f, 1.0f  // Top-left
                };
            }
            case TOP -> {
                return new float[] {
                        -0.5f,  0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
                        0.5f,  0.5f, -0.5f,   1.0f, 0.0f,  // Bottom-right
                        0.5f,  0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
                        -0.5f,  0.5f,  0.5f,   0.0f, 1.0f  // Top-left
                };
            }
            case BOTTOM -> {
                return new float[] {
                        -0.5f, -0.5f, -0.5f,   0.0f, 0.0f,  // Bottom-left
                        0.5f, -0.5f, -0.5f,   1.0f, 0.0f,  // Bottom-right
                        0.5f, -0.5f,  0.5f,   1.0f, 1.0f,  // Top-right
                        -0.5f, -0.5f,  0.5f,   0.0f, 1.0f   // Top-left
                };
            }
            default -> {
                return null;
            }
        }
    }
}
