package com.example.stoic.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stoic.R

class DashboardViewModel : ViewModel() {

    private val _philosophers = MutableLiveData<List<Triple<Int, String, String>>>()
    val philosophers: LiveData<List<Triple<Int, String, String>>> = _philosophers

    init {
        _philosophers.value = listOf(
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
    }
}

