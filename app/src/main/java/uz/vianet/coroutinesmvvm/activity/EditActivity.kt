package uz.vianet.coroutinesmvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import uz.vianet.coroutinesmvvm.databinding.ActivityEditBinding
import uz.vianet.coroutinesmvvm.model.Post
import uz.vianet.coroutinesmvvm.utils.Utils.toast
import uz.vianet.coroutinesmvvm.viewmodel.EditViewModel

class EditActivity : AppCompatActivity() {
    lateinit var id: String
    lateinit var binding: ActivityEditBinding
    lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }
    fun initViews(){
        viewModel = ViewModelProvider(this)[EditViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            binding.etUserId.setText(extras.getString("user_id"))
            binding.etTitle.setText(extras.getString("title"))
            binding.etText.setText(extras.getString("body"))
            id = extras.getString("id")!!
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val body = binding.etText.text.toString().trim { it <= ' ' }
            val id_user = binding.etUserId.text.toString().trim { it <= ' ' }

            if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
                val post = Post(id.toInt(),id_user.toInt(), title, body)
                Log.d("@@EditActivity","Pot model Created")
                viewModel.apiPostUpdate(post)
                viewModel.editedPost.observe(this){
                    Log.d("@@EditActivity","${it.title} Post Edited")
                    val intent = Intent(this@EditActivity, MainActivity::class.java)
                    intent.putExtra("title", it.title)
                    startActivity(intent)
                }
                viewModel.error.observe(this){
                    toast(this,it.message.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelHandleData()
    }
}