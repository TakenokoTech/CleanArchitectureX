package tech.takenoko.cleanarchitecturex.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top.*
import org.koin.android.ext.android.inject
import tech.takenoko.cleanarchitecturex.R
import tech.takenoko.cleanarchitecturex.databinding.FragmentTopBinding
import tech.takenoko.cleanarchitecturex.utils.AppLog
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

class TopFragment : Fragment() {

    private val viewModel: TopViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppLog.info(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        viewModel.load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AppLog.info(TAG, "onCreateView")
        val binding = DataBindingUtil.inflate<FragmentTopBinding>(inflater, R.layout.fragment_top, container, false).apply {
            lifecycleOwner = this@TopFragment
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onResume() {
        AppLog.info(TAG, "onResume")
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = RecyclerViewAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(activity).orientation))
        viewModel.list1.observe(this, Observer { list -> adapter.setItem(list) })
    }

    companion object {
        private val TAG = TopFragment::class.java.simpleName
    }
}
