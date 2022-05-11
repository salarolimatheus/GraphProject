# GraphProject
[![](https://jitpack.io/v/salarolimatheus/GraphProject.svg)](https://jitpack.io/#salarolimatheus/GraphProject)


        grafico = findViewById(R.id.grafico);
        grafico.setGraficoFechado(true);
        grafico.setGradeStatus(true);
        grafico.setNomeEixoX("Ordem");
        grafico.setNomeEixoY("Magnitude [A]");
        grafico.setNumeroGradesHorizontais(4);
        grafico.setGrandezaMagnitude("A");

        SerieBarras series = new SerieBarras();
        series.addBarra(1, 80.21513959303451, 1, 60);
        series.addBarra(2, 1.2014214,1, 120);
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
