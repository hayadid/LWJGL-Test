import Render.BlockModel;
import Render.Chunk;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryStack.stackPush;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class DrawChunk {

    private long window;
    private Chunk chunk;
    float cameraX = 0.0f;
    float cameraY = 0.0f;
    float cameraZ = 0.0f;
    double prevRotX;
    double prevRotY;

    public void run() {
        init();
        loop();

        // Free resources and terminate GLFW
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        window = glfwCreateWindow(800, 600, "Textured Cube", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        // Fix textures being drawn upside down
        STBImage.stbi_set_flip_vertically_on_load(true);

        chunk = new Chunk();
    }

    private void loop() {
        float[] vertices = {
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

        int[] indices = {
                // Front face
                0, 1, 2,
                2, 3, 0,
                // Back face
                4, 5, 6,
                6, 7, 4,
                // Left face
                8, 9, 10,
                10, 11, 8,
                // Right face
                12, 13, 14,
                14, 15, 12,
                // Top face
                16, 17, 18,
                18, 19, 16,
                // Bottom face
                20, 21, 22,
                22, 23, 20
        };

        // Generate and bind a VAO
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Generate and bind a VBO for vertices
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Generate and bind an EBO for indices
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Define the vertex attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        // Load and create the texture
        int texture = loadTexture("src/main/resources/image.png");

        // Set up the shader program
        String vertexShaderSource = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec2 aTexCoord;
            
            uniform mat4 model;      // Model matrix
            uniform mat4 projection; // Projection matrix
            
            out vec2 TexCoord;
            
            void main() {
                gl_Position = projection * model * vec4(aPos, 1.0); // Apply projection and model transformation
                TexCoord = aTexCoord;
            }
        """;

        String fragmentShaderSource = """
            #version 330 core
            in vec2 TexCoord;
            out vec4 FragColor;

            uniform sampler2D texture1;

            void main() {
                FragColor = texture(texture1, TexCoord);
            }
        """;

        int vertexShader = createShader(GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentShaderSource);

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader program linking failed: " + glGetProgramInfoLog(shaderProgram));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        float angleX = 0.0f;
        float angleY = 0.0f;

        int projectionLoc = glGetUniformLocation(shaderProgram, "projection");
        int modelLoc = glGetUniformLocation(shaderProgram, "model");

        // Set up the perspective projection matrix
        Matrix4f projection = new Matrix4f().perspective(
                (float) Math.toRadians(45.0),
                800.0f / 600.0f,
                0.1f,
                100.0f
        );

        // Allocate a FloatBuffer for the projection matrix
        FloatBuffer projectionBuffer = MemoryUtil.memAllocFloat(16);
        projection.get(projectionBuffer);
        glUniformMatrix4fv(projectionLoc, false, projectionBuffer);
        MemoryUtil.memFree(projectionBuffer);

        // Create the view matrix (move the camera back)
        Matrix4f view = new Matrix4f().translate(0.0f, 0.0f, 0.0f); // Move camera back 3 units

        // Enable blending
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Disable depth testing
        glEnable(GL_DEPTH_TEST);

        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(window, xPos, yPos);
        prevRotX = xPos[0];
        prevRotY = yPos[0];

        while (!glfwWindowShouldClose(window)) {
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
                cameraZ += 0.001f;
            }
            else if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
                cameraX += 0.001f;
            }
            else if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
                cameraZ += -0.001f;
            }
            else if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
                cameraX += -0.001f;
            }
            else if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
                cameraY += 0.001f;
            }
            else if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
                cameraY += -0.001f;
            }
            else {
                cameraX = 0; cameraZ = 0;
            }

            xPos = new double[1];
            yPos = new double[1];
            glfwGetCursorPos(window, xPos, yPos);
            view.rotateY((float)((prevRotX - xPos[0]) * -0.01));
            //view.rotateX((float)(prevRotY - yPos[0] * -0.00000000000001));
            prevRotX = xPos[0];
            prevRotY = yPos[0];
            view.translate(cameraX, cameraY, cameraZ);

            GL11.glClearColor(0.0f, 0.6f, 0.9f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glUseProgram(shaderProgram);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindVertexArray(vao);

            BlockModel[][][] blocks = chunk.getBlocks();

            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {

                        Matrix4f model1 = new Matrix4f()
                                .translate(0.0f + i, 0.0f + j, 0.0f + k) // Move the first cube to the left
                                .rotateX(angleX)
                                .rotateY(angleY);

                        FloatBuffer modelBuffer1 = MemoryUtil.memAllocFloat(16);
                        model1.get(modelBuffer1);
                        glUniformMatrix4fv(modelLoc, false, modelBuffer1);
                        MemoryUtil.memFree(modelBuffer1);

                        // Combine view and projection matrices
                        Matrix4f combined1 = projection.mul(view, new Matrix4f());
                        FloatBuffer combinedBuffer1 = MemoryUtil.memAllocFloat(16);
                        combined1.get(combinedBuffer1);
                        glUniformMatrix4fv(projectionLoc, false, combinedBuffer1);
                        MemoryUtil.memFree(combinedBuffer1);

                        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
                    }
                }
            }

            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

            glBindVertexArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

//        // re-enable depth-test
//        glEnable(GL_DEPTH_TEST);

        // disable blending
        //glDisable(GL_BLEND);


        // Cleanup
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteProgram(shaderProgram);
    }

    private int loadTexture(String path) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        // Set texture wrapping and filtering options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // Nearest neighbor for minification
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // Nearest neighbor for magnification


        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4); // Force RGBA
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
            }

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);
        }

        return texture;
    }


    private int createShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader compilation failed: " + glGetShaderInfoLog(shader));
        }

        return shader;
    }



    public static void main(String[] args) {
        new DrawChunk().run();
    }
}