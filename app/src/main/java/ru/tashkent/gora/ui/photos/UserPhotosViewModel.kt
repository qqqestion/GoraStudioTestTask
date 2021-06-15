package ru.tashkent.gora.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.tashkent.gora.data.local.PhotoView
import ru.tashkent.gora.data.remote.Photo
import ru.tashkent.gora.repositories.Repository
import ru.tashkent.gora.ui.core.BaseViewModel

class UserPhotosViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _photos = MutableLiveData<List<PhotoView>>()
    val photos: LiveData<List<PhotoView>> = _photos

    fun loadPhotos(userId: Int) = viewModelScope.launch {
        repository.photosForUser(userId).fold(::handleFailure, ::handleAlbumList)
    }

    private fun handleAlbumList(photos: List<Photo>) {
        _photos.value = photos.map { PhotoView(it.id, it.title, it.url) }
    }
}