package uz.vianet.coroutinesmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.vianet.coroutinesmvvm.model.Post
import uz.vianet.coroutinesmvvm.network.RetrofitHttp
import kotlin.coroutines.CoroutineContext

class CreateViewModel :ViewModel(),CoroutineScope {
    var job: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val newPost = MutableLiveData<Post>()
    val error = MutableLiveData<Throwable>()
    val handler = CoroutineExceptionHandler { _, exception ->
        error.value = exception
    }
    fun apiPostCreate(post: Post) {
        launch(Dispatchers.Main + handler) {
            newPost.value = createNewPost(post)
        }
    }
    private suspend fun createNewPost(post: Post):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.createPost(post).execute()
            return@async response.body()!!
        }.await()
    }

    fun cancelHandleData() {
        job?.cancel()
        job = null
    }
}