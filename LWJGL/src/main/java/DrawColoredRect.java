import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DrawColoredRect {

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
        window = glfwCreateWindow(800, 600, "Colored Rectangle", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable vsync
        glfwShowWindow(window);

        // Initialize OpenGL
        GL.createCapabilities();
    }

    private void loop() {
        // Define vertex data for a rectangle
        float[] vertices = {
                // Positions         // Colors
                -0.5f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f, // Top-left (Red)
                0.5f,  0.5f, 0.0f,  0.0f, 1.0f, 0.0f, // Top-right (Green)
                0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f, // Bottom-right (Blue)
                -0.5f, -0.5f, 0.0f,  1.0f, 1.0f, 0.0f  // Bottom-left (Yellow)
        };

        // Define index data for the rectangle
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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Unbind the VAO (optional)
        glBindVertexArray(0);

        // Set up the shader program
        String vertexShaderSource = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec3 aColor;

            out vec3 ourColor;

            void main() {
                gl_Position = vec4(aPos, 1.0);
                ourColor = aColor;
            }
        """;

        String fragmentShaderSource = """
            #version 330 core
            in vec3 ourColor;
            out vec4 FragColor;

            void main() {
                FragColor = vec4(ourColor, 1.0);
            }
        """;

        int vertexShader = createShader(GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentShaderSource);

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader program linking failed: " + glGetProgramInfoLog(shaderProgram));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        // Main render loop
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            // Use the shader program
            glUseProgram(shaderProgram);

            // Bind the VAO and draw the rectangle
            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

            // Swap buffers and poll events
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

        // Check for compilation errors
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader compilation failed: " + glGetShaderInfoLog(shader));
        }

        return shader;
    }

    public static void main(String[] args) {
        new DrawColoredRect().run();
    }
}