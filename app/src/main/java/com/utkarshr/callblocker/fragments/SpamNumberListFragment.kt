package com.utkarshr.callblocker.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.utkarshr.callblocker.MainActivity
import com.utkarshr.callblocker.R
import com.utkarshr.callblocker.addItemDecorationWithoutLastDivider
import com.utkarshr.callblocker.database.SpamNumberDatabase
import com.utkarshr.callblocker.database.SpamNumberDatabaseDao
import com.utkarshr.callblocker.databinding.FragmentSpamNumberListBinding
import com.utkarshr.callblocker.recyclerview.spamlist.SpamListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_spam_number_list.view.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SpamNumberListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var spamListAdapter: SpamListAdapter? = null

    private lateinit var dao: SpamNumberDatabaseDao

    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSpamNumberListBinding = FragmentSpamNumberListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        dao = SpamNumberDatabase.getInstance(requireContext()).dao

        recyclerView = binding.root.spam_list_recycler_view
        setupRecyclerView(binding)
        setupFab(binding)

        populateData()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.tv_toolbar.text = "Call Blocker"
        mainActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mainActivity.toolbar.title = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun populateData() {
        val disposable = Observable.fromCallable { dao.getAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null) {
                    spamListAdapter?.spamNumbers = it
                }
            }, { error ->
                Toast.makeText(requireContext(), "Not able to fetch data\n${error.toString()}", Toast.LENGTH_LONG).show()
            })

        compositeDisposable.add(disposable)
    }

    private fun setupFab(binding: FragmentSpamNumberListBinding) {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun setupRecyclerView(binding: FragmentSpamNumberListBinding) {
        spamListAdapter = SpamListAdapter()

        recyclerView?.let {
            it.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = spamListAdapter
            }
        }

        recyclerView?.addItemDecorationWithoutLastDivider()
    }

    private fun requestPermissions() {
        val requiredPermissions: MutableList<String> = ArrayList()
        requiredPermissions.add(Manifest.permission.CALL_PHONE)
        requiredPermissions.add(Manifest.permission.READ_PHONE_STATE)
        requiredPermissions.add(Manifest.permission.READ_CALL_LOG)
        requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)

        val missingPermissions: MutableList<String> = ArrayList()

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(permission)
            }
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                missingPermissions.toTypedArray(),
                0
            )
        }
    }
}