package com.example.stoic.ui.dashboard

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stoic.databinding.ItemPhilosopherCardBinding

class PhilosopherAdapter(
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<PhilosopherAdapter.PhilosopherViewHolder>() {

    private var philosophers = listOf<Triple<Int, String, String>>()

    class PhilosopherViewHolder(val binding: ItemPhilosopherCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhilosopherViewHolder {
        val binding = ItemPhilosopherCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhilosopherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhilosopherViewHolder, position: Int) {
        val philosopher = philosophers[position]
        val isClaimed = isPhilosopherClaimed(philosopher.second)

        holder.binding.apply {
            philosopherImage.setImageResource(philosopher.first)
            philosopherName.text = philosopher.second
            philosopherTitle.text = philosopher.third

            if (!isClaimed) {
                // Aplicar filtro gris a la imagen
                philosopherImage.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                lockedText.visibility = View.VISIBLE

            } else {
                // Imagen sin filtro
                philosopherImage.clearColorFilter()
                lockedText.visibility = View.GONE
                // Texto normal
                philosopherName.alpha = 1f
                philosopherTitle.alpha = 1f
            }
        }
    }

    override fun getItemCount() = philosophers.size

    fun submitList(list: List<Triple<Int, String, String>>) {
        philosophers = list
        notifyDataSetChanged()
    }

    private fun isPhilosopherClaimed(philosopherName: String): Boolean {
        val claimedPhilosophers = sharedPreferences.getStringSet("claimed_philosophers", setOf()) ?: setOf()
        return claimedPhilosophers.contains(philosopherName)
    }
}