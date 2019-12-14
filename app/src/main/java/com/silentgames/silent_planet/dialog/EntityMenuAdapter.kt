package com.silentgames.silent_planet.dialog

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.dialog.EntityMenuAdapter.ElementType.*
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.effects.CaptureEffect
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.utils.getEntityList

class EntityMenuAdapter(
        entityList: MutableList<EntityType>,
        currentCell: CellType,
        private val onClick: (BaseProperties) -> Unit,
        private val onCapturedPlayerClick: (Player) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val convertedList: MutableList<BaseProperties> = mutableListOf()

    init {
        convertedList.addAll(entityList.getEntityList().sortedBy { it.hasEffect<CaptureEffect>() })
        convertedList.add(currentCell)
    }

    override fun getItemViewType(position: Int): Int {
        val element = convertedList[position]
        return if (element is EntityType && element.hasEffect<CaptureEffect>()) {
            CAPTURED_PLAYER.ordinal
        } else {
            OTHER.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (values()[viewType]) {
            CAPTURED_PLAYER -> CapturedPlayerViewHolder(inflate(
                    parent.context,
                    R.layout.element_captured_player_info,
                    null
            ))
            OTHER -> EntityMenu(inflate(
                    parent.context,
                    R.layout.element_cell_info,
                    null
            ))
        }
    }

    override fun getItemCount(): Int = convertedList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = convertedList[position]
        if (holder is EntityMenu) {
            holder.update(element, onClick)
        } else if (holder is CapturedPlayerViewHolder && element is Player && element.hasEffect<CaptureEffect>()) {
            holder.update(element, onCapturedPlayerClick)
        }
    }

    class EntityMenu(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivEntityIcon: ImageView = itemView.findViewById(R.id.iv_entity_icon)
        private val tvEntityName: TextView = itemView.findViewById(R.id.tv_entity_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvCrystalCount: TextView = itemView.findViewById(R.id.tv_crystal_count)

        fun update(entity: BaseProperties, onClick: (BaseProperties) -> Unit) {
            ivEntityIcon.setImageBitmap(entity.bitmap)
            tvEntityName.text = entity.name
            tvDescription.text = entity.description
            tvCrystalCount.text = entity.crystals.toString()
            itemView.setOnClickListener { onClick.invoke(entity) }
        }
    }

    class CapturedPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivEntityIcon: ImageView = itemView.findViewById(R.id.iv_entity_icon)
        private val tvEntityName: TextView = itemView.findViewById(R.id.tv_entity_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvCrystalCount: TextView = itemView.findViewById(R.id.tv_crystal_count)

        fun update(entity: Player, onClick: (Player) -> Unit) {
            ivEntityIcon.setImageBitmap(entity.bitmap)
            tvEntityName.text = entity.name
            tvDescription.text = entity.description
            tvCrystalCount.text = entity.getEffect<CaptureEffect>()?.buybackPrice?.toString() ?: ""
            itemView.setOnClickListener { onClick.invoke(entity) }
        }
    }

    private enum class ElementType {
        CAPTURED_PLAYER,
        OTHER
    }

}