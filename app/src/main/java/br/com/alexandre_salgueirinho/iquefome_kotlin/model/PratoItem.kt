package br.com.alexandre_salgueirinho.iquefome_kotlin.model

import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_prato.view.*

class PratoItem (val prato:Pratos): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val precoPrato = "R\$ " + prato.pratoPreco
        viewHolder.itemView.textView_Nome_Prato.text = prato.pratoNome
        viewHolder.itemView.textView_Restaurante.text = prato.pratoRestaurante
        viewHolder.itemView.textView_Preco.text = precoPrato

        Picasso.get().load(prato.pratoUrlFoto).into(viewHolder.itemView.circleImage_Perfil)
    }

    override fun getLayout(): Int {
        return R.layout.item_prato
    }
}