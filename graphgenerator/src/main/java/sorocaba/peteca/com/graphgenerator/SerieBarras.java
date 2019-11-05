package sorocaba.peteca.com.graphgenerator;

import android.graphics.Paint;

public class SerieBarras {
    public static final int tamanhoPontos = 15;
    public double[] valor_x, valor_y;
    private int numeroPontos;
    private ConfigPaint paint;
    private class ConfigPaint {
        int StrokeWidth = 2;
        Paint.Style Style = Paint.Style.FILL_AND_STROKE;
        int Color = android.graphics.Color.YELLOW;
    }

    public SerieBarras() {
        valor_x = new double[tamanhoPontos];
        valor_y = new double[tamanhoPontos];
        numeroPontos = 0;
        paint = new ConfigPaint();
    }

    public boolean addPonto(double x, double y) {
        if (numeroPontos < tamanhoPontos) {
            valor_x[numeroPontos] = x;
            valor_y[numeroPontos] = y;
            numeroPontos++;
            return true;
        } else {
            return false;
        }
    }
    public void reiniciar() {
        numeroPontos = 0;
    }
    public void limpar() {
        for (numeroPontos = 0; numeroPontos < tamanhoPontos; numeroPontos++){
            valor_x[numeroPontos] = 0;
            valor_y[numeroPontos] = 0;
        }
        reiniciar();
    }
    public int tamanho() {
        return numeroPontos;
    }

    public double valorMaximo() {
        double valorMaximo = valor_y[0];
        for (int ponto = 1; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] > valorMaximo)
                valorMaximo = valor_y[ponto];
        }
        return valorMaximo;
    }
    public double valorMinimo() {
        double valorMinimo = valor_y[0];
        for (int ponto = 1; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] < valorMinimo)
                valorMinimo = valor_y[ponto];
        }
        return valorMinimo;
    }

    public boolean valoresPositivos() {
        for (int ponto = 0; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] < 0)
                return false;
        }
        return true;
    }
    public boolean valoresNegativos() {
        for (int ponto = 0; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] > 0)
                return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return (numeroPontos == 0);
    }

    public int getStrokeWidth() {
        return paint.StrokeWidth;
    }
    public void setStrokeWidth(int strokeWidth) {
        paint.StrokeWidth = strokeWidth;
    }
    public Paint.Style getStyle() {
        return paint.Style;
    }
    public void setStyle(Paint.Style style) {
        paint.Style = style;
    }
    public int getColor() {
        return paint.Color;
    }
    public void setColor(int color) {
        paint.Color = color;
    }

}
