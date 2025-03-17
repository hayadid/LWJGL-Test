package EngineTester;

import RenderEngine.DisplayManager;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;

import java.io.File;


public class MainGameLoop {
    public static void main(String[] args) {

        System.setProperty("org.lwjgl.librarypath",
                new File("./natives").getAbsolutePath());

        DisplayManager.createDisplay();

        while(!Display.isCloseRequested()) {
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }
}
