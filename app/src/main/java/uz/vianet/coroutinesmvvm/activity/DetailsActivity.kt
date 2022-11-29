package uz.vianet.coroutinesmvvm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import uz.vianet.coroutinesmvvm.databinding.ActivityDetailsBinding
import uz.vianet.coroutinesmvvm.utils.Utils.toast
import uz.vianet.coroutinesmvvm.viewmodel.DetailViewModel

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            val id = extras.getInt("id")
            viewModel.apiPostDetail(id)
            viewModel.detailPost.observe(this){
                binding.tvTitle.setText(it.title.uppercase())
                binding.tvBody.setText(it.body)
            }
            viewModel.error.observe(this){
                toast(this,it.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelHandleData()
    }
}