package com.example.myapplication.ui.theme.raca

import androidx.annotation.DrawableRes
import com.example.myapplication.R
import com.example.myapplication.model.personagens.racas.Raca as ModelRaca
import com.example.myapplication.model.personagens.racas.Humano
import com.example.myapplication.model.personagens.racas.Elfo
import com.example.myapplication.model.personagens.racas.Anao
import com.example.myapplication.model.personagens.racas.Halfling

@DrawableRes
fun ModelRaca.imageRes(): Int = when (this) {
    is Humano   -> R.drawable.humano
    is Elfo     -> R.drawable.elfo
    is Anao     -> R.drawable.anao
    is Halfling -> R.drawable.halfling
    else        -> R.drawable.humano   // default
}

fun ModelRaca.displayName(): String = when (this) {
    is Humano   -> "Humano"
    is Elfo     -> "Elfo"
    is Anao     -> "Anão"
    is Halfling -> "Halfling"
    else        -> ""
}

