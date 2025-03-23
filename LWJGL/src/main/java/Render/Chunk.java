package Render;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    private BlockModel[][][] blocks;
//    private float[] opaqueVertices;
//    private float[] opaqueIndices;
//    private float[] transparentVertices;
//    private float[] transparentIndices;
    private List<Float> vertices;
    private List<Integer> indices;

    public Chunk() {
        this.blocks = new BlockModel[16][16][16];
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();

        int index = 0;

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    blocks[i][j][k] = new BlockGrass();

//                    blocks[i][j][k].updateFacesData(i, j, k, index);
//                    vertices.addAll(blocks[i][j][k].getVerticesToDraw());
//                    indices.addAll(blocks[i][j][k].getIndicesToDraw());
//                    index++;
                }
            }
        }
    }

    public float[] getVertices() {
        float[] verts = new float[vertices.size()];

        System.out.println("verts:");
        for (int i = 0; i < verts.length; i++) {
            verts[i] = vertices.get(i);
            System.out.println(verts[i] + ", ");
        }

        return verts;
    }

    public int[] getIndices() {
        int[] inds = new int[indices.size()];
        for (int i = 0; i < inds.length; i++) {
            inds[i] = indices.get(i);
        }

        return inds;
    }

    public BlockModel[][][] getBlocks() {
        return blocks;
    }
}
