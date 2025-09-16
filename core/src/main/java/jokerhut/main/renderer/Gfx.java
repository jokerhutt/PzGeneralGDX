package jokerhut.main.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Gfx {
    public static final Texture PIXEL;
    static {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        PIXEL = new Texture(pm);
        pm.dispose();
        PIXEL.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }
}
