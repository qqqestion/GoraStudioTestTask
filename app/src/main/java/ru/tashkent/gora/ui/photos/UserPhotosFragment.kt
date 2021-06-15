package ru.tashkent.gora.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.tashkent.R
import ru.tashkent.databinding.FragmentUserPhotosBinding
import ru.tashkent.gora.adapters.PhotosAdapter
import ru.tashkent.gora.data.local.PhotoView
import ru.tashkent.gora.data.local.UserView
import ru.tashkent.gora.exceptions.Failure
import ru.tashkent.gora.ui.MainActivity
import ru.tashkent.gora.ui.core.BaseFragment
import timber.log.Timber

private const val ARGUMENT_USER_KEY = "argument.user"

class UserPhotosFragment : BaseFragment() {

    private var _binding: FragmentUserPhotosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserPhotosViewModel by viewModels {
        (activity as MainActivity).viewModelProvider
    }

    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<UserView>(ARGUMENT_USER_KEY)!!
        requireActivity().title = "Photos of ${user.name}"

        binding.srlRefresh.isRefreshing = true
        binding.srlRefresh.setOnRefreshListener {
            viewModel.loadPhotos(user.id)
        }

        photosAdapter = PhotosAdapter()
        binding.rvAlbums.adapter = photosAdapter
        binding.rvAlbums.layoutManager = LinearLayoutManager(requireContext())

        if (savedInstanceState == null) {
            viewModel.loadPhotos(user.id)
        }
        viewModel.failure.observe(viewLifecycleOwner, Observer(::handleError))
        viewModel.photos.observe(viewLifecycleOwner, Observer(::handleList))
    }

    private fun handleList(photos: List<PhotoView>) {
        photosAdapter.photos = photos
        binding.srlRefresh.isRefreshing = false
    }

    private fun handleError(failure: Failure) {
        Timber.d("$failure")
        binding.srlRefresh.isRefreshing = false
        when (failure) {
            is Failure.NetworkError -> {
                snackbar(R.string.failure_network)
            }
            is Failure.ServerError -> {
                snackbar(R.string.failure_server)
            }
            is Failure.TimeoutError -> {
                snackbar(R.string.failure_timeout)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        val TAG = "UserPhotosFragment"

        fun newInstance(user: UserView): UserPhotosFragment {
            val args = Bundle(1).apply { putParcelable(ARGUMENT_USER_KEY, user) }
            val fragment = UserPhotosFragment()
            fragment.arguments = args
            return fragment
        }
    }
}