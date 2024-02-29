package com.dev.jiujitsu.constants.enums;

import java.util.ArrayList;
import java.util.List;

public enum FaixasEnum {
    BRANCA {
        @Override
        public int getNumeroAulasGrau() {
            return 35;
        }

        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 150;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 10;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausRecebidos, int aulasRealizadas) {
            return this.getAulasProximaFaixa(grausRecebidos, aulasRealizadas);
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return this.getAulaParaPreta(grauAtual, aulasFeitas);
        }
    },
    AZUL {
        @Override
        public int getNumeroAulasGrau() {
            return 100;
        }

        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 500;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 100;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausRecebidos, int aulasRealizadas) {
            return this.getAulasProximaFaixa(grausRecebidos, aulasRealizadas);
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return this.getAulaParaPreta(grauAtual, aulasFeitas);
        }
    },
    ROXA {
        @Override
        public int getNumeroAulasGrau() {
            return 75;
        }


        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 375;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 75;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausRecebidos, int aulasRealizadas) {
            return this.getAulasProximaFaixa(grausRecebidos, aulasRealizadas);
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return this.getAulaParaPreta(grauAtual, aulasFeitas);
        }
    },
    MARROM {
        @Override
        public int getNumeroAulasGrau() {
            return 50;
        }


        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 250;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 50;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausRecebidos, int aulasRealizadas) {
            return this.getAulasProximaFaixa(grausRecebidos, aulasRealizadas);
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return this.getAulaParaPreta(grauAtual, aulasFeitas);
        }
    },

    PRETA {
        @Override
        public int getNumeroAulasGrau() {
            return 0;
        }

        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 0;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 0;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausUsuario, int aulasRealizadas) {
            return 0;
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return 0;
        }
    },

    CORAL {
        @Override
        public int getNumeroAulasGrau() {
            return 0;
        }

        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 0;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 0;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausUsuario, int aulasRealizadas) {
            return 0;
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return 0;
        }
    },

    VERMELHA {
        @Override
        public int getNumeroAulasGrau() {
            return 0;
        }

        @Override
        public int getNumeroTotalAulasGraduacao() {
            return 0;
        }

        @Override
        public int getNumeroAulasGraduacao() {
            return 0;
        }

        @Override
        public int getAulasRestantesParaProximaFaixa(int grausUsuario, int aulasRealizadas) {
            return 0;
        }

        @Override
        public int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas) {
            return 0;
        }
    };

    public abstract int getNumeroAulasGrau();

    public abstract int getNumeroTotalAulasGraduacao();

    public abstract int getNumeroAulasGraduacao();

    public abstract int getAulasRestantesParaProximaFaixa(int grausUsuario, int aulasRealizadas);

    public abstract int getTotalAulasFaltantesPreta(int grauAtual, int aulasFeitas);


    public List<FaixasEnum> buscaListaFaixasQueFaltam(String faixaAtual) {
        FaixasEnum faixaAtualEnum = FaixasEnum.valueOf(faixaAtual.toUpperCase());
        List<FaixasEnum> faixasRestantes = new ArrayList<>();
        boolean encontrouFaixaAtual = false;

        for (FaixasEnum faixa : FaixasEnum.values()) {
            if (faixa == faixaAtualEnum) {
                encontrouFaixaAtual = true;
            }

            if (encontrouFaixaAtual && !ehFaixaFinal(faixa) && !ehPreta(faixa) && faixa != faixaAtualEnum) {
                faixasRestantes.add(faixa);
            }
        }

        return faixasRestantes;
    }

    public int anosParaCoral(int grauAtual) {
        int totalAnos = 0;

        for (int grau = 0; grau < 6 - grauAtual; grau++) {
            totalAnos += anosGrau(grau);
        }

        return totalAnos;
    }

    public int anosParaVermelha(int grauAtual) {
        int totalAnos = 0;

        for (int grau = 0; grau < 9 - grauAtual; grau++) {
            totalAnos += anosGrau(grau);
        }

        return totalAnos;
    }


    private int anosGrau(int graus) {
        return switch (graus) {
            case 3, 4, 5 -> 5;
            case 6, 7 -> 7;
            case 8 -> 10;
            default -> 3;
        };
    }

    int getAulaParaPreta(int grauAtual, int aulasFeitas) {
        int totalAulasFaltantes = 0;

        List<FaixasEnum> faixasFaltantes = this.buscaListaFaixasQueFaltam(this.name());

        for (FaixasEnum faixa : faixasFaltantes) {
            totalAulasFaltantes += faixa.getNumeroTotalAulasGraduacao();
        }
        totalAulasFaltantes += this.getAulasRestantesParaProximaFaixa(grauAtual, aulasFeitas);

        return totalAulasFaltantes;
    }

    int getAulasProximaFaixa(int grausUsuario, int aulasRealizadas) {
        int aulasRestantes = this.getNumeroTotalAulasGraduacao() - aulasRealizadas;
        aulasRestantes -= grausUsuario * this.getNumeroAulasGrau();

        return Math.max(aulasRestantes, 0);
    }

    public static boolean ehFaixaFinal(FaixasEnum faixa) {
        return faixa == CORAL || faixa == VERMELHA;
    }

    public static boolean ehPreta(FaixasEnum faixa) {
        return faixa == PRETA;
    }


    public static FaixasEnum obterFaixaPorNome(String faixa){
        return FaixasEnum.valueOf(faixa.toUpperCase());

    }

}