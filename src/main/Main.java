package main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main implements ApplicationListener {
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Game game;

    @Override
    public void create() {
        Texture.setEnforcePotImages(false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024f, 576f);
        spriteBatch = new SpriteBatch();
        game = new Game();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        game.render(spriteBatch);

        spriteBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = true;
        config.title = "BM Mexican Standoff";
        config.width = 1440;
        config.height = 900;
        new LwjglApplication(new Main(), config);
    }
}
