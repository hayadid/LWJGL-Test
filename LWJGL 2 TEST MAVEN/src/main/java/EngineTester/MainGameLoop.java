package EngineTester;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.RawModel;
import RenderEngine.Renderer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;

import java.io.File;


public class MainGameLoop {
    public static void main(String[] args) {

        System.setProperty("org.lwjgl.librarypath",
                new File("./natives").getAbsolutePath());

        DisplayManager.createDisplay();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        RawModel rectangle = loader.loadToVAO(vertices);


        while(!Display.isCloseRequested()) {
            renderer.prepare();
            renderer.render(rectangle);
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();

        DisplayManager.closeDisplay();
    }
}
