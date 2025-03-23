import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DrawTextureRect {

    private long window;

    public void run() {
        init();
        loop();

        // Free resources and terminate GLFW
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Set error callback
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        // Create a window
        window = glfwCreateWindow(800, 600, "Textured Rectangle", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable vsync
        glfwShowWindow(window);

        // Initialize OpenGL
        GL.createCapabilities();

        // Fix textures being drawn upside down
        STBImage.stbi_set_flip_vertically_on_load(true);
    }

    private void loop() {
        // Vertex data with positions and texture coordinates
        float[] vertices = {
                // Positions          // Texture Coords
                -0.5f,  0.5f, 0.0f,   0.0f, 1.0f, // Top-left
                0.5f,  0.5f, 0.0f,   1.0f, 1.0f, // Top-right
                0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // Bottom-right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f  // Bottom-left
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
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

            out vec2 TexCoord;

            void main() {
                gl_Position = vec4(aPos, 1.0);
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

        // Main render loop
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            glUseProgram(shaderProgram);

            glBindTexture(GL_TEXTURE_2D, texture);

            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // Cleanup
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteProgram(shaderProgram);
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

    private int loadTexture(String path) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        // Set texture wrapping and filtering options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

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

    public static void main(String[] args) {
        new DrawTextureRect().run();
    }
}