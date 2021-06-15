package ru.tashkent.gora.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.tashkent.R
import ru.tashkent.databinding.FragmentUsersBinding
import ru.tashkent.gora.adapters.UsersAdapter
import ru.tashkent.gora.data.local.UserView
import ru.tashkent.gora.exceptions.Failure
import ru.tashkent.gora.ui.MainActivity
import ru.tashkent.gora.ui.core.BaseFragment
import ru.tashkent.gora.ui.photos.UserPhotosFragment
import timber.log.Timber

class UsersFragment : BaseFragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding: FragmentUsersBinding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels {
        (activity as MainActivity).viewModelProvider
    }

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Users"

        usersAdapter = UsersAdapter()
        usersAdapter.setOnUserClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, UserPhotosFragment.newInstance(it))
                .addToBackStack(UserPhotosFragment.TAG)
                .commit()
        }
        binding.rvUsers.adapter = usersAdapter
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())

        binding.srlRefresh.setOnRefreshListener {
            viewModel.loadUsers()
        }
        binding.srlRefresh.isRefreshing = true

        if (savedInstanceState == null) {
            viewModel.loadUsers()
        }
        viewModel.failure.observe(viewLifecycleOwner, Observer(::handleError))
        viewModel.users.observe(viewLifecycleOwner, Observer(::handleList))
    }

    private fun handleList(users: List<UserView>) {
        usersAdapter.users = users
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
        val TAG = "UsersFragment"
    }
}