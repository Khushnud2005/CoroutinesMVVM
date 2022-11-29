package uz.vianet.coroutinesmvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import uz.vianet.coroutinesmvvm.adapter.PostAdapter
import uz.vianet.coroutinesmvvm.databinding.ActivityMainBinding
import uz.vianet.coroutinesmvvm.model.Post
import uz.vianet.coroutinesmvvm.utils.Utils.toast
import uz.vianet.coroutinesmvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var posts = ArrayList<Post>()
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        binding.floating.setOnClickListener { openCreateActivity() }
        viewModel.apiPostList()
        viewModel.allPosts.observe(this) {
            refreshAdapter(it)
        }
        viewModel.error.observe(this){
            toast(this,it.message.toString())
        }

        val extras = intent.extras
        if (extras != null) {
            binding.pbLoading.visibility = View.VISIBLE
            Toast.makeText(this@MainActivity,"${extras.getString("title")} Edited", Toast.LENGTH_LONG).show()
            viewModel.apiPostList()
        }
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {
        val adapter = PostAdapter(this, posts)
        binding.recyclerView.setAdapter(adapter)
        binding.pbLoading.visibility = View.GONE
    }
    fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        launchCreateActivity.launch(intent)
    }

    var launchCreateActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.pbLoading.visibility = View.VISIBLE
            if (result.data != null) {
                val title = result.data!!.getStringExtra("title")
                toast(this@MainActivity,"$title Created")
                viewModel.apiPostList()
            }
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelHandleData()
    }

}