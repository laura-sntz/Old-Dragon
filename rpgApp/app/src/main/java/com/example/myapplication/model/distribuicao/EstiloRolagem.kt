package com.example.myapplication.model.distribuicao
enum class EstiloRolagem {
    CLASSICO,
    AVENTUREIRO,
    HEROICO
}

fun estiloParaMetodo(estilo: EstiloRolagem): MetodoDistribuicao = when (estilo) {
    EstiloRolagem.CLASSICO    -> EstiloClassico()
    EstiloRolagem.AVENTUREIRO -> EstiloAventureiro()
    EstiloRolagem.HEROICO     -> EstiloHeroico()
}
