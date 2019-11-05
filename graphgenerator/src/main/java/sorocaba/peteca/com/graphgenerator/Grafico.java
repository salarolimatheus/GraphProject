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

import java.util.ArrayList;
import java.util.List;

public class Grafico extends View {

    // Variaveis relacionados com o gráfico sem modificações
    private Dimensoes dim;
    private Path pathEixos, pathGrade;
    private Paint paintEixos, paintGrade, paintTextos;

    // Variaveis disponiveis para o usuario fazer modificações
    private Paint paintSeriesExterno, paintSeriesInterno , paintSelecionadoExterno, paintSelecionadoInterno;
    private List<Barra> listBarras  = new ArrayList<>();
    public int indexSelecionado;
    private SerieBarras series;
    private String nomeEixoX = "", nomeEixoY = "", nomeGrafico = "";
    public String stringOrdem = "", stringMagnitude = "", stringPhase = "", stringFreq = "";
    private int numeroIntervalos = 4;
    private boolean graficoFechado, gradeVertical, gradeHorizontal;
    private class Barra {
        public float left, top, right, bottom;
        public int id;
        Barra(float left, float top, float right, float bottom, int id) {
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

        paintGrade = new Paint();
        paintGrade.setStrokeWidth(3);
        paintGrade.setStyle(Paint.Style.STROKE);
        paintGrade.setColor(Color.DKGRAY);

        paintTextos = new Paint();
        paintSeriesInterno = new Paint();
        paintSeriesExterno = new Paint();
        paintSelecionadoInterno = new Paint();
        paintSelecionadoExterno = new Paint();

        paintEixos.setAntiAlias(true);
        paintGrade.setAntiAlias(true);
        paintTextos.setAntiAlias(true);
        paintSeriesInterno.setAntiAlias(true);
        paintSeriesExterno.setAntiAlias(true);
        paintSelecionadoInterno.setAntiAlias(true);
        paintSelecionadoExterno.setAntiAlias(true);

        pathEixos = new Path();
        pathGrade = new Path();

        paintSeriesInterno.setTextAlign(Paint.Align.CENTER);
        paintSeriesInterno.setStyle(Paint.Style.FILL);
        paintSeriesExterno.setStyle(Paint.Style.STROKE);
        paintSelecionadoInterno.setStyle(Paint.Style.FILL);
        paintSelecionadoExterno.setStyle(Paint.Style.STROKE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paintFundo = new Paint();
        paintFundo.setStyle(Paint.Style.FILL);
        paintFundo.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintFundo);
        if (series != null) {
            desenhaGrades();
            canvas.drawPath(pathGrade, paintGrade);
            for (int index = 0; index < listBarras.size(); index++) {
                Path path = new Path();
                path.addRect(new RectF(listBarras.get(index).left, listBarras.get(index).top, listBarras.get(index).right, listBarras.get(index).bottom), Path.Direction.CCW);
                paintSeriesInterno.setColor(series.getColor());
                if (listBarras.get(index).id != indexSelecionado) {
                    if (series.getStyle() == Paint.Style.FILL_AND_STROKE) {
                        canvas.drawPath(path, paintSeriesInterno);
                        canvas.drawPath(path, paintSeriesExterno);
                    } else if (series.getStyle() == Paint.Style.FILL)
                        canvas.drawPath(path, paintSeriesInterno);
                    else
                        canvas.drawPath(path, paintSeriesExterno);
                    paintSeriesInterno.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                } else {
                    paintSeriesInterno.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    if (series.getStyle() == Paint.Style.FILL_AND_STROKE) {
                        canvas.drawPath(path, paintSelecionadoInterno);
                        canvas.drawPath(path, paintSelecionadoExterno);
                    } else if (series.getStyle() == Paint.Style.FILL)
                        canvas.drawPath(path, paintSelecionadoInterno);
                    else
                        canvas.drawPath(path, paintSelecionadoExterno);
                }
                paintSeriesInterno.setColor(Color.BLACK);
                canvas.drawText(Double.toString(series.valor_y[index]), (listBarras.get(index).left + listBarras.get(index).right)/2,
                        ((paintSeriesInterno.getTextSize())<(listBarras.get(index).bottom - listBarras.get(index).top))?
                        (1.015f * listBarras.get(index).top - 0.015f * listBarras.get(index).bottom):(1.5f*listBarras.get(index).top + (1-1.5f)* listBarras.get(index).bottom),
                        paintSeriesInterno);
            }
            atualizaEixos(canvas);
        }
        desenhaLegenda(canvas);
        canvas.drawPath(pathEixos, paintEixos);
   }

