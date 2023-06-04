package com.example.stabilityGuard.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.stabilityGuard.R
import com.example.stabilityGuard.databinding.FragmentHomeBinding
import com.example.stabilityGuard.utils.AlarmAdapter
import com.example.stabilityGuard.utils.VerticalSpaceItemDecorator
import com.example.stabilityGuard.utils.toPx
import com.example.stabilityGuard.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Timer
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val timer = Timer()

        val interval = 15000L // 15 seconds

        timer.scheduleAtFixedRate(
            timerTask {
                viewModel.getAlarms()
            },
            0,
            interval,
        )

        observeLiveData()

        initRecyclerView()

        binding.surveillanceSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSurveillance(isEnabled = isChecked)
        }

        binding.swiperefresh.setOnRefreshListener(this)
    }

    private fun observeLiveData() {
        viewModel.alarmsSuccess.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = false
            alarmAdapter.setData(it)
        }

        viewModel.emailIntent.observe(viewLifecycleOwner) {
            Timber.d("Email sent!")
            it.setPackage("com.google.android.gm")
            startActivity(it)
        }
    }

    private fun initRecyclerView() {
        alarmAdapter = AlarmAdapter(
            onItemAck = {
                viewModel.ackAlarm(alarmId = it)
            },
            onItemCleared = {
                viewModel.clearAlarm(deviceId = it.deviceId, alarmId = it.id)
            },

        )
        val decoration =
            VerticalSpaceItemDecorator(resources.getInteger(R.integer.margin_tv_item).toPx())
        binding.rvAlarms.apply {
            adapter = alarmAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(decoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRefresh() {
        viewModel.getAlarms()
    }
}
