package com.example.myapplication.ui.theme.classe.subclasse

import androidx.annotation.DrawableRes
import com.example.myapplication.R

// importe suas subclasses do Model
import com.example.myapplication.model.classes.subclasses.Academico
import com.example.myapplication.model.classes.subclasses.Barbaro
import com.example.myapplication.model.classes.subclasses.Bardo
import com.example.myapplication.model.classes.subclasses.Druida
import com.example.myapplication.model.classes.subclasses.Ilusionista
import com.example.myapplication.model.classes.subclasses.Necromante
import com.example.myapplication.model.classes.subclasses.Paladino
import com.example.myapplication.model.classes.subclasses.Ranger

// Como nem sempre há uma base comum nas suas subclasses,
// uso extension em Any (funciona do mesmo jeito).
@DrawableRes
fun Any.subclasseImageRes(): Int = when (this) {
    is Academico   -> R.drawable.academico
    is Barbaro     -> R.drawable.barbaro
    is Bardo       -> R.drawable.bardo
    is Druida      -> R.drawable.druida
    is Ilusionista -> R.drawable.ilusionista
    is Necromante  -> R.drawable.necromante
    is Paladino    -> R.drawable.paladino
    is Ranger      -> R.drawable.ranger
    else           -> R.drawable.barbaro
}

fun Any.subclasseDisplayName(): String = when (this) {
    is Academico   -> "Acadêmico"
    is Barbaro     -> "Bárbaro"
    is Bardo       -> "Bardo"
    is Druida      -> "Druida"
    is Ilusionista -> "Ilusionista"
    is Necromante  -> "Necromante"
    is Paladino    -> "Paladino"
    is Ranger      -> "Ranger"
    else           -> "Subclasse"
}
