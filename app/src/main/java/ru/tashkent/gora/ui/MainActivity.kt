package ru.tashkent.gora.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.tashkent.R
import ru.tashkent.databinding.ActivityMainBinding
import ru.tashkent.gora.network.JsonPlaceholderApi
import ru.tashkent.gora.network.NetworkHandler
import ru.tashkent.gora.repositories.Repository
import ru.tashkent.gora.ui.users.UsersFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var viewModelProvider: ViewModelProviderFactory

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())

        val service = JsonPlaceholderApi.create()
        val networkHandler = NetworkHandler(this)
        val repository = Repository(service, networkHandler)
        viewModelProvider = ViewModelProviderFactory(repository)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, UsersFragment(), UsersFragment.TAG)
                .commit()
        }
    }
}