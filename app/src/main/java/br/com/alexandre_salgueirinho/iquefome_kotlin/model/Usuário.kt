package br.com.alexandre_salgueirinho.iquefome_kotlin.model

class Usuário(
    val Cliente_Id: String,
    val Cliente_Nome: String,
    val Cliente_Sobrenome: String,
    val Cliente_Email: String,
    val Cliente_Password: String,
    val Cliente_Celular: String,
    val Cliente_Indicado: String,
    val Cliente_UrlImagemPerfil: String,
    val cliente_Pontos: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}