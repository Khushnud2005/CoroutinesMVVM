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

class EditViewModel : ViewModel(),CoroutineScope {
    var job: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val error = MutableLiveData<Throwable>()
    val handler = CoroutineExceptionHandler { _, exception ->
        error.value = exception
    }
    val editedPost = MutableLiveData<Post>()

    fun apiPostUpdate(post: Post) {
        Log.d("@@EditViewModel","${post.title} Post Came To ViewModel")
        launch(Dispatchers.Main + handler) {
            editedPost.value = updatedPost(post)
        }
    }
    private suspend fun updatedPost(post: Post):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.updatePost(post.id,post).execute()
            Log.d("@@EditViewModel","${response.body()!!.title} Post Responded")
            return@async response.body()!!
        }.await()
    }
    fun cancelHandleData() {
        job?.cancel()
        job = null
    }
}