package com.example.stoic.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stoic.R
import com.example.stoic.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class HomeFragment : Fragment() {
    private val PERMISSION_REQUEST_CODE = 1001
    private lateinit var handler: Handler
    private lateinit var updateTimer: Runnable
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        handler = Handler(Looper.getMainLooper())
        val sharedPreferences = requireActivity().getSharedPreferences("stoic_prefs", Context.MODE_PRIVATE)
        val factory = HomeViewModelFactory(sharedPreferences)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        setupUI()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClaimButton()
        updatePhilosopherStatus()
    }

    private fun setupUI() {
        displayPhilosopherOfTheDay()
        displayPhilosopherTimeToClaim()
        setupButtons()
    }

    private fun setupButtons() {
        binding.button.setOnClickListener {
            showPhilosopherInfo()
        }

        binding.downloadb.setOnClickListener {
            val philosopher = homeViewModel.getPhilosopherOfTheDay()
            showDownloadConfirmation(philosopher)
        }

        binding.share.setOnClickListener {
            shareImage()
        }
    }

    private fun setupClaimButton() {
        binding.button5.setOnClickListener {
            val philosopherName = binding.philosopherName.text.toString()

            if (homeViewModel.isPhilosopherClaimed(philosopherName)) {
                showToast("You already have this philosopher!")
                return@setOnClickListener
            }

            if (!homeViewModel.canClaimToday()) {
                showToast("You can only claim one philosopher per day!")
                return@setOnClickListener
            }

            if (homeViewModel.claimPhilosopher(philosopherName)) {
                showClaimSuccess()
                updatePhilosopherStatus()
            } else {
                showToast("Failed to claim philosopher. Try again tomorrow.")
            }
        }
    }

    private fun updatePhilosopherStatus() {
        val philosopher = homeViewModel.getPhilosopherOfTheDay()
        val isClaimed = homeViewModel.isPhilosopherClaimed(philosopher.second)

        binding.apply {
            if (isClaimed) {
                philosopherImage.clearColorFilter()
                button5.isEnabled = false
                button5.text = "Claimed"
            } else {
                philosopherImage.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                button5.isEnabled = homeViewModel.canClaimToday()
                button5.text = if (homeViewModel.canClaimToday()) "Claim" else "Come back tomorrow"
            }
        }
    }

    private fun displayPhilosopherOfTheDay() {
        val philosopher = homeViewModel.getPhilosopherOfTheDay()
        binding.apply {
            philosopherImage.setImageResource(philosopher.first)
            philosopherName.text = philosopher.second
            philosopherDescription.text = philosopher.third
        }
    }

    private fun displayPhilosopherTimeToClaim() {
        updateTimer = object : Runnable {
            override fun run() {
                val timeRemaining = homeViewModel.getPhilosopherTimeRem()
                if (timeRemaining > 0) {
                    val hours = timeRemaining / (1000 * 60 * 60)
                    val minutes = (timeRemaining % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (timeRemaining % (1000 * 60)) / 1000

                    binding.tiempo.text = "Time to claim it: %02d:%02d:%02d".format(hours, minutes, seconds)
                    handler.postDelayed(this, 1000)
                } else {
                    binding.tiempo.text = "New philosopher available!"
                }
            }
        }
        handler.post(updateTimer)
    }

    private fun showPhilosopherInfo() {
        val philosopherName = binding.philosopherName.text.toString()
        val info = homeViewModel.getPhilosopherInfo(philosopherName)
        val philosopherImage = homeViewModel.getPhilosopherOfTheDay().first

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_philosopher_info, null)

        view.findViewById<TextView>(R.id.philosopherInfoTitle).text = philosopherName
        view.findViewById<TextView>(R.id.philosopherInfoContent).text = info
        view.findViewById<ImageView>(R.id.philosopherImageExpanded).setImageResource(philosopherImage)

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun showDownloadConfirmation(philosopher: Triple<Int, String, String>) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Confirmation")
            setMessage("Are you sure you want to download the image of ${philosopher.second}?")
            setPositiveButton("Yes") { _, _ ->
                downloadImage(philosopher.first, philosopher.second)
            }
            setNegativeButton("No") { dialog, _ ->
                showToast("Download canceled")
                dialog.dismiss()
            }
            create().show()
        }
    }

    private fun downloadImage(imageResource: Int, philosopherName: String) {
        checkPermissions()
        try {
            val bitmap = BitmapFactory.decodeResource(resources, imageResource)
            val filename = "$philosopherName.jpg"
            saveImage(bitmap, filename)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error downloading image")
        }
    }

    private fun saveImage(bitmap: Bitmap, filename: String) {
        try {
            val fos: OutputStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageAndroid10Plus(filename)
            } else {
                saveImageLegacy(filename)
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            showToast("Image downloaded: $filename")
            notifyGallery(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error saving image")
        }
    }

    private fun saveImageAndroid10Plus(filename: String): OutputStream {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return requireContext().contentResolver.openOutputStream(uri!!)!!
    }

    private fun saveImageLegacy(filename: String): OutputStream {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        return FileOutputStream(image)
    }

    private fun shareImage() {
        try {
            val philosopher = homeViewModel.getPhilosopherOfTheDay()
            val bitmap = BitmapFactory.decodeResource(resources, philosopher.first)
            val file = File(requireContext().cacheDir, "philosopher_${philosopher.second}.jpg")

            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            }

            val imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_SUBJECT, "Philosopher of the Day")
                putExtra(Intent.EXTRA_TEXT, "Check out this philosopher: ${philosopher.second}")
            }

            startActivity(Intent.createChooser(shareIntent, "Share Philosopher"))
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error sharing image")
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun notifyGallery(filename: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename)
            val uri = Uri.fromFile(file)
            requireContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        }
    }

    private fun showClaimSuccess() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("¡Success!")
            setMessage("You have successfully claimed today's philosopher!")
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create().show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
        _binding = null
    }
}