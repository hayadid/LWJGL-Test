package RenderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInAttribueList(0, positions);
        unbindVAO();
        // each vertex has three positions
        return new RawModel(vaoID, positions.length / 3);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttribueList(int attributeNumber, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        // static draw tells ogl that we are not planning to edit the data later
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer
                (attributeNumber, 3, GL11.GL_FLOAT,
                false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); // switches buffer from write state to read state
        return buffer;
    }

    public void cleanUp() {
        for (int vao: vaos
             ) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo: vbos
             ) {
            GL15.glDeleteBuffers(vbo);
        }
    }
}
