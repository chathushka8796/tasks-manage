package com.example.todo.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.fragments.AddTodoPopupFragment
import com.example.todo.fragments.RetrofitInstance
import com.example.todo.utlis.ToDoAdapter
import com.example.todo.utlis.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), AddTodoPopupFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddTodoPopupFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mlist: MutableList<ToDoData>
    private lateinit var timeTextView: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
        updateTime()
    }

    private fun registerEvents() {
        binding.addTaskBtn.setOnClickListener {
            if (popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddTodoPopupFragment()
            popUpFragment?.setListener(this)
            popUpFragment?.show(
                childFragmentManager,
                AddTodoPopupFragment.TAG
            )
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mlist = mutableListOf()
        adapter = ToDoAdapter(mlist)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
        timeTextView = binding.timeTextView
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mlist.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        mlist.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Task saved successfully!", Toast.LENGTH_SHORT).show()
                todoEt.text = null
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment?.dismiss()
        }
    }

    override fun onUpdateTask(toDoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUpFragment?.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        if (popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        popUpFragment = AddTodoPopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popUpFragment?.setListener(this)
        popUpFragment?.show(childFragmentManager, AddTodoPopupFragment.TAG)
    }

    private fun updateTime() {
        handler.post(object : Runnable {
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitInstance.api.getCurrentTime("Asia/Colombo")
                        withContext(Dispatchers.Main) {
                            timeTextView.text = response.time
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
}
