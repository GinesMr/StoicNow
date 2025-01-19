
package com.example.stoic.ui.dashboard
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.stoic.R
import com.example.stoic.databinding.FragmentDashboardBinding
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PhilosopherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadPhilosophers()
    }


    private fun setupRecyclerView() {
        val sharedPreferences = requireActivity().getSharedPreferences("stoic_prefs", Context.MODE_PRIVATE)
        adapter = PhilosopherAdapter(sharedPreferences)

        binding.philosophersRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@DashboardFragment.adapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                    outRect.set(spacing, spacing, spacing, spacing)
                }
            })
        }
    }

    private fun loadPhilosophers() {
        val philosophers = listOf(
            Triple(R.drawable.cleanthes, "Cleanthes of Assos", "Sage of the Steadfast"),
            Triple(R.drawable.chrysippus, "Chrysippus of Soli", "Warden of Logic"),
            Triple(R.drawable.aristo, "Aristo of Chios", "Mystic of the Isles"),
            Triple(R.drawable.herillus, "Herillus of Carthage", "Scholar of Serenity"),
            Triple(R.drawable.sphaerus, "Sphaerus of Borysthenes", "Guardian of Reason"),
            Triple(R.drawable.persaeus, "Persaeus of Citium", "Keeper of Tranquility"),
            Triple(R.drawable.boethus, "Boethus of Sidon", "Philosopher of Harmony"),
            Triple(R.drawable.diogenes, "Diogenes of Babylon", "Explorer of Virtue"),
            Triple(R.drawable.antipater, "Antipater of Tarsus", "Sentinel of Wisdom"),
            Triple(R.drawable.poseidonius, "Poseidonius of Apamea", "Navigator of the Cosmos"),
            Triple(R.drawable.dardanus, "Dardanus of Athens", "Oracle of Discipline"),
            Triple(R.drawable.areus, "Areus Didymus", "Seeker of Clarity"),
            Triple(R.drawable.athenodorus, "Athenodorus of Tarsus", "Scribe of the Stars"),
            Triple(R.drawable.amelius, "Amelius", "Luminary of Truth"),
            Triple(R.drawable.polybius, "Polybius", "Historian of Resilience"),
            Triple(R.drawable.bion, "Bion of Borysthenes", "Jester of Reflection"),
            Triple(R.drawable.dionysius, "Dionysius of Heraclea", "Voyager of Liberty"),
            Triple(R.drawable.stratonicus, "Stratonicus of Lampsacus", "Pathfinder of Order"),
            Triple(R.drawable.demetrius, "Demetrius of Phaleron", "Guardian of Governance"),
            Triple(R.drawable.zeno_elea, "Zeno of Elea", "Paradox Weaver"),
            Triple(R.drawable.ceno, "Ceno of Citium", "Founder of Stoicism"),
            Triple(R.drawable.zenon_laodicea, "Zenon of Laodicea", "Sentinel of Insight"),
            Triple(R.drawable.seleucus, "Seleucus of Seleucia", "Keeper of Knowledge"),
            Triple(R.drawable.hierocles, "Hierocles of Alexandria", "Protector of Brotherhood"),
            Triple(R.drawable.seneca, "Seneca the Younger", "Sage of Shadows"),
            Triple(R.drawable.rusticus, "Junius Rusticus", "Teacher of Emperors"),
            Triple(R.drawable.galus, "Galus of Caesarea", "Wise Voyager"),
            Triple(R.drawable.musonius, "Musonius Rufus", "Advocate of Equality"),
            Triple(R.drawable.sextus, "Sextus Empiricus", "Skeptic of Certainty"),
            Triple(R.drawable.hadot, "Pierre Hadot", "Reviver of Stoicism"),
            Triple(R.drawable.epictetus, "Epictetus", "Freed Sage"),
            Triple(R.drawable.holiday, "Ryan Holiday", "Modern Pathfinder"),
            Triple(R.drawable.pigliucci, "Massimo Pigliucci", "Rational Explorer"),
            Triple(R.drawable.cicero, "Marcus Tullius Cicero", "Orator of Wisdom"),
            Triple(R.drawable.marque_aurelio, "Marcus Aurelius", "Emperor and Philosopher")
        )
        adapter.submitList(philosophers)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}