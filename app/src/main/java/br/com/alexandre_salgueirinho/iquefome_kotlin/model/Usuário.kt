package br.com.alexandre_salgueirinho.iquefome_kotlin.model

class Usuário(
    val cliente_Id: String,
    val cliente_Nome: String,
    val cliente_Sobrenome: String,
    val cliente_Email: String,
    val cliente_Password: String,
    val cliente_Celular: String,
    val cliente_Indicado: String,
    val cliente_UrlImagemPerfil: String,
    val cliente_Pontos: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}