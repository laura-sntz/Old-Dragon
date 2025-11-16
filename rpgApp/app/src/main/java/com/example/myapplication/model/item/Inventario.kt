package com.example.myapplication.model.item

class Inventario {

    private val itens = mutableListOf<Item>()

    fun adicionarItem(item: Item) {
        itens.add(item)
        println("Você obteve: ${item.nome}")
    }

    fun removerItem(item: Item) {
        itens.remove(item)
    }

    fun listarItens(): List<Item> = itens.toList()

    fun usarItem(nomeItem: String): Item? {
        val item = itens.find { it.nome.equals(nomeItem, ignoreCase = true) }
        if (item != null) {
            itens.remove(item)
            println("🧪 Você usou: ${item.nome}")
        }
        return item
    }

    fun contem(nomeItem: String): Boolean {
        return itens.any { it.nome.equals(nomeItem, ignoreCase = true) }
    }
}
