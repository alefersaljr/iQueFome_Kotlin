package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_prato.view.*

class  InicialAdapter: RecyclerView.Adapter<CustomViewModel>() {

    val pratosNomes = listOf ("Strogonoff", "Lasanha de Carne", "Empadão", "Pernil Assado", "Pizza de Frango com Catupiry", "Casquinha de Siri")
    val restaurantesNomes = listOf ("Quitanda", "Da Vovó", "Comidinha Caseira", "Restaurante Rústico", "Pizza Set", "Peixinho")
    val pratosPrecos = listOf ("R\$ 12,50", "R\$ 16,00", "R\$ 10,99", "R\$ 65,00", "R\$ 25,90", "R\$ 12.90")
    val restaurantesDistancia = listOf ("0,5 m", "3 m", "25 m", "1.000 m", "2,5 m", "10 m")

    override fun getItemCount(): Int {
        return pratosNomes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewModel {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPrato = layoutInflater.inflate(R.layout.item_prato, parent, false)
        return CustomViewModel(itemPrato)
    }

    override fun onBindViewHolder(holder: CustomViewModel, position: Int) {
        val pratoNome = pratosNomes.get(position)
        val restauranteNome = restaurantesNomes.get(position)
        val pratoPreco = pratosPrecos.get(position)
        val restauranteDistancia = restaurantesDistancia.get(position)

        holder.view.textView_Nome_Prato.text = pratoNome
        holder.view.textView_Restaurante.text = restauranteNome
        holder.view.textView_Preco.text = pratoPreco
        holder.view.textView_Distancia.text = restauranteDistancia
    }
}

class CustomViewModel(val view: View): RecyclerView.ViewHolder(view){  }