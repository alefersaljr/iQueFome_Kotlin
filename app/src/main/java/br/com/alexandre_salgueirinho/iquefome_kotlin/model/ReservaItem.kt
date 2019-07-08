package br.com.alexandre_salgueirinho.iquefome_kotlin.model

import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_reserva.view.*

class ReservaItem (val reserva: Reservas): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.item_reserva_data_hora.text = reserva.reserva_Hora
        viewHolder.itemView.item_reserva_data_nome.text = reserva.restaurante_Nome
        viewHolder.itemView.item_reserva_data_prato.text = reserva.prato_Nome
    }

    override fun getLayout(): Int {
        return R.layout.item_reserva
    }
}