package br.com.alexandre_salgueirinho.iquefome_kotlin.model

class Pratos(
    val pratoId: String,
    val pratoNome: String,
    val pratoPreco: String,
    val pratoRestaurante: String,
    val pratoUrlFoto: String,
    val pratoDescricao: String,
    val pratoTipo: String,
    val pratoTipoComida: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}