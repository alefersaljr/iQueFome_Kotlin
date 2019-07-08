package br.com.alexandre_salgueirinho.iquefome_kotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Feedback(
    val feedbackId: String,
    val feedbackDescricao: String,
    val feedbackPratoNome: String,
    val feedbackRestauranteId: String
): Parcelable {
    constructor() : this("", "", "", "")
}