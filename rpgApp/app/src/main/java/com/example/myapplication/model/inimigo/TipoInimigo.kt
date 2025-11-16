package com.example.myapplication.model.inimigo

enum class TipoInimigo(
    val nomeExibicao: String,
    val descricao: String
) {
    GOBLIN("Goblin", "Criatura pequena e traiçoeira das florestas."),
    ORC("Orc", "Guerreiro selvagem e brutal."),
    ESQUELETO("Esqueleto", "Mortos-vivos animados por magia negra."),
    BANDIDO("Bandido", "Humano traiçoeiro em busca de pilhagem."),
    DRAGAO("Dragão", "Criatura lendária e destrutiva, símbolo de poder."),
    CHEFAO("Senhor das Sombras", "Um inimigo poderoso, quase invencível.");
}
