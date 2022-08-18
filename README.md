# Gráfico de Barras
![Badge Desenvolvido](http://img.shields.io/static/v1?label=STATUS&message=DESENVOLVIDO&color=GREEN&style=for-the-badge)

[![](https://jitpack.io/v/salarolimatheus/GraphProject.svg)](https://jitpack.io/#salarolimatheus/GraphProject)

Um gráfico de barras para Android focada para a visualização de componentes harmônicas de um sinal. Fique a vontade para alterar as coisas que forem necessárias para adequar ao seu projeto.

## 📱 Visualização

## 🛠️ Como funciona
### XML
Para adicionar o objeto no XML, basta utilizar a seguinte estrutura:
```
<sorocaba.peteca.com.graphgenerator.Grafico
android:layout_width="match_parent"
android:layout_height="match_parent"
android:id="@+id/grafico"
android:layout_centerHorizontal="true"/>
```

### Java
Configurando os eixos e o gráfico em geral:
```
grafico = findViewById(R.id.grafico);
grafico.setGraficoFechado(true);
grafico.setGradeStatus(true);
grafico.setNomeEixoX("Ordem");
grafico.setNomeEixoY("Magnitude [A]");
grafico.setNumeroGradesHorizontais(4);
grafico.setGrandezaMagnitude("A");
```

Manipulando os dados para serem apresentados no gráfico:
```
SerieBarras series = new SerieBarras();
series.addBarra(1, 80.21513959303451, 1, 60);
series.addBarra(2, 1.2014214,1, 120);
series.reiniciar();
series.addBarra(3, 30,1, 180);
series.addBarra(4, 1,1, 240);
series.addBarra(5, 1,1, 240);
series.addBarra(6, 50,1, 300);
series.addBarra(8, 30,1, 180);
series.addBarra(9, 1,1, 240);

grafico.addSerie(series);

grafico.setOnTouchListener(new View.OnTouchListener() {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
	grafico.ToqueNaTela(motionEvent);
	return true;
    }
});
```

#### Funções
