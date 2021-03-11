package com.davisilvaprojetos.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture passaro;
	int contador = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
	}

	@Override
	public void render () {
		batch.begin();


		batch.draw(passaro, contador, 0);

		contador+=2;

		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
