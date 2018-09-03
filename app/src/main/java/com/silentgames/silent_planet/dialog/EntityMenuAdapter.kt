package com.silentgames.silent_planet.dialog

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.CellProperties
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.SpaceShip

class EntityMenuAdapter(
        entityList: MutableList<EntityType>,
        currentCell: CellType,
        val onClick: (CellProperties) -> Unit
) : RecyclerView.Adapter<EntityMenuAdapter.EntityMenu>() {

    private val convertedList: MutableList<CellProperties> = mutableListOf()

    init {
        entityList.forEach {
            if (it is SpaceShip) {
                convertedList.add(it)
                convertedList.addAll(it.playersOnBord)
            } else {
                convertedList.add(it)
            }
        }
        convertedList.add(currentCell)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityMenu {
        return EntityMenu(inflate(
                parent.context,
                R.layout.element_cell_info,
                null
        ))
    }

    override fun getItemCount(): Int = convertedList.size

    override fun onBindViewHolder(holder: EntityMenu, position: Int) {
        holder.update(convertedList[position], onClick)
    }

    class EntityMenu(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivEntityIcon: ImageView = itemView.findViewById(R.id.iv_entity_icon)
        private val tvEntityName: TextView = itemView.findViewById(R.id.tv_entity_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvCrystalCount: TextView = itemView.findViewById(R.id.tv_crystal_count)

        fun update(entity: CellProperties, onClick: (CellProperties) -> Unit) {
            ivEntityIcon.setImageBitmap(entity.bitmap)
            tvEntityName.text = entity.name
            tvDescription.text = entity.description
            tvCrystalCount.text = entity.crystals.toString()
            itemView.setOnClickListener { onClick.invoke(entity) }
        }
    }

}