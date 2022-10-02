package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentFavorBinding
import cn.skygard.happyoj.repo.database.AppDatabase
import cn.skygard.happyoj.repo.remote.model.Task
import cn.skygard.happyoj.view.activity.LabActivity
import cn.skygard.happyoj.view.adapter.TasksRvAdapter
import kotlinx.coroutines.*

class FavorFragment : BaseBindFragment<FragmentFavorBinding>() {

    private val rvAdapter by lazy {
        TasksRvAdapter { item ->
            LabActivity.start(requireContext(), item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            rvFavor.run {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            refresh()
            srlFavors.setOnRefreshListener {
                refresh()
                srlFavors.isRefreshing = false
            }
        }
    }

    private fun refresh() {
        val favors = (defaultSp.getStringSet("favors", emptySet())?: emptySet()).map {
            it.toLong()
        }
        CoroutineScope(Dispatchers.IO + Job()).launch {
            val favorTasks = favors.mapNotNull {
                AppDatabase.INSTANCE.taskDao().get(it)?.let { t ->
                    Task.TaskSubject(
                        id = t.tid,
                        title = t.title,
                        summary = t.summary,
                        updateTime =  t.date,
                        imageUrl = t.imageUrl,
                        deadline = t.deadline
                    )
                }
            }.toList()
            withContext(Dispatchers.Main) {
                rvAdapter.submitList(favorTasks)
            }
        }
    }

    companion object {
        fun newInstance() = FavorFragment()
    }

}