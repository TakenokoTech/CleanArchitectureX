package tech.takenoko.cleanarchitecturex.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_top.*
import org.koin.android.ext.android.inject
import tech.takenoko.cleanarchitecturex.R
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

class TopFragment : Fragment() {

    private val viewModel: TopViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBinding()

        viewModel.load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    private fun onBinding() {
        viewModel.text1.observe(this, Observer { text1.text = it })
    }

    companion object {
        val TAG = TopFragment::class.java.simpleName
    }
}
