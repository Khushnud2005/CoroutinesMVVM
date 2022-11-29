package uz.vianet.coroutinesmvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import uz.vianet.coroutinesmvvm.databinding.ActivityCreateBinding
import uz.vianet.coroutinesmvvm.model.Post
import uz.vianet.coroutinesmvvm.utils.Utils.toast
import uz.vianet.coroutinesmvvm.viewmodel.CreateViewModel

class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    lateinit var viewModel: CreateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[CreateViewModel::class.java]

        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            val title: String = binding.etTitle.getText().toString()
            val body: String = binding.etBody.getText().toString().trim { it <= ' ' }
            val id_user: String = binding.etUserId.getText().toString().trim { it <= ' ' }
            if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
                val post = Post(id_user.toInt(), title, body)
                viewModel.apiPostCreate(post)
                viewModel.newPost.observe(this){
                    val intent = Intent()
                    intent.putExtra("title", it.title)
                    setResult(RESULT_OK, intent)
                    super@CreateActivity.onBackPressed()
                }
                viewModel.error.observe(this){
                    toast(this,it.message.toString())
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelHandleData()
    }
}