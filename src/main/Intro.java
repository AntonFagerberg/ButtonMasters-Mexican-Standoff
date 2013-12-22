package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Intro implements Entity {
    private Music music;
    private Texture mollerino, tombrero, alejandro, jenpeno, title, charc;
    private float x, y = 0f;
    private long startTime;
    private int part = 0;

    public Intro() {
        mollerino = new Texture(Gdx.files.internal("gfx/mollerino.png"));
        tombrero = new Texture(Gdx.files.internal("gfx/tombrero.png"));
        alejandro = new Texture(Gdx.files.internal("gfx/alejandro.png"));
        jenpeno = new Texture(Gdx.files.internal("gfx/jenpeno.png"));
        title = new Texture(Gdx.files.internal("gfx/title.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sfx/282154_Mexican_Gunfighting.mp3"));
        music.play();
        startTime = System.currentTimeMillis();
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void stop() {
        music.stop();
        music.dispose();
        mollerino.dispose();
        tombrero.dispose();
        alejandro.dispose();
        jenpeno.dispose();
        title.dispose();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (part == 0 && elapsedTime() > 1500 && elapsedTime() < 5000) {
            spriteBatch.draw(mollerino, x, y);
            y -= 0.6f;
        } else if (part == 0 && elapsedTime() > 1800) {
            part++;
            x = 0f;
            y = -180f;
        } else if (part == 1 && elapsedTime() < 8500) {
            spriteBatch.draw(tombrero, x, y);
            y += 0.6f;
        } else if (part == 1) {
            part++;
            x = y = 0f;
        } else if (part == 2 && elapsedTime() < 12000) {
            spriteBatch.draw(alejandro, x, y);
            y -= 0.6f;
        } else if (part == 2) {
            part++;
            x = 0f;
            y = -180f;
        } else if (part == 3 && elapsedTime() < 15500) {
            spriteBatch.draw(jenpeno, x, y);
            y += 0.6f;
        } else if (part == 3) {
            spriteBatch.draw(title, 0f, -175f);
        }
    }
}