    public void iniciar() {
        dim = new Dimensoes(getHeight(), getWidth());
        desenhaEixos();
    }
    public void atualizar() {
        invalidate();
    }
    private void desenhaEixos() {
        pathEixos.reset();
        pathGrade.reset();

        pathEixos.moveTo(dim.getLarguraEixoY(), dim.getAlturaEixoY());
        pathEixos.lineTo(dim.getLarguraEixoY(), dim.getAlturaEixoY() + dim.getAlturaGrafico());
        pathEixos.lineTo(dim.getLarguraEixoY() + dim.getLarguraGrafico(), dim.getAlturaEixoY() + dim.getAlturaGrafico());

        if (graficoFechado) {
            pathEixos.lineTo(dim.getLarguraEixoY() + dim.getLarguraGrafico(), dim.getAlturaEixoY());
            pathEixos.close();
        }
    }
    private void desenhaGrades() {
        if (gradeVertical){
            float tamanhoGrade = ((dim.getLarguraGrafico()/numeroIntervalos) * 0.85f);
            for (int linha = 1; linha < numeroIntervalos; linha++) {
                pathGrade.moveTo((dim.getLarguraEixoY() + tamanhoGrade * linha), dim.getAlturaEixoY());
                pathGrade.lineTo((dim.getLarguraEixoY() + tamanhoGrade * linha), dim.getAlturaEixoY() + dim.getAlturaGrafico());
            }
        } if (gradeHorizontal){
            float tamanhoGrade = ((dim.getAlturaGrafico()/numeroIntervalos) * 0.85f);
            for (int coluna = 1; coluna < (numeroIntervalos+1); coluna++) {
                pathGrade.moveTo(dim.getLarguraEixoY(), dim.getAlturaEixoY() + dim.getAlturaGrafico() - tamanhoGrade * coluna);
                pathGrade.lineTo(dim.getLarguraEixoY() + dim.getLarguraGrafico(), dim.getAlturaEixoY() + dim.getAlturaGrafico() - tamanhoGrade * coluna);
            }
        }
    }

