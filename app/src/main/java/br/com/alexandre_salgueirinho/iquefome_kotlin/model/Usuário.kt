package br.com.alexandre_salgueirinho.iquefome_kotlin.model

class Usuário(
    val userId: String,
    val userNome: String,
    val userSobrenome: String,
    val userEmail: String,
    val userPassword: String,
    val userCelular: String,
    val userIndicado: String,
    val userUrlImagemPerfil: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}