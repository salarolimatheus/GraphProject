package sorocaba.peteca.com.graphproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sorocaba.peteca.com.graphgenerator.Grafico;
import sorocaba.peteca.com.graphgenerator.SerieBarras;

public class MainActivity extends AppCompatActivity {
    Grafico grafico;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grafico = findViewById(R.id.grafico);

        grafico.setGraficoFechado(true);
        grafico.setGradeStatus(true);
        grafico.setNomeEixoX("Ordem");
        grafico.setNomeEixoY("Magnitude [A]");
        grafico.setNumeroGradesHorizontais(4);
        grafico.setGrandezaMagnitude("A");
        grafico.setLegenda("Ordem", "f", "Magnitude", "Fase");

        SerieBarras series = new SerieBarras();
        series.addBarra(1, 15.564, 1, 60);
        series.addBarra(2, 6.592,1, 120);
        series.addBarra(3, 0.000,1, 180);
        series.addBarra(4, 1.319,1, 240);
        series.addBarra(5, 0.006,1, 240);
        series.addBarra(6, 0.565,1, 300);
        series.addBarra(7, 0.004,1, 180);
        series.addBarra(8, 0.314,1, 180);

        grafico.addSerie(series);

        grafico.setOnTouchListener((view, motionEvent) -> {
            grafico.ToqueNaTela(motionEvent);
            return true;
        });
    }
}
