package com.cusufcan.fotografpaylasma.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cusufcan.fotografpaylasma.R
import com.cusufcan.fotografpaylasma.adapter.PostAdapter
import com.cusufcan.fotografpaylasma.databinding.FragmentFeedBinding
import com.cusufcan.fotografpaylasma.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var popup: PopupMenu

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: PostAdapter
    private lateinit var posts: ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

        posts = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popup = PopupMenu(requireContext(), binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)

        binding.floatingActionButton.setOnClickListener { floatingActionButtonClicked(it) }

        fetchData()

        adapter = PostAdapter(posts)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.setHasFixedSize(true)
        binding.feedRecyclerView.adapter = adapter
    }

    private fun fetchData() {
        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    error.printStackTrace()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            posts.clear()
                            val docs = value.documents
                            for (doc in docs) {
                                val comment = doc.get("comment") as String
                                val userEmail = doc.get("email") as String
                                val downloadUrl = doc.get("downloadUrl") as String

                                val post = Post(downloadUrl, userEmail, comment)
                                posts.add(post)
                            }

                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun floatingActionButtonClicked(view: View) {
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.upload -> {
                val action = FeedFragmentDirections.actionFeedFragmentToUploadFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            R.id.signOut -> {
                auth.signOut()
                val action = FeedFragmentDirections.actionFeedFragmentToAuthFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }

        return true
    }
}