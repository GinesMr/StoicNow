    package com.example.stoic.ui.home

    import android.content.SharedPreferences
    import android.icu.util.Calendar
    import androidx.lifecycle.ViewModel
    import com.example.stoic.R

    class HomeViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
        private val CLAIMED_PHILOSOPHERS_KEY = "claimed_philosophers"

        private val philosophers = listOf(
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

        fun getPhilosopherOfTheDay(): Triple<Int, String, String> {
            val lastUpdatedTime = sharedPreferences.getLong("lastUpdatedTime", 0L)
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastUpdatedTime >= 24 * 60 * 60 * 1000) {
                val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1
                val philosopher = philosophers[dayOfYear % philosophers.size]
                sharedPreferences.edit().putLong("lastUpdatedTime", currentTime).apply()
                return philosopher
            }
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1
            return philosophers[dayOfYear % philosophers.size]
        }

        fun getPhilosopherTimeRem(): Long {
            val lastUpdatedTime = sharedPreferences.getLong("lastUpdatedTime", 0L)
            val currentTime = System.currentTimeMillis()
            val timeInterval = 24 * 60 * 60 * 1000L

            val timeElapsed = currentTime - lastUpdatedTime
            val timeRemaining = timeInterval - timeElapsed

            return if (timeRemaining < 0) 0L else timeRemaining
        }

        fun isPhilosopherClaimed(philosopherName: String): Boolean {
            val claimedPhilosophers = getClaimedPhilosophers()
            return claimedPhilosophers.contains(philosopherName)
        }

        fun claimPhilosopher(philosopherName: String): Boolean {
            val lastClaimTime = sharedPreferences.getLong("lastClaimTime", 0L)
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastClaimTime < 24 * 60 * 60 * 1000) {
                return false
            }

            val claimedPhilosophers = getClaimedPhilosophers().toMutableSet()
            claimedPhilosophers.add(philosopherName)

            sharedPreferences.edit().apply {
                putStringSet(CLAIMED_PHILOSOPHERS_KEY, claimedPhilosophers)
                putLong("lastClaimTime", currentTime)
                apply()
            }

            return true
        }

        private fun getClaimedPhilosophers(): Set<String> {
            return sharedPreferences.getStringSet(CLAIMED_PHILOSOPHERS_KEY, setOf()) ?: setOf()
        }

        fun canClaimToday(): Boolean {
            val lastClaimTime = sharedPreferences.getLong("lastClaimTime", 0L)
            val currentTime = System.currentTimeMillis()
            return currentTime - lastClaimTime >= 24 * 60 * 60 * 1000
        }

        fun getPhilosopherInfo(name: String): String {
            return when (name) {
                "Cleanthes of Assos" ->
                    "Cleanthes of Assos, 'Sage of the Steadfast', was a Greek Stoic philosopher who succeeded Zeno of Citium and is known for his philosophical poetry on virtue and emotional control. He was known for his rigorous discipline and his role in defining Stoic philosophy, particularly through his work 'Hymn to Zeus', which reflects his belief in divine reason (Logos)."

                "Chrysippus of Soli" ->
                    "Chrysippus of Soli, 'Warden of Logic', was a Greek Stoic philosopher who played a crucial role in systematizing Stoicism, especially in logic and ethics. He wrote extensively on logic, ethics, and metaphysics, developing the Stoic system into a comprehensive worldview. He is credited with refining Stoic doctrines on determinism and fate, becoming the second most prolific Stoic after Zeno."

                "Aristo of Chios" ->
                    "Aristo of Chios, 'Mystic of the Isles', was a Greek philosopher known for his mystical and radical interpretation of Stoicism, focusing on self-sufficiency and independence. He rejected most conventional teachings of the Stoics, emphasizing the importance of an ascetic life. His contributions shaped the Stoic focus on personal autonomy and simplicity."

                "Herillus of Carthage" ->
                    "Herillus of Carthage, 'Scholar of Serenity', was a Greek philosopher and one of the first disciples of Zeno of Citium, known for his focus on inner serenity. He emphasized the significance of inner peace over external wealth and success, an idea that became a central tenet of Stoicism."

                "Sphaerus of Borysthenes" ->
                    "Sphaerus of Borysthenes, 'Guardian of Reason', was a Greek Stoic philosopher who emphasized the importance of reason in achieving virtue. He was a prominent Stoic teacher in Rome and served as an advisor to King Antigonus II of Macedonia, promoting Stoic ethics as a guide to ruling wisely and justly."

                "Persaeus of Citium" ->
                    "Persaeus of Citium, 'Keeper of Tranquility', was a Stoic philosopher who promoted tranquility and self-sufficiency as means to live in accordance with nature. A disciple of Zeno, Persaeus played a key role in the development of early Stoic thought, focusing on how virtue and reason lead to happiness and peace of mind."

                "Boethus of Sidon" ->
                    "Boethus of Sidon, 'Philosopher of Harmony', was a Stoic philosopher who explored the harmony between the individual and the cosmos, seeking balance in all things. He was known for synthesizing Stoicism with earlier Platonic thought and for his work on the nature of happiness and human flourishing."

                "Diogenes of Babylon" ->
                    "Diogenes of Babylon, 'Explorer of Virtue', was a Greek philosopher known for his focus on virtue and self-discipline as the pillars of a fulfilling life. He was influential in spreading Stoic ideas to Rome, particularly through his teachings on ethics and the importance of self-control in the face of adversity."

                "Antipater of Tarsus" ->
                    "Antipater of Tarsus, 'Sentinel of Wisdom', was a Greek philosopher and one of Zeno's most important disciples, known for his teachings on wisdom and morality. He helped establish Stoic ethics as a dominant framework in Hellenistic philosophy, influencing later Roman Stoics like Seneca and Marcus Aurelius."

                "Poseidonius of Apamea" ->
                    "Poseidonius of Apamea, 'Navigator of the Cosmos', was a Greek philosopher who combined Stoic philosophy with science and astronomy, seeking to explain the universe holistically. He believed that human actions were deeply connected to the cosmic order, and his work laid the groundwork for later developments in cosmology and ethics."

                "Dardanus of Athens" ->
                    "Dardanus of Athens, 'Oracle of Discipline', was a Stoic philosopher known for his emphasis on discipline and self-control as essential virtues. His teachings focused on the importance of reason and moral integrity, particularly in managing emotions and achieving a virtuous life."

                "Areus Didymus" ->
                    "Areus Didymus, 'Seeker of Clarity', was a Greek philosopher who stood out for his efforts to clarify and spread Stoic teachings. He contributed to the Stoic tradition by refining the practical aspects of Stoic philosophy, focusing on how to apply Stoicism in everyday life."

                "Athenodorus of Tarsus" ->
                    "Athenodorus of Tarsus, 'Scribe of the Stars', was a Stoic philosopher who focused on the importance of ethics and rational thought in guiding one's life. He was a mentor to several Roman emperors, and his teachings on self-control and moral virtue had a lasting influence on Stoic thought."

                "Amelius" ->
                    "Amelius, 'Luminary of Truth', was a philosopher who contributed to the development of Stoic ethics, emphasizing truth and moral clarity. He was a key figure in the later stages of Stoic philosophy and played a significant role in interpreting the works of earlier Stoic philosophers."

                "Polybius" ->
                    "Polybius, 'Historian of Resilience', was a Stoic historian who focused on the strength of character and resilience in facing adversity. His historical writings emphasized the role of virtue and resilience in the face of political instability, offering valuable insights into the challenges of leadership."

                "Bion of Borysthenes" ->
                    "Bion of Borysthenes, 'Jester of Reflection', was a Greek philosopher known for his witty and ironic approach to philosophical thought. His blend of humor and insight made Stoicism more accessible to a broader audience, and he is remembered for his lighthearted yet profound teachings on life and virtue."

                "Dionysius of Heraclea" ->
                    "Dionysius of Heraclea, 'Voyager of Liberty', was a Stoic philosopher focused on the liberation of the mind through wisdom and virtuous living. His teachings emphasized the importance of intellectual freedom and the role of philosophy in achieving a life of tranquility and self-sufficiency."

                "Stratonicus of Lampsacus" ->
                    "Stratonicus of Lampsacus, 'Pathfinder of Order', was a Stoic philosopher who believed in the pursuit of order and rationality in all aspects of life. He argued that virtue and wisdom lead to a harmonious life, which he considered the ultimate goal of human existence."

                "Demetrius of Phaleron" ->
                    "Demetrius of Phaleron, 'Guardian of Governance', was a Stoic philosopher who focused on politics and governance, seeking to apply Stoic principles to public life. He believed that rulers should be guided by wisdom, virtue, and fairness, using reason to govern effectively and justly."

                "Zeno of Elea" ->
                    "Zeno of Elea, 'Paradox Weaver', was a philosopher known for his paradoxes, which challenged conventional thinking and influenced the development of Stoic logic. His work on the nature of reality and the limits of human knowledge laid the foundation for much of Stoic epistemology."

                "Zeno of Citium" ->
                    "Zeno of Citium, 'Founder of Stoicism', was a philosopher from Cyprus who founded Stoicism, a school of thought that emphasized virtue, rationality, and living in harmony with nature. Zeno's teachings on the nature of the universe and the importance of self-control had a profound influence on Western philosophy."

                "Zenon of Laodicea" ->
                    "Zenon of Laodicea, 'Sentinel of Insight', was a Stoic philosopher who focused on gaining deep insights into nature and the human condition. He emphasized that understanding the world and one's place in it through reason and virtue was the key to a fulfilling life."

                "Seleucus of Seleucia" ->
                    "Seleucus of Seleucia, 'Keeper of Knowledge', was a philosopher who sought to preserve and share ancient wisdom through his teachings. His work as a philosopher helped bridge the gap between earlier Greek thought and the later developments of Stoicism."

                "Hierocles of Alexandria" ->
                    "Hierocles of Alexandria, 'Protector of Brotherhood', was a Stoic philosopher known for his views on ethics and the importance of human relationships. He believed that philosophy should guide individuals in how to treat others with respect, fairness, and compassion."

                "Seneca the Younger" ->
                    "Seneca the Younger, 'Sage of Shadows', was a Roman Stoic philosopher and statesman known for his works on ethics, control over emotions, and facing adversity with dignity. He served as an advisor to Emperor Nero and is remembered for his writings on the moral and practical aspects of Stoicism."

                "Junius Rusticus" ->
                    "Junius Rusticus, 'Teacher of Emperors', was a Stoic philosopher who mentored the Roman Emperor Marcus Aurelius, shaping his moral and ethical outlook. His teachings on virtue and moral clarity were central to Marcus's development as a philosopher and leader."

                "Galus of Caesarea" ->
                    "Galus of Caesarea, 'Wise Voyager', was a Stoic philosopher known for his travels and philosophical contributions on ethics and virtue. He focused on how the practice of Stoic ethics could bring about resilience and strength in the face of life's challenges."

                "Musonius Rufus" ->
                    "Musonius Rufus, 'Advocate of Equality', was a Roman Stoic philosopher who championed the equality of all people and the importance of moral education. He advocated for a simple and ascetic lifestyle, teaching that philosophy should be lived, not just discussed."

                "Sextus Empiricus" ->
                    "Sextus Empiricus, 'Skeptic of Certainty', was a philosopher who is considered one of the most important figures in the tradition of Pyrrhonism, advocating for skepticism in the search for truth. His writings challenged the certainty of knowledge, arguing that human perception is limited."

                "Pierre Hadot" ->
                    "Pierre Hadot, 'Reviver of Stoicism', was a 20th-century philosopher who played a significant role in reviving Stoic philosophy and emphasizing its practical application in modern life. He encouraged people to view philosophy as a way of life and not just an academic pursuit."

                "Epictetus" ->
                    "Epictetus, 'Freed Sage', was a Greek Stoic philosopher who, as a former slave, taught the importance of inner freedom, ethics, and the power of the mind. His teachings on self-discipline and resilience continue to inspire those seeking to live virtuous lives."

                "Ryan Holiday" ->
                    "Ryan Holiday, 'Modern Pathfinder', is a contemporary author and advocate for Stoicism, applying ancient philosophy to modern challenges and success. His books, such as 'The Obstacle is the Way' and 'Daily Stoic', bring Stoic principles to a wide audience."

                "Massimo Pigliucci" ->
                    "Massimo Pigliucci, 'Rational Explorer', is a modern philosopher known for his writings on Stoicism and rational thinking, encouraging people to live virtuous lives. His work in popularizing Stoic thought in the 21st century has been instrumental in bringing Stoicism to new generations."

                "Marcus Tullius Cicero" ->
                    "Marcus Tullius Cicero, 'Orator of Wisdom', was a Roman statesman, orator, and Stoic philosopher who contributed to the spread of Stoic ideas in Roman society. His works on rhetoric, philosophy, and politics have left a lasting impact on Western thought."

                "Marcus Aurelius" ->
                    "Marcus Aurelius, 'Emperor and Philosopher', was a Roman emperor and Stoic philosopher known for his Meditations, a series of personal writings on Stoic principles and leadership. His philosophical reflections on life, leadership, and resilience continue to inspire people around the world."

                else -> "Information not available."
            }
        }

        fun getClaimedCount(): Int {
            return getClaimedPhilosophers().size
        }

        fun getTotalPhilosophers(): Int {
            return philosophers.size
        }

        fun getCollectionProgress(): Float {
            return getClaimedCount().toFloat() / getTotalPhilosophers().toFloat()
        }
    }


