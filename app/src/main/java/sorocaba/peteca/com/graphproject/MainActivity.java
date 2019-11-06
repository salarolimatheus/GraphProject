package sorocaba.peteca.com.graphproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import sorocaba.peteca.com.graphgenerator.Grafico;
import sorocaba.peteca.com.graphgenerator.SerieBarras;

public class MainActivity extends AppCompatActivity {
    Grafico grafico;
    SerieBarras series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grafico = findViewById(R.id.grafico);
        grafico.setGraficoFechado(true);
        grafico.setGradeHorizontal(true);
        grafico.setNomeEixoX("Ordem");
        grafico.setNomeEixoY("Magnitude");

        series = new SerieBarras();
        series.setStrokeWidth(5);
        series.setStyle(Paint.Style.FILL_AND_STROKE);
        series.setColor(Color.YELLOW);
        series.setColorStroke(Color.RED);
        series.setStrokeWidthSelecionado(7);
        series.setColorSelecionado(Color.BLUE);
        series.setColorSelecionadoStroke(Color.GREEN);
        series.addPonto(1, 80.21513959303451);
        series.addPonto(2, 1.2014214);
        series.addPonto(3, 30);
        series.addPonto(4, 1);
        series.addPonto(5, 50);

        grafico.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                grafico.ToqueNaTela(motionEvent);
                grafico.atualizar();
                grafico.stringOrdem = "";
                grafico.stringFreq = "";
                grafico.stringMagnitude = "";
                grafico.stringPhase = "";
                //grafico.indexSelecionado
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        grafico.iniciar();
        grafico.atualizar();
        grafico.removeSeries();
        grafico.addSeries(series);
    }
}
