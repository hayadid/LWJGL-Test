package Render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BlockModel {
    protected BlockFace faceFront, faceBack, faceLeft, faceRight, faceTop, faceBottom;
    protected ArrayList<Float> verticesToDraw;
    protected ArrayList<Integer> indicesToDraw;

    public BlockModel() {
        initFaces();
        verticesToDraw = new ArrayList<>();
        indicesToDraw = new ArrayList<>();
    }

    abstract void initFaces();

    public BlockFace getFaceFront() {
        return faceFront;
    }

    public BlockFace getFaceBack() {
        return faceBack;
    }

    public BlockFace getFaceLeft() {
        return faceLeft;
    }

    public BlockFace getFaceRight() {
        return faceRight;
    }

    public BlockFace getFaceTop() {
        return faceTop;
    }

    public BlockFace getFaceBottom() {
        return faceBottom;
    }

    public List<Float> getVerticesToDraw() {
        return verticesToDraw;
    }

    public List<Integer> getIndicesToDraw() {
        return indicesToDraw;
    }

    protected void updateFacesData(float x, float y, float z, int index) {
//        for (float f : faceFront.getVertices())
//            verticesToDraw.add(f);
//        for (float f : faceBack.getVertices())
//            verticesToDraw.add(f);
//        for (float f : faceLeft.getVertices())
//            verticesToDraw.add(f);
//        for (float f : faceRight.getVertices())
//            verticesToDraw.add(f);
//        for (float f : faceTop.getVertices())
//            verticesToDraw.add(f);
//        for (float f : faceBottom.getVertices())
//            verticesToDraw.add(f);
        for (float f :  BlockFace.VERTICES_ORDER)
            verticesToDraw.add(f);

        for (int i = 0; i < verticesToDraw.size(); i++) {
            if (i % 5 == 1) {
                verticesToDraw.set(i, verticesToDraw.get(i) + x);
            }

            if (i % 5 == 2) {
                verticesToDraw.set(i, verticesToDraw.get(i) + y);
            }

            if (i % 5 == 3) {
                verticesToDraw.set(i, verticesToDraw.get(i) + z);
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < BlockFace.INDICES_ORDER.length; j++) {
                indicesToDraw.add(BlockFace.INDICES_ORDER[j] + ((i + index) * BlockFace.INDICES_INCREASE));
            }
        }
    }
}
