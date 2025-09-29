package com.example.myapplication.ui.theme.classe

import androidx.annotation.DrawableRes
import com.example.myapplication.R
import com.example.myapplication.model.classes.implementacoes.Clerigo
import com.example.myapplication.model.classes.implementacoes.Guerreiro
import com.example.myapplication.model.classes.implementacoes.Ladrao
import com.example.myapplication.model.classes.implementacoes.Mago
import com.example.myapplication.model.classes.base.Classe as ModelClasse

@DrawableRes
fun ModelClasse.imageRes(): Int = when (this) {
    is Guerreiro -> R.drawable.guerreiro
    is Ladrao    -> R.drawable.ladrao
    is Mago      -> R.drawable.mago
    is Clerigo   -> R.drawable.clerigo
    else         -> R.drawable.guerreiro
}

fun ModelClasse.displayName(): String = when (this) {
    is Guerreiro -> "Guerreiro"
    is Ladrao    -> "Ladrão"
    is Mago      -> "Mago"
    is Clerigo   -> "Clérigo"
    else         -> ""
}