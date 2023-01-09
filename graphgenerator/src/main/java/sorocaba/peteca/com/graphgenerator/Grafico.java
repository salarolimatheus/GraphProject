package sorocaba.peteca.com.graphgenerator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Grafico extends View {
    private float alturaEixoX, alturaEixoY, alturaGrafico;
    private float larguraEixoX, larguraEixoY, larguraGrafico;

    private float larguraPaddingEsquerdo, larguraPaddingDireito;

    // Variaveis relacionados com o gráfico sem modificações
    private final List<Path> pathBarras = new ArrayList<>();
    private final List<BarraDimensoes> listBarraDimensoes  = new ArrayList<>();
    private Path pathEixos, pathGrade;
    private Paint paintEixos, paintGrade, paintTextos, paintBarras, paintBarraSelecionada;
    private Rect rect;

    // Variaveis disponiveis para o usuario fazer modificações
    private boolean graficoFechado = true, gradeStatus = false;
    private String nomeEixoX = "", nomeEixoY = "";
    public String stringOrdem = "", stringMagnitude = "", stringPhase = "", stringFreq = "";
    private String grandezaMagnitude = "", grandezaPhase = "º", grandezaFreq = "Hz";
    private int numeroGradesHorizontais = 4;

    private SerieBarras seriesBarras;

    //NAO REVISADOS
    private DecimalFormat df;
    private int indexSelecionado = 0;
    private int colorBarraSelecionada = Color.RED;
    private float tamanhoSerie, distanciaSerie;
    private String legenda1 = "Ordem", legenda2 = "f", legenda3 = "Magnitude", legenda4 = "Fase";

    private static class BarraDimensoes {
        public float left, top, right, bottom;
        public int id;

        BarraDimensoes(float left, float top, float right, float bottom, int id) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.id = id;
        }
    }

    public Grafico(Context context) {
        super(context);
        init();
    }
    public Grafico(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        paintEixos = new Paint();
        paintEixos.setStrokeWidth(5);
        paintEixos.setStyle(Paint.Style.STROKE);
        paintEixos.setColor(Color.BLACK);
        paintEixos.setAntiAlias(true);

        paintGrade = new Paint();
        paintGrade.setStrokeWidth(3);
        paintGrade.setStyle(Paint.Style.STROKE);
        paintGrade.setColor(Color.DKGRAY);
        paintGrade.setAntiAlias(true);

        paintTextos = new Paint();
        paintTextos.setAntiAlias(true);

        paintBarras = new Paint();
        paintBarras.setStyle(Paint.Style.FILL);
        paintBarras.setColor(Color.BLUE);

        paintBarraSelecionada = new Paint();
        paintBarraSelecionada.setStyle(Paint.Style.FILL);
        paintBarraSelecionada.setColor(Color.RED);

        pathEixos = new Path();
        pathGrade = new Path();

        df = new DecimalFormat("0.000");

        this.seriesBarras = null;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintEixos.setStyle(Paint.Style.FILL_AND_STROKE);
        paintEixos.setColor(Color.WHITE);
        canvas.drawRect(rect, paintEixos);
        paintEixos.setColor(Color.BLACK);
        paintEixos.setStyle(Paint.Style.STROKE);

        if(gradeStatus) {
            canvas.drawPath(pathGrade, paintGrade);
        }
        if (seriesBarras != null) {
            for (int index = 0; index < pathBarras.size(); index++) {
                Path path = this.pathBarras.get(index);
                if (index != indexSelecionado)
                    canvas.drawPath(path, paintBarras);
                else
                    canvas.drawPath(path, paintBarraSelecionada);
            }
        }

        canvas.drawPath(pathEixos, paintEixos);
        atualizaTextosEixos(canvas);
        atualizaLegenda(canvas);
   }

    public void ToqueNaTela(MotionEvent event) {
        for (int elemento = 0; elemento < listBarraDimensoes.size(); elemento++) {
            BarraDimensoes barraDimensoes = listBarraDimensoes.get(elemento);
            float largura = (barraDimensoes.right - barraDimensoes.left);
            if ((event.getX() >= barraDimensoes.left - largura) && (event.getX() < barraDimensoes.right + largura) &&
                    (event.getY() > barraDimensoes.top * 0.95f) && (event.getY() < (barraDimensoes.bottom + alturaEixoX))) {
                indexSelecionado = barraDimensoes.id;
            }
        }

        stringOrdem = String.valueOf(seriesBarras.valor_x[indexSelecionado]);
        df.applyPattern("0.000");
        stringMagnitude = df.format(seriesBarras.valor_y[indexSelecionado]);
        stringPhase = df.format(seriesBarras.arg1[indexSelecionado]);
        df.applyPattern("0.0");
        stringFreq = df.format(seriesBarras.arg2[indexSelecionado]);

        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        alturaEixoY = 0.05f * (float) height;
        alturaGrafico = 0.89f * (float) height;
        alturaEixoX = 0.06f * (float) height;

        larguraEixoY = 0.02f * (float) width;
        larguraGrafico = 0.96f * (float) width;
        larguraEixoX = 0.02f * (float) width;

        larguraPaddingEsquerdo = 0.05f * (float) width;
        larguraPaddingDireito = 0.85f * (float) width;

        rect = new Rect(0, 0, width, height);

        desenhaEixos();
        desenhaGrades();
        atualizaDados();

        if (seriesBarras != null) {
            indexSelecionado = 0;
            stringOrdem = String.valueOf(seriesBarras.valor_x[indexSelecionado]);
            stringMagnitude = df.format(seriesBarras.valor_y[indexSelecionado]);
            stringPhase = df.format(seriesBarras.arg2[indexSelecionado]);
            df.applyPattern("0.0");
            stringFreq = df.format(seriesBarras.arg1[indexSelecionado]);
        }

        invalidate();
        super.onSizeChanged(width, height, oldw, oldh);
    }

    public void addSerie(SerieBarras series) {
        this.seriesBarras = series;
        atualizaDados();
        indexSelecionado = 0;
        stringOrdem = String.valueOf(seriesBarras.valor_x[indexSelecionado]);
        stringMagnitude = df.format(seriesBarras.valor_y[indexSelecionado]);
        stringPhase = df.format(seriesBarras.arg2[indexSelecionado]);
        df.applyPattern("0.0");
        stringFreq = df.format(seriesBarras.arg1[indexSelecionado]);

        invalidate();
    }

    private void desenhaEixos() {
        pathEixos.reset();

        pathEixos.moveTo(larguraEixoY, alturaEixoY);
        pathEixos.lineTo(larguraEixoY, alturaEixoY + alturaGrafico);
        pathEixos.lineTo(larguraEixoY + larguraGrafico, alturaEixoY + alturaGrafico);

        if (graficoFechado) {
            pathEixos.lineTo(larguraEixoY + larguraGrafico, alturaEixoY);
            pathEixos.close();
        }
    }
    private void desenhaGrades() {
        pathGrade.reset();
        if (gradeStatus) {
            float tamanhoGrade = ((alturaGrafico/ numeroGradesHorizontais) * 0.85f);
            for (int coluna = 1; coluna < (numeroGradesHorizontais +1); coluna++) {
                pathGrade.moveTo(larguraEixoY, alturaEixoY + alturaGrafico - tamanhoGrade * coluna);
                pathGrade.lineTo(larguraEixoY + larguraGrafico, alturaEixoY + alturaGrafico - tamanhoGrade * coluna);
            }
        }
    }
    private void atualizaTextosEixos(Canvas canvas) {
        paintTextos.setTextSize(alturaEixoY/1.5f);
        paintTextos.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(nomeEixoY, (larguraEixoY), (alturaEixoY * 0.8f), paintTextos);

        paintTextos.setTextSize(alturaEixoX/2.0f);
        paintTextos.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(nomeEixoX, (larguraEixoY + larguraGrafico + larguraEixoX/2), (alturaEixoY + alturaGrafico + alturaEixoX/1.7f), paintTextos);

        paintTextos.setTextSize(alturaEixoX/2.0f);
        paintTextos.setTextAlign(Paint.Align.CENTER);

        if (seriesBarras != null) {
            for (int index = 0; index < seriesBarras.tamanho(); index++) {
                if (index == indexSelecionado) {
                    paintTextos.setColor(colorBarraSelecionada);
                    paintTextos.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    canvas.drawText(String.valueOf(seriesBarras.valor_x[index]),
                            larguraPaddingEsquerdo + ((2*index+1) * tamanhoSerie - distanciaSerie)/2,
                            alturaGrafico + alturaEixoY + (alturaEixoX/2), paintTextos);

                    paintTextos.setColor(Color.BLACK);
                    paintTextos.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                } else {
                    canvas.drawText(String.valueOf(seriesBarras.valor_x[index]),
                            larguraPaddingEsquerdo + ((2*index+1) * tamanhoSerie - distanciaSerie)/2,
                            alturaGrafico + alturaEixoY + (alturaEixoX/2), paintTextos);
                }

            }
        }
    }
    private void atualizaLegenda(Canvas canvas) {
        paintTextos.setTextSize(alturaGrafico*0.12f/4);
        paintTextos.setTextAlign(Paint.Align.LEFT);
        canvas.drawRect(new RectF(larguraEixoY + (larguraGrafico*2.0f/5), alturaEixoY,
                larguraEixoY + larguraGrafico, alturaEixoY + alturaGrafico*0.14f), paintEixos);
        canvas.drawText(legenda1 + " = " + stringOrdem, larguraEixoY + (larguraGrafico*2.1f/5), alturaEixoY + (alturaGrafico*0.14f*0.25f), paintTextos);
        canvas.drawText(legenda3 + " = " + stringMagnitude + grandezaMagnitude, larguraEixoY + (larguraGrafico*2.1f/5),alturaEixoY + (alturaGrafico*0.14f*0.6f), paintTextos);
        canvas.drawText(legenda4 + " = " + stringPhase + grandezaPhase, larguraEixoY + (larguraGrafico*2.1f/5), alturaEixoY + (alturaGrafico*0.14f*0.95f), paintTextos);
        paintTextos.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText( legenda2 + " = " + stringFreq + grandezaFreq, larguraEixoY + (larguraGrafico - larguraEixoX), alturaEixoY + (alturaGrafico*0.14f*0.25f), paintTextos);
    }
    private void atualizaDados() {
        pathBarras.clear();
        listBarraDimensoes.clear();
        if (seriesBarras == null)
            return;

        int quantidadeBarras = seriesBarras.tamanho();
        tamanhoSerie = (larguraPaddingDireito - larguraPaddingEsquerdo) / quantidadeBarras;
        distanciaSerie = 0.75f * tamanhoSerie;

        for (int barra = 0; barra < quantidadeBarras; barra++) {
            Path path = new Path();
            float a =(float) (alturaEixoY * 0.90f + alturaGrafico * (1.0 - ((seriesBarras.valor_y[barra] / seriesBarras.valorMaximo) * 0.95)));
            path.addRect(larguraPaddingEsquerdo + barra * tamanhoSerie,
                    a,
                    larguraPaddingEsquerdo + (barra+1) * tamanhoSerie - distanciaSerie,
                    alturaGrafico + alturaEixoY, Path.Direction.CW);
            listBarraDimensoes.add(new BarraDimensoes(larguraPaddingEsquerdo + barra * tamanhoSerie,
                    (float) (alturaEixoY + alturaGrafico * (1.0 - ((seriesBarras.valor_y[barra] / seriesBarras.valorMaximo) * 0.95))),
                    larguraPaddingEsquerdo + (barra + 1) * tamanhoSerie - distanciaSerie,
                    alturaGrafico + alturaEixoY + (alturaEixoX/2), barra));
            pathBarras.add(path);
        }
        indexSelecionado = 0;
    }

    //region getters et setters
    public void setGradeStatus(boolean gradeStatus) {
        this.gradeStatus = gradeStatus;
    }
    public void setGraficoFechado(boolean graficoFechado) {
        this.graficoFechado = graficoFechado;
    }
    public void setNomeEixoX(String nomeEixoX) {
        this.nomeEixoX = nomeEixoX;
    }
    public void setNomeEixoY(String nomeEixoY) {
        this.nomeEixoY = nomeEixoY;
    }
    public void setNumeroGradesHorizontais(int numeroGradesHorizontais) {
        this.numeroGradesHorizontais = numeroGradesHorizontais;
    }
    public void setBarraColor(int color) {
        paintBarras.setColor(color);
    }
    public void setBarraSelecionadaColor(int color) {
        paintBarraSelecionada.setColor(color);
        this.colorBarraSelecionada = color;
    }
    public void setGrandezaPhase(String grandezaPhase) {
        this.grandezaPhase = grandezaPhase;
    }
    public void setGrandezaFreq(String grandezaFreq) {
        this.grandezaFreq = grandezaFreq;
    }
    public void setGrandezaMagnitude(String grandezaMagnitude) {
        this.grandezaMagnitude = grandezaMagnitude;
    }
    public void setLegenda(String legenda1, String legenda2, String legenda3, String legenda4) {
        this.legenda1 = legenda1;
        this.legenda2 = legenda2;
        this.legenda3 = legenda3;
        this.legenda4 = legenda4;
    }
//endregion
}
