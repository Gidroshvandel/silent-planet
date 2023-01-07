package com.silentgames.silent_planet.dialog

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.dialog.EntityMenuAdapter.ElementType.*
import com.silentgames.silent_planet.engine.TextureLoader

class EntityMenuAdapter(
    entityList: MutableList<EntityData>,
    currentCellData: EntityData,
    private val onClick: (EntityData) -> Unit,
    private val onCapturedPlayerClick: (EntityData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val convertedList: MutableList<EntityData> = mutableListOf()

    init {
        convertedList.addAll(entityList.sortedBy { it.captured })
        convertedList.add(currentCellData)
    }

    override fun getItemViewType(position: Int): Int {
        val element = convertedList[position]
        return if (element.captured) {
            CAPTURED_PLAYER.ordinal
        } else {
            OTHER.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (values()[viewType]) {
            CAPTURED_PLAYER -> CapturedPlayerViewHolder(
                inflate(
                    parent.context,
                    R.layout.element_captured_player_info,
                    null
                )
            )
            OTHER -> EntityMenu(
                inflate(
                    parent.context,
                    R.layout.element_cell_info,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int = convertedList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = convertedList[position]
        if (holder is EntityMenu) {
            holder.update(element, onClick)
        } else if (holder is CapturedPlayerViewHolder && element.captured) {
            holder.update(element, onCapturedPlayerClick)
        }
    }

    class EntityMenu(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivEntityIcon: ImageView = itemView.findViewById(R.id.iv_entity_icon)
        private val tvEntityName: TextView = itemView.findViewById(R.id.tv_entity_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvCrystalCount: TextView = itemView.findViewById(R.id.tv_crystal_count)

        fun update(entity: EntityData, onClick: (EntityData) -> Unit) {
            ivEntityIcon.setImageBitmap(TextureLoader.load(itemView.context, entity.texture))
            tvEntityName.text = entity.name
            tvDescription.text = entity.description
            tvCrystalCount.text = entity.crystalCount
            itemView.setOnClickListener { onClick.invoke(entity) }
        }
    }

    class CapturedPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivEntityIcon: ImageView = itemView.findViewById(R.id.iv_entity_icon)
        private val tvEntityName: TextView = itemView.findViewById(R.id.tv_entity_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvCrystalCount: TextView = itemView.findViewById(R.id.tv_crystal_count)

        fun update(entity: EntityData, onClick: (EntityData) -> Unit) {
            ivEntityIcon.setImageBitmap(TextureLoader.load(itemView.context, entity.texture))
            tvEntityName.text = entity.name
            tvDescription.text = entity.description
            tvCrystalCount.text = entity.crystalCount
            itemView.setOnClickListener { onClick.invoke(entity) }
        }
    }

    private enum class ElementType {
        CAPTURED_PLAYER,
        OTHER
    }
}

class EntityData(
    val id: Long,
    val texture: String,
    val name: String,
    val description: String,
    val crystalCount: String,
    val captured: Boolean = false
)
