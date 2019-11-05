package sorocaba.peteca.com.graphgenerator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Grafico extends View {

    // Variaveis relacionados com o gráfico sem modificações
    private Dimensoes dim;
    private Path pathEixos, pathGrade;
    private Paint paintEixos, paintGrade, paintTextos;

    // Variaveis disponiveis para o usuario fazer modificações
    private Paint paintSeriesExterno, paintSeriesInterno;
    private Path pathSeriesExterno, pathSeriesInterno;
    private SerieBarras series;
    private String nomeEixoX = "", nomeEixoY = "", nomeGrafico = "";
    private int numeroIntervalos = 4;
    private boolean graficoFechado, gradeVertical, gradeHorizontal;

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

        pathEixos = new Path();
        pathGrade = new Path();
        pathSeriesInterno = new Path();
        pathSeriesExterno = new Path();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paintFundo = new Paint();
        paintFundo.setStyle(Paint.Style.FILL);
        paintFundo.setColor(Color.WHITE);
        desenhaGrades();
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintFundo);
        canvas.drawPath(pathGrade, paintGrade);
        canvas.drawPath(pathEixos, paintEixos);
        if (series.getStyle() == Paint.Style.FILL) {
            canvas.drawPath(pathSeriesInterno, paintSeriesInterno);
        } else if (series.getStyle() == Paint.Style.FILL_AND_STROKE) {
            canvas.drawPath(pathSeriesInterno, paintSeriesInterno);
            canvas.drawPath(pathSeriesExterno, paintSeriesExterno);
        } else
            canvas.drawPath(pathSeriesExterno, paintSeriesExterno);
        if (series != null)
            atualizaValoresEixos(canvas);
        atualizaEixos(canvas);
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
            float tamanhoGrade = ((dim.getLarguraGrafico()/numeroIntervalos) * (series.isEmpty()? 1:0.8f));
            for (int linha = 1; linha < numeroIntervalos; linha++) {
                pathGrade.moveTo((dim.getLarguraEixoY() + tamanhoGrade * linha), dim.getAlturaEixoY());
                pathGrade.lineTo((dim.getLarguraEixoY() + tamanhoGrade * linha), dim.getAlturaEixoY() + dim.getAlturaGrafico());
            }
        } if (gradeHorizontal){
            float tamanhoGrade = ((dim.getAlturaGrafico()/numeroIntervalos) * (series.isEmpty()? 1:0.8f));
            for (int coluna = 1; coluna < (numeroIntervalos+1); coluna++) {
                pathGrade.moveTo(dim.getLarguraEixoY(), dim.getAlturaEixoY() + dim.getAlturaGrafico() - tamanhoGrade * coluna);
                pathGrade.lineTo(dim.getLarguraEixoY() + dim.getLarguraGrafico(), dim.getAlturaEixoY() + dim.getAlturaGrafico() - tamanhoGrade * coluna);
            }
        }
    }
    private void atualizaEixos(Canvas canvas) {
        paintTextos.setTextSize(dim.getAlturaEixoY()/1.4f);
        paintTextos.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(nomeEixoY, (dim.getLarguraEixoY()*0.1f), (dim.getAlturaEixoY() * 0.8f), paintTextos);

        paintTextos.setTextSize(dim.getAlturaEixoY()/1.2f);
        paintTextos.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(nomeGrafico, ((dim.getLarguraEixoY() + dim.getLarguraGrafico() + dim.getLarguraEixoX())/2), (dim.getAlturaEixoY() * 0.7f), paintTextos);

        paintTextos.setTextSize(dim.getAlturaEixoY()/1.4f);
        paintTextos.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(nomeEixoX, (dim.getLarguraEixoY() + dim.getLarguraGrafico() + dim.getLarguraEixoX()/2), (float)(dim.getAlturaEixoY() + dim.getAlturaGrafico() + dim.getAlturaEixoX() * 0.7), paintTextos);
    }
    private void atualizaValoresEixos(Canvas canvas) {
        Paint paintValoresEixos = new Paint();
        paintValoresEixos.setTextSize(dim.getAlturaEixoY()/1.8f);
        paintValoresEixos.setTextAlign(Paint.Align.CENTER);
        for (int ponto = 0; ponto < series.tamanho(); ponto++) {
            canvas.drawText(Double.toString(series.valor_x[ponto]),
                    (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1)),
                    (dim.getAlturaEixoY() + dim.getAlturaGrafico() + dim.getAlturaEixoX() * 0.7f), paintValoresEixos);
            canvas.drawText(Double.toString((series.valorMaximo()/numeroIntervalos) * ponto), (dim.getLarguraEixoY()/2),
                    dim.getAlturaEixoY() + dim.getAlturaGrafico() - (dim.getAlturaGrafico()/numeroIntervalos) * 0.8f * ponto, paintValoresEixos);
        }
    }
    public void add(SerieBarras seriesImportado) {
        if (seriesImportado != null) {
            series = seriesImportado;
            pathSeriesInterno.reset();

            for (int ponto = 0; ponto < series.tamanho(); ponto++) {
                RectF rectF = new RectF(
                        (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) - (dim.getLarguraGrafico() * 0.015f)),
                        (float) (dim.getAlturaEixoY() + dim.getAlturaGrafico() * (1.0 - ((series.valor_y[ponto] / series.valorMaximo()) * 0.8))),
                        (dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) + (dim.getLarguraGrafico() * 0.015f)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico()));
                pathSeriesInterno.addRect(rectF, Path.Direction.CCW);

                pathSeriesExterno.moveTo((dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) - (dim.getLarguraGrafico() * 0.015f)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico()));
                pathSeriesExterno.lineTo(dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) - (dim.getLarguraGrafico() * 0.015f),
                        (float) (dim.getAlturaEixoY() + dim.getAlturaGrafico() * (1.0 - ((series.valor_y[ponto] / series.valorMaximo()) * 0.8))));
                pathSeriesExterno.lineTo((dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) + (dim.getLarguraGrafico() * 0.015f)),
                        (float) (dim.getAlturaEixoY() + dim.getAlturaGrafico() * (1.0 - ((series.valor_y[ponto] / series.valorMaximo()) * 0.8))));
                pathSeriesExterno.lineTo((dim.getLarguraEixoY() + ((dim.getLarguraGrafico() * 0.95f)/(series.tamanho() + 1)) * (ponto + 1) + (dim.getLarguraGrafico() * 0.015f)),
                        (dim.getAlturaEixoY() + dim.getAlturaGrafico()));
            }



            paintSeriesInterno.setColor(series.getColor());
            paintSeriesInterno.setStyle(Paint.Style.FILL);
            paintSeriesExterno.setStrokeWidth(series.getStrokeWidth());
            paintSeriesExterno.setStyle(Paint.Style.STROKE);
            atualizar();
        }
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