    private void desenhaLegenda(Canvas canvas) {
        Paint paintLegenda = new Paint();
        paintLegenda.setTextSize(dim.getAlturaGrafico()*0.1f/3);
        paintLegenda.setTextAlign(Paint.Align.LEFT);
        canvas.drawRect(new RectF(dim.getLarguraEixoY() + (dim.getLarguraGrafico()*2.0f/5), dim.getAlturaEixoY(),
                dim.getLarguraEixoY() + dim.getLarguraGrafico(), dim.getAlturaEixoY() + dim.getAlturaGrafico()*0.13f), paintEixos);
        canvas.drawText("Ordem = " + stringOrdem, dim.getLarguraEixoY() + (dim.getLarguraGrafico()*2.1f/5), dim.getAlturaEixoY() + (dim.getAlturaGrafico()*0.14f*0.25f), paintLegenda);
        canvas.drawText("Magnitude = " + stringMagnitude + " A", dim.getLarguraEixoY() + (dim.getLarguraGrafico()*2.1f/5),dim.getAlturaEixoY() + (dim.getAlturaGrafico()*0.14f*0.55f), paintLegenda);
        canvas.drawText("Phase = " + stringPhase + "º", dim.getLarguraEixoY() + (dim.getLarguraGrafico()*2.1f/5), dim.getAlturaEixoY() + (dim.getAlturaGrafico()*0.14f*0.85f), paintLegenda);
        paintLegenda.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText( "f = " + stringFreq + " Hz", dim.getLarguraEixoY() + (dim.getLarguraGrafico() - dim.getLarguraEixoX()), dim.getAlturaEixoY() + (dim.getAlturaGrafico()*0.14f*0.25f), paintLegenda);
    }
    private void atualizaEixos(Canvas canvas) {
        paintTextos.setTextSize(dim.getAlturaEixoY()/1.4f);
        paintTextos.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(nomeEixoY, (dim.getLarguraEixoY()*0.1f), (dim.getAlturaEixoY() * 0.8f), paintTextos);
        paintTextos.setTextSize(dim.getAlturaEixoY()/1.2f);
        paintTextos.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(nomeGrafico, ((dim.getLarguraEixoY() + dim.getLarguraGrafico() + dim.getLarguraEixoX())/2), (dim.getAlturaEixoY() * 0.75f), paintTextos);
        paintTextos.setTextSize(dim.getAlturaEixoY()/1.4f);
        paintTextos.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(nomeEixoX, (dim.getLarguraEixoY() + dim.getLarguraGrafico() + dim.getLarguraEixoX()/2), (float)(dim.getAlturaEixoY() + dim.getAlturaGrafico() + dim.getAlturaEixoX() * 0.7), paintTextos);

        Paint paintValoresEixos = new Paint();
        paintValoresEixos.setTextSize(dim.getAlturaEixoY()/1.8f);
        paintValoresEixos.setTextAlign(Paint.Align.CENTER);
        for (int ponto = 0; ponto < series.tamanho(); ponto++) {
            if (ponto == indexSelecionado) {
                paintValoresEixos.setColor(series.getColorSelecionado());
                paintValoresEixos.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText(Double.toString(series.valor_x[ponto]),
                        (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico() + dim.getAlturaEixoX() * 0.7f), paintValoresEixos);
                paintValoresEixos.setColor(Color.BLACK);
                paintValoresEixos.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            } else {
                canvas.drawText(Double.toString(series.valor_x[ponto]),
                        (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f) / (series.tamanho() + 1)) * (ponto + 1)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico() + dim.getAlturaEixoX() * 0.7f), paintValoresEixos);
            }

        }
        for (int ponto = 0; ponto < (numeroIntervalos + 1); ponto++) {
            canvas.drawText(Double.toString((series.valorMaximo()/numeroIntervalos) * ponto), (dim.getLarguraEixoY()/2),
                    dim.getAlturaEixoY() + dim.getAlturaGrafico() - (dim.getAlturaGrafico()/numeroIntervalos) * 0.85f * ponto, paintValoresEixos);
        }
    }
    public void addSeries(SerieBarras seriesImportado) {
        listBarras.clear();
        if (seriesImportado != null) {
            series = seriesImportado;
            for (int ponto = 0; ponto < series.tamanho(); ponto++) {
                listBarras.add(new Barra(dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) - (dim.getLarguraGrafico() * 0.015f),
                        (float) (dim.getAlturaEixoY() + dim.getAlturaGrafico() * (1.0 - ((series.valor_y[ponto] / series.valorMaximo()) * 0.85))),
                        (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) + (dim.getLarguraGrafico() * 0.015f)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico()), ponto));
            }
            paintSeriesInterno.setTextSize(dim.getLarguraGrafico() * 0.04f);
            paintSeriesExterno.setStrokeWidth(series.getStrokeWidth());
            paintSeriesExterno.setColor(series.getColorStroke());
            paintSelecionadoInterno.setColor(series.getColorSelecionado());
            paintSelecionadoExterno.setStrokeWidth(series.getStrokeWidthSelecionado());
            paintSelecionadoExterno.setColor(series.getColorSelecionadoStroke());
            atualizar();
        }
    }
    public void removeSeries() {
        if (series != null)
            series.limpar();
    }
    public void ToqueNaTela(MotionEvent event) {
        for (int elemento = 0; elemento < listBarras.size(); elemento++) {
            if ((event.getX() >= listBarras.get(elemento).left) && (event.getX() < listBarras.get(elemento).right) &&
                    (event.getY() > listBarras.get(elemento).top) && (event.getY() < (listBarras.get(elemento).bottom + dim.getAlturaEixoX()))) {
                indexSelecionado = listBarras.get(elemento).id;
            }
        }
        invalidate();
    }

    //region getters et setters
    public void setGraficoFechado(boolean graficoFechado) {
        this.graficoFechado = graficoFechado;
    }
    public void setGradeVertical(boolean gradeVertical) {
        this.gradeVertical = gradeVertical;
    }
    public void setGradeHorizontal(boolean gradeHorizontal) {
        this.gradeHorizontal = gradeHorizontal;
    }
    public void setNomeEixoX(String nomeEixoX) {
        this.nomeEixoX = nomeEixoX;
    }
    public void setNomeEixoY(String nomeEixoY) {
        this.nomeEixoY = nomeEixoY;
    }
    public void setNomeGrafico(String nomeGrafico) {
        this.nomeGrafico = nomeGrafico;
    }
    public void setNumeroIntervalos(int numeroIntervalos) {
        this.numeroIntervalos = numeroIntervalos;
    }
    //endregion
}
