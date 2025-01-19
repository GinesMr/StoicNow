    package com.example.stoic.ui.notifications

    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.webkit.WebChromeClient
    import android.webkit.WebSettings
    import android.webkit.WebView
    import android.webkit.WebViewClient
    import android.widget.Toast
    import androidx.activity.addCallback
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.stoic.databinding.FragmentNotificationsBinding

    class NotificationsFragment : Fragment() {
        private var _binding: FragmentNotificationsBinding? = null
        private val binding get() = _binding!!
        private lateinit var adapter: BookAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupRecyclerView()
            setupWebView()
            setupBackHandler()
        }

        private fun setupRecyclerView() {
            adapter = BookAdapter(requireContext()) { book ->
                openPdf(book)
            }

            binding.booksRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@NotificationsFragment.adapter
            }

            adapter.submitList(BookRepository.books)
        }

        private fun setupWebView() {
            binding.webView.apply {
                settings.apply {
                    javaScriptEnabled = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    setSupportZoom(true)
                    defaultZoom = WebSettings.ZoomDensity.FAR
                    allowFileAccess = true
                    allowContentAccess = true
                    allowFileAccessFromFileURLs = true
                    allowUniversalAccessFromFileURLs = true
                    setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
                }

                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        view?.loadUrl(url ?: "")
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        private fun setupBackHandler() {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                if (binding.webView.visibility == View.VISIBLE) {
                    showBooksList()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }

        private fun openPdf(book: Book) {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.webView.visibility = View.VISIBLE
                binding.booksRecyclerView.visibility = View.GONE

                val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body, html { margin: 0; padding: 0; height: 100%; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe src="https://docs.google.com/viewer?embedded=true&url=${book.url}" 
                        width="100%" 
                        height="100%" 
                        frameborder="0">
                </iframe>
            </body>
            </html>
        """.trimIndent()

                binding.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

            } catch (e: Exception) {
                Log.e("PDF", "Error opening PDF", e)
                Toast.makeText(
                    context,
                    "Error opening PDF: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                binding.progressBar.visibility = View.GONE
                showBooksList()
            }
        }
        private fun showBooksList() {
            binding.webView.visibility = View.GONE
            binding.booksRecyclerView.visibility = View.VISIBLE
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }