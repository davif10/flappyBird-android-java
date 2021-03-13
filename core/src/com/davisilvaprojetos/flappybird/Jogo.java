package com.davisilvaprojetos.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
    //Texturas
    private SpriteBatch batch;
    private Texture[] passaros;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;

    //Formas para colisão
    private ShapeRenderer shapeRenderer;
    private Circle circuloPassaro;
    private Rectangle retanguloCanoCima;
    private Rectangle retanguloCanoBaixo;

    //Atributos de configuracoes
    private float larguraDispositivo;
    private float alturaDispositivo;
    private float variacao = 0;
    private float gravidade = 0;
    private float posicaoInicialVerticalPassaro = 0;
    private float posicaoCanoHorizontal;
    private float posicaoCanoVertical;
    private float espacoEntreCanos;
    private Random random;
    private int pontos = 0;
    private boolean passouCano = false;
    private int estadoJogo = 0;

    //Exibição de textos
    BitmapFont textoPontuacao;
    BitmapFont textoReiniciar;
    BitmapFont textoMelhorPontuacao;

    @Override
    public void create() {
        inicializarTexturas();
        inicializarObjetos();
    }

    @Override
    public void render() {
        verificarEstadoJogo();
        baterAsasPassaro();
        validarPontos();
        desenharTexturas();
        detectarColisoes();

    }

    private void detectarColisoes() {
        circuloPassaro.set(
                50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());

        retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());

        boolean colidiuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
        boolean colidiuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);
        if (colidiuCanoCima || colidiuCanoBaixo) {
            estadoJogo = 2;
        }

        /*
        //Desenhando na tela as formas dos objetos para verificar se está no tamanho certo

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.circle(50 + passaros[0].getWidth() / 2,posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        //Cano Topo
        shapeRenderer.rect(
                posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight()
        );

        //Cano Baixo
        shapeRenderer.rect(
                posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight()
        );

        shapeRenderer.end();

         */

    }
    /*      EstadoJogo
     *0- Jogo Inicial, passaro parado
     * 1- Começa o jogo
     * 2- Colidiu
     */

    private void verificarEstadoJogo() {
        boolean toqueTela = Gdx.input.justTouched();

        if (estadoJogo == 0) {
            //Aplica evento de toque na tela
            if (toqueTela) {
                gravidade = -15;
                estadoJogo = 1;
            }

        } else if (estadoJogo == 1) {
            //Aplica evento de toque na tela
            if (toqueTela) {
                gravidade = -15;
            }

            //Movimentar cano
            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
            if (posicaoCanoHorizontal < -canoTopo.getWidth()) {
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVertical = random.nextInt(400) - 200;
                passouCano = false;
            }

            //Aplica gravidade no pássaro Ex: 500 -2 = 498
            if (posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


            gravidade++;

        } else if (estadoJogo == 2) {

        }

    }

    private void desenharTexturas() {
        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);
        batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
        batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
        textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2 - 50, alturaDispositivo - 100);

        if (estadoJogo == 2) {
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
            textoReiniciar.draw(batch,"Toque para reiniciar!",larguraDispositivo / 2 -140, alturaDispositivo / 2 - gameOver.getHeight() / 2);
            textoMelhorPontuacao.draw(batch, "Seu record é: 0 pontos",larguraDispositivo / 2 -140, alturaDispositivo / 2 - gameOver.getHeight());
        }

        batch.end();
    }

    public void validarPontos() {
        if (posicaoCanoHorizontal < 50 - passaros[0].getWidth()) {//Passou da posicao do passaro
            if (!passouCano) {
                pontos++;
                passouCano = true;
            }
        }
    }

    public void baterAsasPassaro() {
        variacao += Gdx.graphics.getDeltaTime() * 10;
        //Verifica variação para bater asas do pássaro
        if (variacao > 3)
            variacao = 0;
    }

    private void inicializarTexturas() {
        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");
        gameOver = new Texture("game_over.png");
    }

    private void inicializarObjetos() {
        batch = new SpriteBatch();
        random = new Random();
        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;
        posicaoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 300;

        //Configuracoes dos textos
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);

        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.GREEN);
        textoReiniciar.getData().setScale(2);

        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.RED);
        textoMelhorPontuacao.getData().setScale(2);

        //Formas geométricas para colisões
        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoBaixo = new Rectangle();
        retanguloCanoCima = new Rectangle();
    }

    @Override
    public void dispose() {

    }
}
