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

class DetailViewModel : ViewModel(),CoroutineScope{

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
    val detailPost = MutableLiveData<Post>()

    fun apiPostDetail(id: Int) {
        Log.d("@@DetailViewModel","$id Post Came To ViewModel")
        launch(Dispatchers.Main + handler) {
            detailPost.value = getPost(id)
        }
    }
    private suspend fun getPost(id:Int):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.detailPost(id).execute()
            Log.d("@@DetailViewModel","${response.body()!!.title} Post Responded")
            return@async response.body()!!
        }.await()
    }
    fun cancelHandleData() {
        job?.cancel()
        job = null
    }
}