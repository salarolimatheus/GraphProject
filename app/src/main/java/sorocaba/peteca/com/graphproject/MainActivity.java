package sorocaba.peteca.com.graphproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sorocaba.peteca.com.graphgenerator.Grafico;
import sorocaba.peteca.com.graphgenerator.SerieBarras;

public class MainActivity extends AppCompatActivity {
    Grafico grafico;
    SerieBarras serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grafico = findViewById(R.id.grafico);
        grafico.setGraficoFechado(true);
        grafico.setGradeHorizontal(true);
        grafico.setGradeVertical(false);
        grafico.setNomeEixoX("Ordem");
        grafico.setNomeEixoY("Magnitude");
        grafico.setNomeGrafico("Nome do Gráfico");

        serie = new SerieBarras();
        serie.setStrokeWidth(5);
        serie.setStyle(Paint.Style.FILL_AND_STROKE);
        serie.setColor(Color.YELLOW);
        serie.addPonto(1.5, 1);
        serie.addPonto(2, 30);
        serie.addPonto(3, 1);
        serie.addPonto(4, 50);
        serie.addPonto(5, 80);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        grafico.iniciar();
        grafico.atualizar();
        grafico.removeSeries();
        grafico.addSeries(serie);
    }
}
