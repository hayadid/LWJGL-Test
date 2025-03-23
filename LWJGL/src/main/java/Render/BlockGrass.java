package Render;

import java.util.List;

public class BlockGrass extends BlockModel {


    @Override
    void initFaces() {
        faceFront = new BlockFace(BlockFace.FaceType.FRONT, "src/main/resources/image.png",
                false);
        faceBack = new BlockFace(BlockFace.FaceType.BACK, "src/main/resources/image.png",
                false);
        faceLeft = new BlockFace(BlockFace.FaceType.LEFT, "src/main/resources/image.png",
                false);
        faceRight = new BlockFace(BlockFace.FaceType.RIGHT, "src/main/resources/image.png",
                false);
        faceBottom = new BlockFace(BlockFace.FaceType.BOTTOM, "src/main/resources/image.png",
                false);
        faceTop = new BlockFace(BlockFace.FaceType.TOP, "src/main/resources/image.png",
                false);
    }
}
