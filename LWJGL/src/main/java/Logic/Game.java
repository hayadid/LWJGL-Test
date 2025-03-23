package Logic;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    // Variable definitions:
    private long window;
    private final int WIDTH = 1024;
    private final int HEIGHT = 768;

    // Methods:
    public static void main(String[] args) {
        new Game().run();
    }

    public void run() {
        init();
        loop();
        cleanUp();
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
        window = glfwCreateWindow(WIDTH, HEIGHT, "Colored Rectangle", NULL, NULL);
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

    }

    private void handleInput() {

    }

    private void render() {

    }

    private void cleanUp() {
        // Free resources and terminate GLFW
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
